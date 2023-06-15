package com.tony.web.advice

import com.tony.utils.getLogger
import com.tony.utils.typeNameClearBounds
import com.tony.utils.typeParameter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.util.ReflectionUtils
import java.lang.reflect.Field
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

    public abstract val name: String

    @PostConstruct
    private fun init() {
        logger.info(
            "Request body @InjectRequestBodyField(value=\"$name\") " +
                "field:$typeName  will injected by RequestBodyFieldInjector<$typeName>(name=$name).",
        )
    }

    /**
     * 当 [InjectRequestBodyField.value] 为空字符串时判断 [Field.getName]和[RequestBodyFieldInjector.name]是否相等.
     *
     * 否则判断 [InjectRequestBodyField.value]和[RequestBodyFieldInjector.name]是否相等.
     *
     */
    internal fun supports(field: Field): Boolean {
        val annotation = field.getAnnotation(InjectRequestBodyField::class.java) ?: return false
        val fieldGenericTypeName = field.genericType.typeNameClearBounds
        return (annotation.value == name || field.name == name) && fieldGenericTypeName == typeName
    }

    /**
     *  处理 通配符类型名称
     */
    internal val typeName: String = this::class.java.typeParameter().typeNameClearBounds
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

    fun injectValues(body: Any): Any {
        val bodyClass = body::class.java
        val fieldMap = supportedClassFieldsCache.getOrDefault(bodyClass, mapOf())
        val fieldInjectorMap = supportedInjector.getOrDefault(bodyClass, mapOf())
        fieldInjectorMap
            .forEach { (_, injector) ->
                val field = fieldMap.getValue("${injector.name}:${injector.typeName}")
                val value = injector.value()
                ReflectionUtils.makeAccessible(field)
                try {
                    ReflectionUtils.setField(field, body, value)
                } catch (e: IllegalArgumentException) {
                    logger.warn(e.message)
                    return@forEach
                }
                logger
                    .debug(
                        "${injector::class.java.name} " +
                            "inject ${bodyClass.name}'s " +
                            "field ${field.name} value:$value",
                    )
            }
        return body
    }

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

    private fun process(targetType: Class<*>, declaredFields: Array<Field>): Boolean {
        val annotatedFields =
            declaredFields.filter {
                it.isAnnotationPresent(InjectRequestBodyField::class.java)
            }

        val supportsInjectors = supportedInjector.getOrPut(targetType) {
            requestBodyFieldInjectors.associateBy {
                it.name
            }.filterValues { injector ->
                val typeName = injector.typeName
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
                        .putIfAbsent("${injector.name}:$typeName", it)
                }.isNotEmpty()
            }
        }
        val targetTypeSupport = supportsInjectors.isNotEmpty()
        supportedClassesCache[targetType] = targetTypeSupport
        return targetTypeSupport
    }
}
