package com.tony.web.advice

import com.tony.utils.getLogger
import com.tony.utils.typeParameter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.util.ReflectionUtils
import java.lang.reflect.Field
import java.lang.reflect.Type
import java.util.concurrent.ConcurrentHashMap
import javax.annotation.PostConstruct

/**
 * RequestBodyFieldInjector is
 * @author tangli
 * @since 2023/06/08 10:56
 */
public abstract class RequestBodyFieldInjector<T> {

    private val logger: Logger = getLogger()

    public abstract fun value(): T

    public abstract val fieldName: String

    @PostConstruct
    private fun init() {
        logger.info("Request body will inject the $fieldName ${type.typeName} value")
    }

    internal fun supports(field: Field): Boolean =
        field.name == fieldName &&
            field.genericType.equals(type)

    internal val type: Type = this::class.java.typeParameter()
}

internal class RequestBodyFieldInjectorComposite(
    private val requestBodyFieldInjectors: List<RequestBodyFieldInjector<*>>,
) {
    private val logger: Logger = LoggerFactory.getLogger(RequestBodyFieldInjectorComposite::class.java)

    private val supportedClassesCache = ConcurrentHashMap<Class<*>, Boolean>()

    private val supportedClassFieldsCache: ConcurrentHashMap<Class<*>, MutableMap<String, Field>> =
        ConcurrentHashMap<Class<*>, MutableMap<String, Field>>()

    private val supportedInjector: ConcurrentHashMap<Class<*>, Map<String, RequestBodyFieldInjector<*>>> =
        ConcurrentHashMap<Class<*>, Map<String, RequestBodyFieldInjector<*>>>()

    fun supports(targetType: Class<*>?): Boolean {
        if (targetType == null) return false

        val declaredFields = targetType.declaredFields
        val typeHasNoInjectField = declaredFields.none { it.isAnnotationPresent(InjectRequestBodyField::class.java) }
        if (typeHasNoInjectField) {
            return false
        }

        val supportFromCache = supportedClassesCache[targetType]
        if (supportFromCache != null) {
            return supportFromCache
        }

        return process(targetType, declaredFields)
    }

    fun injectValues(body: Any): Any {
        val bodyClass = body::class.java
        val fieldMap = supportedClassFieldsCache.getOrDefault(bodyClass, mapOf())
        supportedInjector
            .getOrDefault(bodyClass, mapOf())
            .forEach { (_, injector) ->
                val field = fieldMap.getValue("${injector.fieldName}:${injector.type.typeName}")
                val value = injector.value()
                ReflectionUtils.makeAccessible(field)
                ReflectionUtils.setField(field, body, value)
                logger
                    .debug(
                        "${injector::class.java.name} " +
                            "inject ${bodyClass.name}'s " +
                            "field ${field.name} value:$value",
                    )
            }
        return body
    }

    private fun process(targetType: Class<*>, declaredFields: Array<Field>): Boolean {
        val annotatedFields =
            declaredFields.filter {
                it.isAnnotationPresent(InjectRequestBodyField::class.java)
            }

        val supportsInjectors = supportedInjector.getOrPut(targetType) {
            requestBodyFieldInjectors.associateBy {
                it.fieldName
            }.filterValues { injector ->
                val typeName = injector.type.typeName
                annotatedFields.filter { field ->
                    val supports = injector.supports(field)
                    if (!supports) {
                        val fieldName = field.name
                        logger.debug(
                            "${targetType.name} field $fieldName:$typeName does not equal $fieldName:$typeName",
                        )
                    }
                    supports
                }.onEach {
                    supportedClassFieldsCache
                        .getOrPut(targetType) { mutableMapOf() }
                        .putIfAbsent("${injector.fieldName}:$typeName", it)
                }.isNotEmpty()
            }
        }
        val targetTypeSupport = supportsInjectors.isNotEmpty()
        supportedClassesCache[targetType] = targetTypeSupport
        return targetTypeSupport
    }
}
