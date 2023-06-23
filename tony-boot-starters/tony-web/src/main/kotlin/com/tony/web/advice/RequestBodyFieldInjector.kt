package com.tony.web.advice

import com.tony.utils.defaultIfBlank
import com.tony.utils.getLogger
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.util.ReflectionUtils
import java.lang.reflect.Field
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.function.Supplier

/**
 * RequestBodyFieldInjector is
 * @author tangli
 * @since 2023/06/08 10:56
 */
public open class RequestBodyFieldInjector(
    public val name: String,
    internal val value: Supplier<*>,
) {

    private val logger: Logger = getLogger()

    init {
        @Suppress("LeakingThis")
        logger.info(
            "Request body field with {} will injected by $this.",
            if (name.isBlank()) {
                "@InjectRequestBodyField"
            } else {
                "@InjectRequestBodyField(value=\"$name\")"
            },
        )
    }

    protected open fun inject(annotatedField: Field, body: Any) {
        ReflectionUtils.setField(annotatedField, body, value.get())
    }

    internal fun internalInject(annotatedField: Field, body: Any): Boolean {
        ReflectionUtils.makeAccessible(annotatedField)
        return try {
            inject(annotatedField, body)
            true
        } catch (e: IllegalArgumentException) {
            logger.warn(e.message)
            false
        }
    }

    override fun toString(): String {
        return this::class.java.simpleName.defaultIfBlank("RequestBodyFieldInjector") + "($name)"
    }
}

internal class RequestBodyFieldInjectorComposite(
    private val requestBodyFieldInjectors: List<RequestBodyFieldInjector>,
) {
    companion object {
        @JvmStatic
        private val logger: Logger = LoggerFactory.getLogger(RequestBodyFieldInjectorComposite::class.java)

        @JvmStatic
        private val supportedClassesCache = ConcurrentHashMap<Class<*>, Boolean>()

        /**
         * <被注入的类, <注入器名, 被注入的类字段列表>>
         */
        @JvmStatic
        private val supportedClassFieldsCache =
            ConcurrentHashMap<Class<*>, ConcurrentMap<String, MutableList<Field>>>()

        @JvmStatic
        private val supportedInjector =
            ConcurrentHashMap<Class<*>, ConcurrentMap<String, RequestBodyFieldInjector>>()
    }

    fun injectValues(body: Any): Any {
        val bodyClass = body::class.java
        val fieldListMap = supportedClassFieldsCache[bodyClass]
        val injectorMap = supportedInjector[bodyClass]

        val removedInjectorNames = injectorMap
            ?.values
            ?.filter { injector ->
                val fieldList = fieldListMap?.getValue(injector.name) ?: return@filter false
                val injectResultList = fieldList.map { field ->
                    field to injector.internalInject(field, body)
                }
                synchronized(fieldListMap) {
                    injectResultList.forEachIndexed { index, (field, injectResult) ->
                        if (!injectResult) {
                            fieldList.removeAt(index)
                            logger.warn("${bodyClass.simpleName}.${field.name} inject failed, remove supports.")
                        }
                    }
                    if (fieldList.isEmpty()) {
                        logger.warn("${bodyClass.simpleName} inject supports field is empty.")
                        fieldListMap.remove(injector.name)
                    }
                }
                fieldList.isEmpty()
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
                    "${targetType.simpleName} has no RequestBodyFieldInjector supports",
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
        val fieldMap = supportedClassFieldsCache[targetType]
        val injectorMap = supportedInjector[targetType]

        synchronized(supportedInjector) {
            injectorNameList.forEach {
                logger.warn("${targetType.simpleName} remove ${injectorMap?.remove(it)} supports")
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

        return supportedClassesCache[targetType] ?: synchronized(supportedClassesCache) {
            getSupportsAndProcessCache(targetType, annotatedFields)
        }
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
                            /*
                             * 当 [InjectRequestBodyField.value] 为空字符串时判断 [Field.getName]和[RequestBodyFieldInjector.name]是否相等.
                             *
                             * 否则判断 [InjectRequestBodyField.value]和[RequestBodyFieldInjector.name]是否相等.
                             *
                             */
                            val annotation = field.getAnnotation(InjectRequestBodyField::class.java)
                            if (annotation.value.isNotBlank()) {
                                annotation.value == injector.name
                            } else {
                                field.name == injector.name
                            }
                        }
                        .apply {
                            synchronized(supportedClassFieldsCache) {
                                onEach {
                                    supportedClassFieldsCache
                                        .getOrPut(targetType) { ConcurrentHashMap() }
                                        .getOrPut(injector.name) { mutableListOf() }
                                        .add(it)
                                }
                            }
                        }
                        .isNotEmpty()
                }.let {
                    ConcurrentHashMap<String, RequestBodyFieldInjector>().apply {
                        putAll(it)
                    }
                }
        }
        val targetTypeSupport = supportsInjectors.isNotEmpty()
        supportedClassesCache[targetType] = targetTypeSupport
        return targetTypeSupport
    }
}
