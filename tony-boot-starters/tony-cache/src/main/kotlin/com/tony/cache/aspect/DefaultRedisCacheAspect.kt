@file:Suppress("unused")

package com.tony.cache.aspect

import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.type.TypeFactory
import com.tony.PROJECT_GROUP
import com.tony.cache.RedisKeys
import com.tony.cache.RedisManager
import com.tony.cache.annotation.RedisCacheEvict
import com.tony.cache.annotation.RedisCacheable
import com.tony.exception.ApiException
import com.tony.utils.doIf
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
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext
import java.math.BigDecimal
import java.math.BigInteger

@Aspect
class DefaultRedisCacheAspect {

    @Pointcut("@annotation($PROJECT_GROUP.cache.annotation.RedisCacheEvict.Container)")
    fun redisCacheEvict() = Unit

    @Pointcut("@annotation($PROJECT_GROUP.cache.annotation.RedisCacheable)")
    fun redisCacheable() = Unit

    private fun cacheKey(
        arguments: Array<Any>,
        paramsNames: Array<String>,
        cacheKey: String,
        redisParamsNames: Array<String>,
        usePrefix: Boolean = true
    ): String {
        if (paramsNames.isEmpty() ||
            redisParamsNames.isEmpty()
        ) {
            return if (usePrefix) {
                RedisKeys.genKey(cacheKey)
            } else {
                cacheKey
            }
        }

        val paramsValues =
            redisParamsNames.foldIndexed(
                Array<Any?>(redisParamsNames.size) {}
            ) { index, paramsValues, redisParamName ->
                if (redisParamName.startsWith("#")) {
                    val indexOfDot = redisParamName.indexOf(".")
                    val indexOfFirst = paramsNames.indexOfFirst { it == redisParamName.substring(2, indexOfDot) }
                    paramsValues.apply {
                        this[index] = SpelExpressionParser()
                            .parseExpression(redisParamName.substring(indexOfDot + 1, redisParamName.indexOf("}")))
                            .getValue(StandardEvaluationContext(arguments[indexOfFirst]))
                    }
                } else {
                    val indexOfFirst = paramsNames.indexOfFirst { it == redisParamName }
                    paramsValues.apply {
                        this[index] = arguments[indexOfFirst]
                    }
                }
            }

        return if (usePrefix) RedisKeys.genKey(cacheKey, *paramsValues) else String.format(cacheKey, *paramsValues)
    }

    @After("redisCacheEvict()")
    fun doCacheEvict(joinPoint: JoinPoint) {
        val arguments = joinPoint.args
        val methodSignature = joinPoint.signature as MethodSignature
        val paramsNames = methodSignature.parameterNames
        val annotations = methodSignature.method.getAnnotationsByType(RedisCacheEvict::class.java)
        annotations.forEach { annotation ->
            val cacheKey = cacheKey(arguments, paramsNames, annotation.cacheKey, annotation.paramsNames)
            RedisManager.delete(cacheKey)
        }
    }

    @Around("redisCacheable()")
    fun doCacheable(joinPoint: ProceedingJoinPoint): Any? {
        val arguments = joinPoint.args
        val methodSignature = joinPoint.signature as MethodSignature
        val paramsNames = methodSignature.parameterNames
        val annotation = methodSignature.method.getAnnotation(RedisCacheable::class.java)
        val cacheKey = cacheKey(arguments, paramsNames, annotation.cacheKey, annotation.paramsNames)
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
                annotation.cacheEmpty.doIf {
                    RedisManager.values.set(
                        cacheKey,
                        getCachedEmptyValueByType(javaType),
                        timeout
                    )
                }
            } else {
                RedisManager.values.set(cacheKey, toCachedValueByType(result, javaType) as Any, timeout)
            }
            return result
        }

        return getCachedValueByType(cachedValue, javaType)
    }

    private fun toCachedValueByType(result: Any?, javaType: JavaType): Any? = when {
        javaType.isStringLikeType() -> result
        javaType.isByteType() -> result
        javaType.isShortType() -> result
        javaType.isIntType() -> result
        javaType.isLongType() -> result
        javaType.isFloatType() -> result
        javaType.isDoubleType() -> result
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
        javaType.isFloatType() -> cachedValue?.toFloatOrNull()
        javaType.isDoubleType() -> cachedValue?.toDoubleOrNull()
        javaType.isTypeOrSubTypeOf(BigDecimal::class.java) -> cachedValue?.toBigDecimalOrNull()
        javaType.isTypeOrSubTypeOf(BigInteger::class.java) -> cachedValue?.toBigIntegerOrNull()
        javaType.isBooleanType() -> cachedValue?.toBooleanStrictOrNull()
        else -> cachedValue?.jsonToObj(
            TypeFactory
                .defaultInstance()
                .constructType(javaType)
        )
    }
}
