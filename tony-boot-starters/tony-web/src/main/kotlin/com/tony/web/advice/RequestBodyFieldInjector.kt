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

    /**
     * 执行过一次注入后判断是否匹配
     */
    internal var supportsWhenAfterInject: Boolean? = null
        set(value) {
            synchronized(lock) {
                field = value
            }
        }

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
        if (supportsWhenAfterInject != null) {
            return supportsWhenAfterInject ?: false
        }
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
        val fieldMap = supportedClassFieldsCache.getOrDefault(bodyClass, mapOf())
        val fieldInjectorMap = supportedInjector.getOrDefault(bodyClass, mapOf())

        val injectResult = fieldInjectorMap
            .map { (_, injector) ->
                val field = fieldMap.getValue(injector.name)
                injectAndReGetSupports(injector, body, field)
            }
        if (injectResult.any { false }) {
            logger.info("I'm here")
            supportedClassesCache.remove(bodyClass)
        }
        return body
    }

    /**
     * 执行注入 , 如果注入失败则标记注入不支持此字段类型.
     * @param injector
     * @param body
     * @param field
     * @return
     */
    private fun injectAndReGetSupports(injector: RequestBodyFieldInjector<*>, body: Any, field: Field): Boolean {
        ReflectionUtils.makeAccessible(field)
        val value = injector.value()
        try {
            ReflectionUtils.setField(field, body, value)
            injector.supportsWhenAfterInject = true
        } catch (e: IllegalArgumentException) {
            logger.warn(e.message)
            injector.supportsWhenAfterInject = false
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

    fun supports(targetType: Class<*>): Boolean {
        val annotatedFields =
            targetType
                .declaredFields
                .filter { it.isAnnotationPresent(InjectRequestBodyField::class.java) }

        if (annotatedFields.isEmpty()) {
            return false
        }

        return supportedClassesCache[targetType] ?: process(targetType, annotatedFields)
    }

    private fun process(targetType: Class<*>, annotatedFields: List<Field>): Boolean {
        val supportsInjectors = supportedInjector.getOrPut(targetType) {
            requestBodyFieldInjectors
                .associateBy {
                    it.name
                }
                .filterValues { injector ->
                    annotatedFields
                        .filter { field ->
                            injector.supportsWhenAfterInject ?: injector.supports(field)
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
