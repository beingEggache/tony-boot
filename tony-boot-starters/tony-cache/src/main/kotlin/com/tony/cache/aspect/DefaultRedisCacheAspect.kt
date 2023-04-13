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
public class DefaultRedisCacheAspect {

    @Pointcut("@annotation($PROJECT_GROUP.cache.annotation.RedisCacheEvict.Container)")
    public fun redisCacheEvict(): Unit = Unit

    @Pointcut("@annotation($PROJECT_GROUP.cache.annotation.RedisCacheable)")
    public fun redisCacheable(): Unit = Unit

    /**
     * 生成实际的缓存键名
     * @param arguments 方法实际参数
     * @param paramsNames 方法参数名数组
     * @param cacheKey 缓存键名或缓存键模板
     * @param annoParamsNames 注解上对应的参数名,可用SpEl表达式
     */
    private fun cacheKey(
        arguments: Array<Any>,
        paramsNames: Array<String>,
        annoParamsNames: Array<String>,
        cacheKey: String,
    ): String {
        if (paramsNames.isEmpty() ||
            annoParamsNames.isEmpty()
        ) {
            return RedisKeys.genKey(cacheKey)
        }
        val paramsValues = paramsValues(arguments, paramsNames, annoParamsNames)
        return RedisKeys.genKey(cacheKey, *paramsValues)
    }

    /**
     * 根据注解信息及方法信息, 生成实际参数的字符串表示供生成缓存键
     *
     * @param annoParamsNames 注解上对应的参数名,可用SpEl表达式
     * @param paramsNames 方法参数名数组
     * @param arguments 方法实际参数
     */
    private fun paramsValues(
        arguments: Array<Any>,
        paramsNames: Array<String>,
        annoParamsNames: Array<String>,
    ): Array<Any?> =
        annoParamsNames.foldIndexed(
            Array(annoParamsNames.size) {},
        ) { index, paramsValues, annoParamName ->
            if (annoParamName.startsWith("#")) {
                val indexOfDot = annoParamName.indexOf(".")
                val indexOfFirst = paramsNames.indexOfFirst { it == annoParamName.substring(2, indexOfDot) }
                paramsValues.apply {
                    this[index] = SpelExpressionParser()
                        .parseExpression(annoParamName.substring(indexOfDot + 1, annoParamName.indexOf("}")))
                        .getValue(StandardEvaluationContext(arguments[indexOfFirst]))
                }
            } else {
                val indexOfFirst = paramsNames.indexOfFirst { it == annoParamName }
                paramsValues.apply {
                    this[index] = arguments[indexOfFirst]
                }
            }
        }

    @After("redisCacheEvict()")
    public fun doCacheEvict(joinPoint: JoinPoint) {
        val arguments = joinPoint.args
        val methodSignature = joinPoint.signature as MethodSignature
        val paramsNames = methodSignature.parameterNames
        val annotations = methodSignature.method.getAnnotationsByType(RedisCacheEvict::class.java)
        annotations.forEach { annotation ->
            val cacheKey = cacheKey(arguments, paramsNames, annotation.paramsNames, annotation.cacheKey)
            RedisManager.delete(cacheKey)
        }
    }

    @Around("redisCacheable()")
    public fun doCacheable(joinPoint: ProceedingJoinPoint): Any? {
        val arguments = joinPoint.args
        val methodSignature = joinPoint.signature as MethodSignature
        val paramsNames = methodSignature.parameterNames
        val annotation = methodSignature.method.getAnnotation(RedisCacheable::class.java)
        val cacheKey = cacheKey(arguments, paramsNames, annotation.paramsNames, annotation.cacheKey)
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
