package com.tony.web.advice

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.core.MethodParameter
import org.springframework.http.HttpInputMessage
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.util.CollectionUtils
import org.springframework.util.ReflectionUtils
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.concurrent.ConcurrentHashMap

/**
 * RequestBodyFieldInjectAdvice is
 * @author tangli
 * @since 2023/06/07 18:07
 */
@ConditionalOnExpression("\${web.inject-request-body-enabled:true}")
@RestControllerAdvice
public class InjectRequestBodyAdvice : RequestBodyAdviceAdapter() {

    private val logger: Logger = LoggerFactory.getLogger(InjectRequestBodyAdvice::class.java)


    init {
        logger.info("Request body inject is enabled.")
        logger
            .info(
                "Request body will inject fields value which named in {} with @InjectRequestBodyField annotation.",
                getFieldNames().joinToString(),
            )
    }

    override fun supports(
        methodParameter: MethodParameter,
        targetType: Type,
        converterType: Class<out HttpMessageConverter<*>>,
    ): Boolean {
        return supportsInject(getTypeFromTarget(targetType) ?: return false)
    }

    override fun afterBodyRead(
        body: Any,
        inputMessage: HttpInputMessage,
        parameter: MethodParameter,
        targetType: Type,
        converterType: Class<out HttpMessageConverter<*>>,
    ): Any {
        val fields = getFieldsFromType(getTypeFromTarget(targetType))
        if (fields.isEmpty()) {
            return body
        }
        val injectValueMap: Map<String, () -> Any?> = getInjectValueMap()
        fields.forEach {
            val fieldName = it.name
            val injectValue = injectValueMap[fieldName]?.invoke()
            val fieldType = it.type
            if (fieldType == injectValue?.javaClass) {
                ReflectionUtils.makeAccessible(it)
                ReflectionUtils.setField(it, body, injectValue)
            } else {
                logger.warn(
                    "Value class({}) not equal to field({}) class({}).",
                    injectValue?.javaClass,
                    fieldName,
                    fieldType,
                )
            }
        }
        return body
    }

    private fun supportsInject(targetType: Class<*>): Boolean {
        if (REQ_CLASS_SUPPORT_CACHE[targetType] != null) {
            return REQ_CLASS_SUPPORT_CACHE[targetType] ?: false
        }
        val fields = getFieldsFromType(targetType)
        if (CollectionUtils.isEmpty(fields)) {
            REQ_CLASS_SUPPORT_CACHE[targetType] = false
            return false
        }
        val hasInjectValue =
            fields
                .any {
                    it.getAnnotation(
                        InjectRequestBodyField::class.java,
                    ) != null
                }
        REQ_CLASS_SUPPORT_CACHE[targetType] = hasInjectValue
        return hasInjectValue
    }

    private fun getFieldsFromType(type: Class<*>?) =
        REQ_CLASS_FIELDS_CACHE
            .getOrPut(type) {
                getFieldNames()
                    .mapNotNull {
                        if (type == null) {
                            null
                        } else {
                            ReflectionUtils.findField(type, it)
                        }
                    }
                    .filter {
                        it.getAnnotation(InjectRequestBodyField::class.java) != null
                    }.ifEmpty {
                        emptyList()
                    }
            }

    /**
     * 获得注入的值对象
     *
     * @return 值对象
     */
    protected fun getInjectValueMap(): Map<String, () -> Any?> {
        return mapOf(
            "string" to { },
            "int" to { },
            "list" to { },
            "map" to { },
            "objList" to { },
            "objMap" to { },
        )
    }

    /**
     * 字段名
     *
     * @return 字段名
     */
    private fun getFieldNames(): Set<String> {
        return getInjectValueMap().keys
    }

    private fun getTypeFromTarget(targetType: Type): Class<*>? {
        return if (targetType is Class<*>) {
            targetType
        } else {
            if (targetType is ParameterizedType) {
                targetType.rawType
            } else {
                logger.warn("targetType is {}", targetType.typeName)
                null
            } as Class<*>?
        }
    }

    private companion object {
        @JvmStatic
        private val REQ_CLASS_SUPPORT_CACHE = ConcurrentHashMap<Class<*>, Boolean>()

        @JvmStatic
        private val REQ_CLASS_FIELDS_CACHE: ConcurrentHashMap<Class<*>, List<Field>> =
            ConcurrentHashMap<Class<*>, List<Field>>()

        @JvmStatic
        private var logger: Logger = LoggerFactory.getLogger(InjectRequestBodyAdvice::class.java)
    }
}
