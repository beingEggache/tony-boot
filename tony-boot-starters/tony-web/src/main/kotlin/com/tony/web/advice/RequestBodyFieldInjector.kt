package com.tony.web.advice

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.concurrent.ConcurrentHashMap

/**
 * RequestBodyFieldInjector is
 * @author tangli
 * @since 2023/06/08 10:56
 */
@Suppress("unused")
public abstract class RequestBodyFieldInjector<T> {

    private val logger: Logger = LoggerFactory.getLogger(RequestBodyFieldInjector::class.java)

    public abstract fun inject(body: Any, field: Field)
    internal fun supports(targetType: Class<*>, field: Field): Boolean {
        val name = field.name
        val fieldGenericType = field.genericType
        val support = name == fieldName &&
            fieldGenericType.equals(type)
        if (!support) {
            logger.warn(
                "${targetType.name} field ${name}:${fieldGenericType.typeName} " +
                    "does not equal ${fieldName}:${type.typeName}"
            )
        }
        return support
    }


    public abstract val fieldName: String

    private val type: Type
        get() {
            val superClass = javaClass.genericSuperclass
            require(superClass !is Class<*>) {  // sanity check, should never happen
                "Internal error: RequestBodyFieldInjector constructed without actual type information"
            }
            return (superClass as ParameterizedType).actualTypeArguments[0]
        }
}

internal class RequestBodyFieldInjectorComposite(
    private val requestBodyFieldInjectors: List<RequestBodyFieldInjector<*>>
) {
    private val supportedClassesCache = ConcurrentHashMap<Class<*>, Boolean>()

    private val supportedClassFieldsCache: ConcurrentHashMap<Class<*>, List<Field>> =
        ConcurrentHashMap<Class<*>, List<Field>>()

    public fun supports(targetType: Class<*>): Boolean {
        if (!targetType.isAnnotationPresent(InjectRequestBody::class.java)) {
            return false
        }
        val annotatedFields =
            targetType.declaredFields.filter { it.annotatedType.isAnnotationPresent(InjectRequestBodyField::class.java) }
        return true
    }
}
