package com.tony.cache.aspect

import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.type.TypeFactory
import com.tony.PROJECT_GROUP
import com.tony.cache.RedisKeys
import com.tony.cache.RedisManager
import com.tony.cache.annotation.RedisCacheEvict
import com.tony.cache.annotation.RedisCacheable
import com.tony.exception.ApiException
import com.tony.utils.isArrayLikeType
import com.tony.utils.isBooleanType
import com.tony.utils.isByteType
import com.tony.utils.isDateTimeLikeType
import com.tony.utils.isDoubleType
import com.tony.utils.isFloatType
import com.tony.utils.isIntType
import com.tony.utils.isLongType
import com.tony.utils.isShortType
import com.tony.utils.isStringLikeType
import com.tony.utils.jsonToObj
import com.tony.utils.secondOfTodayRest
import com.tony.utils.toJsonString
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.After
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.slf4j.LoggerFactory
import org.springframework.expression.EvaluationException
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext
import java.math.BigDecimal
import java.math.BigInteger

/**
 * 默认RedisCache实现.
 * 给常规的 @Cacheable 加了过期时间
 *
 * @author tangli
 * @since 2023/5/24 18:09
 */
@Aspect
public class DefaultRedisCacheAspect {

    private val logger = LoggerFactory.getLogger(DefaultRedisCacheAspect::class.java)

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
        val paramsValues = expressions.foldIndexed<String, Array<Any?>>(
            Array(expressions.size) {},
        ) { index, paramsValues, expression ->
            paramsValues.apply {
                this[index] = getValueFromParam(expression, paramMap)
            }
        }
        return RedisKeys.genKey(cacheKey, *paramsValues)
    }

    @After("@annotation($PROJECT_GROUP.cache.annotation.RedisCacheEvict.Container)")
    public fun doCacheEvict(joinPoint: JoinPoint) {
        val arguments = joinPoint.args
        val methodSignature = joinPoint.signature as MethodSignature
        val paramsNames = methodSignature.parameterNames
        val annotations = methodSignature.method.getAnnotationsByType(RedisCacheEvict::class.java)
        annotations.forEach { annotation ->
            val cacheKey = cacheKey(arguments, paramsNames, annotation.expressions, annotation.cacheKey)
            RedisManager.delete(cacheKey)
        }
    }

    @Around("@annotation(annotation)")
    public fun doCacheable(joinPoint: ProceedingJoinPoint, annotation: RedisCacheable): Any? {
        val arguments = joinPoint.args
        val methodSignature = joinPoint.signature as MethodSignature
        val paramsNames = methodSignature.parameterNames
        val cacheKey = cacheKey(arguments, paramsNames, annotation.expressions, annotation.cacheKey)
        val timeout = if (annotation.expire == RedisCacheable.TODAY_END) secondOfTodayRest() else annotation.expire
        val cachedValue = RedisManager.values.getString(cacheKey)

        val javaType =
            TypeFactory
                .defaultInstance()
                .constructType(methodSignature.method.genericReturnType)

        if (javaType.isDateTimeLikeType()) {
            throw ApiException("Not support dateTimeLike type.")
        }

        if (cachedValue == null) {
            val result = joinPoint.proceed()
            if (result == null) {
                if (annotation.cacheEmpty) {
                    RedisManager.values.set(
                        cacheKey,
                        getCachedEmptyValueByType(javaType),
                        timeout,
                    )
                }
            } else {
                RedisManager.values.set(cacheKey, toCachedValueByType(result, javaType) as Any, timeout)
            }
            return result
        }

        return getCachedValueByType(cachedValue, javaType)
    }

    /**
     * 表达式解析器
     */
    private val expressionParser = SpelExpressionParser()

    /**
     * 获取spel表达式后的结果
     *
     * @param expression  表达式
     * @param paramMap 参数map
     * @return 执行spel表达式后的结果
     */
    private fun getValueFromParam(expression: String, paramMap: Map<String, Any?>): Any? {
        val stringList = expression.split(".")
        if (stringList.size < 2) {
            return paramMap[expression]
        }
        val paramName = stringList.first()
        val realExpression = stringList.drop(1).joinToString(".")
        return try {
            expressionParser
                .parseExpression(realExpression)
                .getValue(StandardEvaluationContext(paramMap[paramName]))
        } catch (e: EvaluationException) {
            logger.error(e.message, e)
            throw ApiException(e.message, cause = e)
        }
    }

    private fun toCachedValueByType(result: Any?, javaType: JavaType): Any? = when {
        javaType.isStringLikeType() -> result
        javaType.isByteType() -> result
        javaType.isShortType() -> result
        javaType.isIntType() -> result
        javaType.isLongType() -> result
        javaType.isFloatType() -> result
        javaType.isDoubleType() -> result
        javaType.isTypeOrSubTypeOf(BigDecimal::class.java) -> result
        javaType.isTypeOrSubTypeOf(BigInteger::class.java) -> result
        javaType.isBooleanType() -> result
        else -> result.toJsonString()
    }

    private fun getCachedEmptyValueByType(javaType: JavaType): String = when {
        javaType.isStringLikeType() -> ""
        javaType.isByteType() -> ""
        javaType.isShortType() -> ""
        javaType.isIntType() -> ""
        javaType.isLongType() -> ""
        javaType.isFloatType() -> ""
        javaType.isDoubleType() -> ""
        javaType.isTypeOrSubTypeOf(BigDecimal::class.java) -> ""
        javaType.isTypeOrSubTypeOf(BigInteger::class.java) -> ""
        javaType.isBooleanType() -> ""
        javaType.isArrayLikeType() -> "[]"
        else -> "{}"
    }

    private fun getCachedValueByType(cachedValue: String?, javaType: JavaType): Any? = when {
        javaType.isStringLikeType() -> cachedValue
        javaType.isByteType() -> cachedValue?.toByteOrNull()
        javaType.isShortType() -> cachedValue?.toShortOrNull()
        javaType.isIntType() -> cachedValue?.toIntOrNull()
        javaType.isLongType() -> cachedValue?.toLongOrNull()
        javaType.isFloatType() -> cachedValue?.toFloatOrNull()
        javaType.isDoubleType() -> cachedValue?.toDoubleOrNull()
        javaType.isTypeOrSubTypeOf(BigDecimal::class.java) -> cachedValue?.toBigDecimalOrNull()
        javaType.isTypeOrSubTypeOf(BigInteger::class.java) -> cachedValue?.toBigIntegerOrNull()
        javaType.isBooleanType() -> cachedValue?.toBooleanStrictOrNull()
        else -> cachedValue?.jsonToObj(
            TypeFactory
                .defaultInstance()
                .constructType(javaType),
        )
    }
}
