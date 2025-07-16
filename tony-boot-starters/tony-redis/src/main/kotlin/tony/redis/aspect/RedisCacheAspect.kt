/*
 * MIT License
 *
 * Copyright (c) 2023-present, tangli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package tony.redis.aspect

import com.fasterxml.jackson.databind.JavaType
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.After
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.slf4j.Logger
import org.springframework.expression.EvaluationException
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext
import tony.annotation.redis.RedisCacheEvict
import tony.annotation.redis.RedisCacheable
import tony.core.PROJECT_GROUP
import tony.core.exception.ApiException
import tony.core.utils.asToNotNull
import tony.core.utils.getLogger
import tony.core.utils.isBooleanType
import tony.core.utils.isDateTimeLikeType
import tony.core.utils.isNumberType
import tony.core.utils.isStringLikeType
import tony.core.utils.jsonToObj
import tony.core.utils.rawClass
import tony.core.utils.secondOfTodayRest
import tony.core.utils.toJavaType
import tony.redis.RedisKeys
import tony.redis.RedisManager
import tony.redis.toNum

/**
 * 默认 Redis 缓存切面实现。
 *
 * 通过 AOP 拦截 @RedisCacheable/@RedisCacheEvict 注解，实现方法级别的 Redis 缓存和自动过期。
 * 支持 SpEL 表达式动态生成缓存 key，支持多种序列化方式（Jackson/Protostuff等）。
 *
 * 注意事项：
 * - 不支持缓存返回类型为日期时间类（如 LocalDateTime、Date 等）的方法。
 * - 若缓存 key 生成表达式或参数异常，会抛出 ApiException。
 * - 建议业务方法幂等，避免缓存击穿带来的并发问题。
 *
 * @author tangli
 * @date 2023/09/28 19:55
 */
@Aspect
public abstract class RedisCacheAspect {
    private val logger: Logger = getLogger()

    /**
     * SpEL 表达式解析器。
     */
    private val expressionParser = SpelExpressionParser()

    /**
     * 执行缓存删除（@RedisCacheEvict）。
     *
     * @param joinPoint AOP 连接点，代表被拦截的方法调用
     * @throws ApiException 表达式解析或 key 生成异常时抛出
     * @author tangli
     * @date 2023/09/13 19:43
     */
    @After(
        "@annotation($PROJECT_GROUP.annotation.redis.RedisCacheEvict.Container) || " +
            "@annotation($PROJECT_GROUP.annotation.redis.RedisCacheEvict)"
    )
    public fun doCacheEvict(joinPoint: JoinPoint) {
        val arguments = joinPoint.args
        val methodSignature = joinPoint.signature.asToNotNull<MethodSignature>()
        val paramsNames = methodSignature.parameterNames
        val annotations =
            methodSignature
                .method
                .getAnnotationsByType(RedisCacheEvict::class.java)
        annotations.forEach { annotation ->
            val cacheKey = cacheKey(arguments, paramsNames, annotation.expressions, annotation.cacheKey)
            RedisManager.delete(cacheKey)
        }
    }

    /**
     * 方法缓存处理（@RedisCacheable）。
     *
     * 先查 Redis 缓存，命中则直接返回，否则执行方法并写入缓存。
     *
     * @param joinPoint AOP 连接点，代表被拦截的方法调用
     * @param annotation RedisCacheable 注解实例
     * @return 方法执行结果或缓存值
     * @throws ApiException 返回类型为日期时间类或表达式/key 生成异常时抛出
     * @author tangli
     * @date 2023/09/13 19:43
     */
    @Around("@annotation(annotation)")
    public fun doCacheable(
        joinPoint: ProceedingJoinPoint,
        annotation: RedisCacheable,
    ): Any? {
        val arguments = joinPoint.args
        val methodSignature = joinPoint.signature.asToNotNull<MethodSignature>()
        val paramsNames = methodSignature.parameterNames
        val cacheKey = cacheKey(arguments, paramsNames, annotation.expressions, annotation.cacheKey)
        val timeout = if (annotation.expire == RedisCacheable.TODAY_END) secondOfTodayRest() else annotation.expire

        val javaType =
            methodSignature
                .method
                .genericReturnType
                .toJavaType()

        if (javaType.isDateTimeLikeType()) {
            throw ApiException("Not support dateTimeLike type.")
        }

        return getCachedValueByType(cacheKey, javaType)
            ?: joinPoint
                .proceed()
                ?.also { result ->
                    RedisManager
                        .values
                        .set(
                            cacheKey,
                            result,
                            timeout
                        )
                }
    }

    /**
     * 解析 SpEL 表达式，获取参数值或对象属性值。
     *
     * @param expression SpEL 表达式，如 "user.id"
     * @param paramMap 参数名到参数值的映射
     * @return 表达式解析后的值
     * @throws ApiException 表达式解析失败时抛出
     */
    private fun getValueFromParam(
        expression: String,
        paramMap: Map<String, Any?>,
    ): Any? {
        val stringList = expression.split(".")
        if (stringList.size < 2) {
            return paramMap[expression]
        }
        val paramName = stringList.first()
        val realExpression =
            stringList
                .drop(1)
                .joinToString(".")
        return try {
            expressionParser
                .parseExpression(realExpression)
                .getValue(StandardEvaluationContext(paramMap[paramName]))
        } catch (e: EvaluationException) {
            logger.error(e.message, e)
            throw ApiException(e.message, cause = e)
        }
    }

    /**
     * 获取缓存中的值，并根据返回类型做类型转换。
     *
     * 由子类实现，支持不同序列化方式。
     *
     * @param cacheKey 缓存 key
     * @param javaType 返回值类型
     * @return 缓存中的值，若无则返回 null
     */
    protected abstract fun getCachedValueByType(
        cacheKey: String,
        javaType: JavaType,
    ): Any?

    /**
     * 生成实际的缓存 key。
     *
     * 支持 SpEL 表达式动态拼接参数，若无表达式则直接用模板 key。
     *
     * @param arguments 方法实际参数
     * @param paramsNames 方法参数名数组
     * @param expressions 注解上的 SpEL 表达式数组
     * @param cacheKey 缓存 key 模板
     * @return 最终缓存 key
     */
    private fun cacheKey(
        arguments: Array<Any?>,
        paramsNames: Array<String>,
        expressions: Array<String>,
        cacheKey: String,
    ): String {
        if (paramsNames.isEmpty() ||
            expressions.isEmpty()
        ) {
            return RedisKeys.genKey(cacheKey)
        }
        val paramMap =
            paramsNames.foldIndexed<String, MutableMap<String, Any?>>(mutableMapOf()) { index, paramMap, paramName ->
                paramMap[paramName] = arguments[index]
                paramMap
            }
        val paramsValues =
            expressions.foldIndexed<String, Array<Any?>>(
                Array(expressions.size) {}
            ) { index, paramsValues, expression ->
                paramsValues.apply {
                    this[index] = getValueFromParam(expression, paramMap)
                }
            }
        return RedisKeys.genKey(cacheKey, *paramsValues)
    }
}

/**
 * Jackson Redis 缓存切面实现。
 *
 * 支持字符串、数字、布尔、枚举、对象等多种类型的缓存序列化与反序列化。
 *
 * @author tangli
 * @date 2023/09/28 19:55
 */
internal class JacksonRedisCacheAspect : RedisCacheAspect() {
    override fun getCachedValueByType(
        cacheKey: String,
        javaType: JavaType,
    ): Any? =
        RedisManager
            .values
            .get<String>(cacheKey)
            ?.let { cachedValue ->
                when {
                    javaType.isStringLikeType() -> {
                        cachedValue
                    }

                    javaType.isNumberType() -> {
                        cachedValue.toNum(javaType.rawClass())
                    }

                    javaType.isBooleanType() -> {
                        cachedValue.toBooleanStrictOrNull()
                    }

                    javaType.isEnumImplType -> {
                        javaType.rawClass.enumConstants.firstOrNull {
                            cachedValue ==
                                it.toString()
                        }
                    }

                    else -> {
                        cachedValue.jsonToObj(javaType)
                    }
                }
            }
}
