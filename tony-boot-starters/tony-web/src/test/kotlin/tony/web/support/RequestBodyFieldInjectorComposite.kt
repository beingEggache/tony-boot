/*
 * MIT License
 *
 * Copyright (c) 2023-present, tangli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package tony.web.support

import tony.annotation.web.support.InjectRequestBodyField
import tony.utils.annotationFromSelfOrGetterOrSetter
import tony.utils.selfOrGetterOrSetterHasAnnotation
import java.lang.reflect.Field
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * RequestBody 注入
 *
 * @author tangli
 * @date 2023/7/6 14:59
 */
internal class RequestBodyFieldInjectorComposite(
    private val requestBodyFieldInjectors: List<RequestBodyFieldInjector>,
) {
    init {
        val injectorNameSet = HashSet<String>(requestBodyFieldInjectors.size)
        requestBodyFieldInjectors.map { it.name }.forEach {
            check(it !in injectorNameSet) { "RequestBodyFieldInjector Name $it duplicate." }
            injectorNameSet.add(it)
        }
    }

    companion object {
        @JvmStatic
        private val logger: Logger = LoggerFactory.getLogger(RequestBodyFieldInjectorComposite::class.java)

        @get:JvmSynthetic
        internal val supportedClassesCache = ConcurrentHashMap<Class<*>, Boolean>()

        /**
         * <被注入的类, <注入器名, 被注入的类字段列表>>
         */
        @get:JvmSynthetic
        @JvmStatic
        internal val supportedClassFieldsCache =
            ConcurrentHashMap<Class<*>, ConcurrentMap<String, MutableSet<Field>>>()

        @get:JvmSynthetic
        @JvmStatic
        internal val classSupportedInjectorMap =
            ConcurrentHashMap<Class<*>, ConcurrentMap<String, RequestBodyFieldInjector>>()

        @get:JvmSynthetic
        @JvmStatic
        internal val fieldOverrideMap =
            ConcurrentHashMap<Field, Boolean>()
    }

    fun injectValues(body: Any): Any {
        val bodyClass = body::class.java
        val fieldListMap = supportedClassFieldsCache[bodyClass]
        val injectorMap = classSupportedInjectorMap[bodyClass]

        val removedInjectorNames =
            injectorMap
                ?.values
                ?.filter { injector ->
                    val fieldList = fieldListMap?.getValue(injector.name) ?: return@filter false
                    val injectResultList =
                        fieldList.map { field ->
                            field to injector.internalInject(field, body)
                        }
                    injectorHasNoSupportedFields(fieldListMap, injectResultList, fieldList, bodyClass, injector.name)
                }?.map { it.name }
        removeInjectorSupports(bodyClass, removedInjectorNames)
        removeClassSupports(bodyClass)
        return body
    }

    private fun injectorHasNoSupportedFields(
        fieldListMap: ConcurrentMap<String, MutableSet<Field>>,
        injectResultList: List<Pair<Field, Boolean>>,
        fieldList: MutableSet<Field>,
        bodyClass: Class<out Any>,
        injectorName: String,
    ): Boolean {
        synchronized(fieldListMap) {
            injectResultList.forEach { (field, injectResult) ->
                if (!injectResult) {
                    fieldList.remove(field)
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

    private fun removeClassSupports(targetType: Class<*>) {
        val fieldMap = supportedClassFieldsCache[targetType]
        val injectorMap = classSupportedInjectorMap[targetType]

        if (injectorMap?.size == 0 && fieldMap?.size == 0) {
            synchronized(supportedClassesCache) {
                supportedClassesCache[targetType] = false
                logger.warn(
                    "${targetType.simpleName} has no RequestBodyFieldInjector supports"
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
                .filter {
                    it.selfOrGetterOrSetterHasAnnotation(InjectRequestBodyField::class.java)
                }

        if (annotatedFields.isEmpty()) {
            return false
        }

        return supportedClassesCache.getOrPut(targetType) {
            synchronized(supportedClassFieldsCache) {
                processSupportedInjectors(
                    targetType,
                    annotatedFields
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
                }.filterValues { injector ->
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
                field
                    .annotationFromSelfOrGetterOrSetter(InjectRequestBodyField::class.java)!!
                    .value,
                injector.name
            )
        }.onEach {
            supportedClassFieldsCache
                .getOrPut(targetType) { ConcurrentHashMap() }
                .getOrPut(injector.name) { mutableSetOf() }
                .add(it)
        }.isNotEmpty()

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
