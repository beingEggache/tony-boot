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
import tony.PROJECT_GROUP
import tony.annotation.redis.RedisCacheEvict
import tony.annotation.redis.RedisCacheable
import tony.exception.ApiException
import tony.redis.RedisKeys
import tony.redis.RedisManager
import tony.redis.toNum
import tony.utils.asToNotNull
import tony.utils.getLogger
import tony.utils.isBooleanType
import tony.utils.isDateTimeLikeType
import tony.utils.isNumberType
import tony.utils.isStringLikeType
import tony.utils.jsonToObj
import tony.utils.rawClass
import tony.utils.secondOfTodayRest
import tony.utils.toJavaType

/**
 * 默认RedisCache实现.
 *
 * 给常规的 @Cacheable 加了过期时间.
 * @author tangli
 * @date 2023/09/28 19:55
 * @since 1.0.0
 */
@Aspect
public abstract class RedisCacheAspect {
    private val logger: Logger = getLogger()

    /**
     * 表达式解析器
     */
    private val expressionParser = SpelExpressionParser()

    /**
     * 执行删除缓存
     * @param [joinPoint] 连接点
     * @author tangli
     * @date 2023/09/13 19:43
     * @since 1.0.0
     */
    @After("@annotation($PROJECT_GROUP.annotation.redis.RedisCacheEvict.Container)")
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
     * 缓存
     * @param [joinPoint] 连接点
     * @param [annotation] 注解
     * @return [Any]?
     * @author tangli
     * @date 2023/09/13 19:43
     * @since 1.0.0
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
     * 获取spel表达式后的结果
     *
     * @param expression  表达式
     * @param paramMap 参数map
     * @return 执行spel表达式后的结果
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

    protected abstract fun getCachedValueByType(
        cacheKey: String,
        javaType: JavaType,
    ): Any?

    /**
     * 生成实际的缓存键名
     * @param arguments 方法实际参数
     * @param paramsNames 方法参数名数组
     * @param cacheKey 缓存键名或缓存键模板
     * @param expressions 注解上对应的SpEl表达式
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
 * jackson RedisCache实现.
 *
 * 给常规的 @Cacheable 加了过期时间.
 * @author tangli
 * @date 2023/09/28 19:55
 * @since 1.0.0
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
