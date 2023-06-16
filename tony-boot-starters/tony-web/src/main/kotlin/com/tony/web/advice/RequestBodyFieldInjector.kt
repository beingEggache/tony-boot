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
    public open val name: String,
) {

    private val logger: Logger = getLogger()

    public abstract fun value(): T

    private val lock = Any()

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
        val annotation = field.getAnnotation(InjectRequestBodyField::class.java) ?: return false
        return (annotation.value == name || field.name == name)
    }
}

internal class RequestBodyFieldInjectorComposite(
    private val requestBodyFieldInjectors: List<RequestBodyFieldInjector<*>>,
) {
    private val logger: Logger = LoggerFactory.getLogger(RequestBodyFieldInjectorComposite::class.java)

    private val supportedClassesCache = ConcurrentHashMap<Class<*>, Boolean>()

    private val supportedClassFieldsCache: ConcurrentHashMap<Class<*>, MutableMap<String, Field>> =
        ConcurrentHashMap<Class<*>, MutableMap<String, Field>>()

    private val supportedInjector: ConcurrentHashMap<Class<*>, MutableMap<String, RequestBodyFieldInjector<*>>> =
        ConcurrentHashMap<Class<*>, MutableMap<String, RequestBodyFieldInjector<*>>>()

    fun injectValues(body: Any): Any {
        val bodyClass = body::class.java
        val fieldMap = supportedClassFieldsCache[bodyClass]
        val injectorMap = supportedInjector[bodyClass]

        val removedInjectorNames = injectorMap
            ?.values
            ?.filter { injector ->
                val injectorName = injector.name
                val field = fieldMap?.getValue(injectorName) ?: return@filter false
                val value = injector.value()
                ReflectionUtils.makeAccessible(field)
                try {
                    ReflectionUtils.setField(field, body, value)
                    false
                } catch (e: IllegalArgumentException) {
                    logger.warn(e.message)
                    true
                }
            }
            ?.map { it.name }
        removeInjectorSupports(bodyClass, removedInjectorNames)
        removeClassSupports(bodyClass)
        return body
    }

    private fun removeClassSupports(
        targetType: Class<*>,
    ) {
        val fieldMap = supportedClassFieldsCache[targetType]
        val injectorMap = supportedInjector[targetType]

        if (injectorMap?.size == 0 && fieldMap?.size == 0) {
            synchronized(supportedClassesCache) {
                supportedClassesCache[targetType] = false
                logger.warn(
                    "${targetType.simpleName} remove RequestBodyFieldInjector supports",
                )
            }
        }
    }

    private fun removeInjectorSupports(
        targetType: Class<*>,
        injectorNameList: List<String>?,
    ) {
        if (injectorNameList.isNullOrEmpty()) {
            return
        }
        logger.warn(
            "RequestBodyFieldInjector(${injectorNameList.joinToString()}) " +
                "removed from ${targetType.simpleName}",
        )

        val fieldMap = supportedClassFieldsCache[targetType]
        val injectorMap = supportedInjector[targetType]

        synchronized(supportedInjector) {
            injectorNameList.forEach {
                injectorMap?.remove(it)
            }
        }
        synchronized(supportedClassFieldsCache) {
            injectorNameList.forEach {
                fieldMap?.remove(it)
            }
        }
    }

    fun supports(targetType: Class<*>): Boolean {
        val annotatedFields =
            targetType
                .declaredFields
                .filter { it.isAnnotationPresent(InjectRequestBodyField::class.java) }

        if (annotatedFields.isEmpty()) {
            return false
        }

        return supportedClassesCache[targetType] ?: getSupportsAndProcessCache(targetType, annotatedFields)
    }

    private fun getSupportsAndProcessCache(targetType: Class<*>, annotatedFields: List<Field>): Boolean {
        val supportsInjectors = supportedInjector.getOrPut(targetType) {
            requestBodyFieldInjectors
                .associateBy {
                    it.name
                }
                .filterValues { injector ->
                    annotatedFields
                        .filter { field ->
                            injector.supports(field)
                        }
                        .onEach {
                            supportedClassFieldsCache
                                .getOrPut(targetType) { mutableMapOf() }
                                .putIfAbsent(injector.name, it)
                        }
                        .isNotEmpty()
                }.toMutableMap()
        }
        val targetTypeSupport = supportsInjectors.isNotEmpty()
        supportedClassesCache[targetType] = targetTypeSupport
        return targetTypeSupport
    }
}
