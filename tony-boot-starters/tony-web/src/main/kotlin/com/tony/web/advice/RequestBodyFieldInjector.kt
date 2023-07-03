package com.tony.web.advice

import com.tony.utils.defaultIfBlank
import com.tony.utils.getLogger
import org.slf4j.Logger
import org.slf4j.LoggerFactory
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
        annotatedField.set(body, value.get())
    }

    internal fun internalInject(annotatedField: Field, body: Any): Boolean {
        annotatedField.trySetAccessible()
        val defaultIfNull = annotatedField.getAnnotation(InjectRequestBodyField::class.java).defaultIfNull
        return try {
            if (defaultIfNull && annotatedField.get(body) != null) {
                true
            } else {
                inject(annotatedField, body)
                true
            }
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
        private val classSupportedInjectorMap =
            ConcurrentHashMap<Class<*>, ConcurrentMap<String, RequestBodyFieldInjector>>()
    }

    fun injectValues(body: Any): Any {
        val bodyClass = body::class.java
        val fieldListMap = supportedClassFieldsCache[bodyClass]
        val injectorMap = classSupportedInjectorMap[bodyClass]

        val removedInjectorNames = injectorMap
            ?.values
            ?.filter { injector ->
                val fieldList = fieldListMap?.getValue(injector.name) ?: return@filter false
                val injectResultList = fieldList.map { field ->
                    field to injector.internalInject(field, body)
                }
                injectorHasNoSupportedFields(fieldListMap, injectResultList, fieldList, bodyClass, injector.name)
            }
            ?.map { it.name }
        removeInjectorSupports(bodyClass, removedInjectorNames)
        removeClassSupports(bodyClass)
        return body
    }

    private fun injectorHasNoSupportedFields(
        fieldListMap: ConcurrentMap<String, MutableList<Field>>,
        injectResultList: List<Pair<Field, Boolean>>,
        fieldList: MutableList<Field>,
        bodyClass: Class<out Any>,
        injectorName: String,
    ): Boolean {
        synchronized(fieldListMap) {
            injectResultList.forEachIndexed { index, (field, injectResult) ->
                if (!injectResult) {
                    fieldList.removeAt(index)
                    logger.warn("${bodyClass.simpleName}.${field.name} inject failed, remove supports.")
                }
            }
            if (fieldList.isEmpty()) {
                logger.warn("${bodyClass.simpleName} inject supports field is empty.")
                fieldListMap.remove(injectorName)
            }
        }
        return fieldList.isEmpty()
    }

    private fun removeClassSupports(
        targetType: Class<*>,
    ) {
        val fieldMap = supportedClassFieldsCache[targetType]
        val injectorMap = classSupportedInjectorMap[targetType]

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
        val injectorMap = classSupportedInjectorMap[targetType]

        synchronized(classSupportedInjectorMap) {
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
        val targetTypeSupport = supportedClassesCache[targetType]

        if (targetTypeSupport != null) {
            return targetTypeSupport
        }

        val annotatedFields =
            targetType
                .declaredFields
                .filter { it.isAnnotationPresent(InjectRequestBodyField::class.java) }

        if (annotatedFields.isEmpty()) {
            return false
        }

        return supportedClassesCache.getOrPut(targetType) {
            synchronized(supportedClassFieldsCache) {
                processSupportedInjectors(
                    targetType,
                    annotatedFields,
                ).isNotEmpty()
            }
        }
    }

    private fun processSupportedInjectors(
        targetType: Class<*>,
        annotatedFields: List<Field>,
    ): ConcurrentMap<String, RequestBodyFieldInjector> =
        classSupportedInjectorMap.getOrPut(targetType) {
            requestBodyFieldInjectors
                .associateBy {
                    it.name
                }
                .filterValues { injector ->
                    hasSupportInjectorFields(annotatedFields, injector, targetType)
                }.let {
                    ConcurrentHashMap<String, RequestBodyFieldInjector>().apply {
                        putAll(it)
                    }
                }
        }

    private fun hasSupportInjectorFields(
        annotatedFields: List<Field>,
        injector: RequestBodyFieldInjector,
        targetType: Class<*>,
    ) = annotatedFields
        .filter { field ->
            isFieldSupportByAnnoValueOrFieldName(
                field.name,
                field.getAnnotation(InjectRequestBodyField::class.java).value,
                injector.name,
            )
        }
        .onEach {
            supportedClassFieldsCache
                .getOrPut(targetType) { ConcurrentHashMap() }
                .getOrPut(injector.name) { mutableListOf() }
                .add(it)
        }
        .isNotEmpty()

    /**
     * 根据注解值或字段名 判断字段是否支持注入
     * @param fieldName 将被注入的字段名
     * @param annotationValue 注解上的值
     * @param injectorName 注入器的名
     */
    private fun isFieldSupportByAnnoValueOrFieldName(
        fieldName: String,
        annotationValue: String,
        injectorName: String,
    ): Boolean =
        if (annotationValue.isNotBlank()) {
            annotationValue == injectorName
        } else {
            fieldName == injectorName
        }
}
