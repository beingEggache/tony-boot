@file:Suppress("unused")

package com.tony.cache.aspect

import com.fasterxml.jackson.databind.type.TypeFactory
import com.tony.PROJECT_GROUP
import com.tony.cache.RedisKeys
import com.tony.cache.RedisManager
import com.tony.cache.annotation.RedisCacheEvict
import com.tony.cache.annotation.RedisCacheable
import com.tony.utils.doIf
import com.tony.utils.jsonToObj
import com.tony.utils.secondOfTodayRest
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.After
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext

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
        redisParamsNames: Array<String>
    ): String {

        if (paramsNames.isEmpty() ||
            redisParamsNames.isEmpty()
        ) {
            return RedisKeys.genKey(cacheKey)
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

        return RedisKeys.genKey(cacheKey, *paramsValues)
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
        if ("{}" == cachedValue) return methodSignature
            .returnType
            .getDeclaredConstructor()
            .newInstance()
        if (cachedValue == null) {
            val result = joinPoint.proceed()
            if (result == null) {
                annotation.cacheEmpty.doIf { RedisManager.values.set(cacheKey, "{}", timeout) }
            } else {
                RedisManager.values.set(cacheKey, result, timeout)
            }
            return result
        }
        return cachedValue.jsonToObj(
            TypeFactory
                .defaultInstance()
                .constructType(methodSignature.method.genericReturnType)
        )
    }
}
