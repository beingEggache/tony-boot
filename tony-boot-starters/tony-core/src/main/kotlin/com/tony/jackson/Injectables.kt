package com.tony.jackson

import com.fasterxml.jackson.databind.BeanProperty
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.InjectableValues
import com.tony.utils.getLogger

/**
 * Jackson 注入.
 *
 * [com.fasterxml.jackson.annotation.JacksonInject.useInput] 没用.
 *
 * 对于 Kotlin 非空字段, 接受 absent, 不接受 null 值.
 *
 * 比如有个类
 * ```
 * data class Person(
 *   val name: String = "" // 建议给 val 赋默认值,否则 注入会调用两次.
 * )
 * ```
 * {} 是可接受的, { "name": null } 是不可接受的.
 *
 * 对 Kotlin 不 友好, 建议只在 Java  使用, Kotlin 环境下, 建议只在 可变修饰的可空类型的 setter上使用.
 * 比如.
 * ```
 * data class Person(
 *   @set:JacksonInject
 *   var name: String?
 * )
 * ```
 *
 * @author Tang Li
 * @date 2023/07/21 14:23
 */
public class InjectableValuesBySupplier(
    private val values: Map<String, InjectableValueSupplier>,
) : InjectableValues() {

    private val logger = getLogger()

    override fun findInjectableValue(
        valueId: Any,
        ctxt: DeserializationContext,
        property: BeanProperty,
        beanInstance: Any?,
    ): Any? {
        val key = valueId as String
        val ob = values[key]?.value(property, beanInstance)
        if (!values.containsKey(key)) {
            logger.warn("""No injectable id with value '$key' found (for property '${property.name}')""")
        }
        return ob
    }
}

public interface InjectableValueSupplier {
    public val name: String
    public fun value(property: BeanProperty?, instance: Any?): Any
}

public abstract class AbstractInjectableValueSupplier(override val name: String) : InjectableValueSupplier {
    public constructor(type: Class<*>) : this(type.name)
}
