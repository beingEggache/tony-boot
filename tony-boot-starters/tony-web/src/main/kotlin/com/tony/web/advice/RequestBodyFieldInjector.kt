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
public abstract class RequestBodyFieldInjector<T>(
    public open val name: String
) {

    private val logger: Logger = getLogger()

    public abstract fun value(): T

    internal var supports: Boolean? = null

    @PostConstruct
    private fun init() {
        val typeName = this::class.java.typeParameter().typeNameClearBounds
        logger.info(
            "Request body field with @InjectRequestBodyField(value=\"$name\") " +
                "will injected by RequestBodyFieldInjector<$typeName>(name=$name)(${this::class.java.simpleName}).",
        )
    }

    /**
     * 当 [InjectRequestBodyField.value] 为空字符串时判断 [Field.getName]和[RequestBodyFieldInjector.name]是否相等.
     *
     * 否则判断 [InjectRequestBodyField.value]和[RequestBodyFieldInjector.name]是否相等.
     *
     */
    internal fun supports(field: Field): Boolean {
        if (supports != null) {
            return supports ?: false
        }

        val annotation = field.getAnnotation(InjectRequestBodyField::class.java)
        if (annotation == null) {
            supports = false
            return false
        }

        return run {
            supports = (annotation.value == name || field.name == name)
            (annotation.value == name || field.name == name)
        }
    }
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
                val field = fieldMap.getValue(injector.name)
                injectAndProcess(injector, body, field)
            }
        return body
    }

    private fun injectAndProcess(injector: RequestBodyFieldInjector<*>, body: Any, field: Field): Boolean {
        ReflectionUtils.makeAccessible(field)
        val value = injector.value()
        try {
            ReflectionUtils.setField(field, body, value)
        } catch (e: IllegalArgumentException) {
            logger.warn(e.message)
            injector.supports = false
            return false
        }
        logger
            .debug(
                "${injector::class.java.name} " +
                    "inject ${body::class.java.name}'s " +
                    "field ${field.name} value:$value",
            )
        return true
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
                annotatedFields.filter { field ->
                    injector.supports(field)
                }.onEach {
                    supportedClassFieldsCache
                        .getOrPut(targetType) { mutableMapOf() }
                        .putIfAbsent(injector.name, it)
                }.isNotEmpty()
            }
        }
        val targetTypeSupport = supportsInjectors.isNotEmpty()
        supportedClassesCache[targetType] = targetTypeSupport
        return targetTypeSupport
    }
}
