package com.tony.jackson

import com.fasterxml.jackson.annotation.JacksonInject
import com.fasterxml.jackson.annotation.OptBoolean
import com.fasterxml.jackson.databind.BeanProperty
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.InjectableValues
import com.tony.utils.annotation
import com.tony.utils.asToNotNull
import com.tony.utils.valueOf
import java.lang.reflect.AnnotatedElement

/**
 * Jackson 注入.
 *
 * 对于 Kotlin 非空字段, 接受 absent, 不接受 null 值.
 * 比如有个类
 * ```
 * data class Person(
 *   val name: String = "" // 建议给 val 赋默认值,否则 注入会调用两次.
 * )
 * ```
 * {} 是可接受的, { "name": null } 是不可接受的.
 *
 * 对于kotlin 不可变字段, 建议对应注解只加在 字段上, 比如 [@field:JacksonInject("string", useInput = OptBoolean.FALSE)].
 *
 * 加在getter 或 setter上 有一些古怪的行为.
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
 * @author tangli
 * @since 2023/07/21 14:23
 */
public class InjectableValuesBySupplier(
    private val values: Map<String, InjectableValueSupplier>,
) : InjectableValues() {

    override fun findInjectableValue(
        valueId: Any,
        ctxt: DeserializationContext,
        forProperty: BeanProperty,
        beanInstance: Any?,
    ): Any? {
        val key = valueId as String
        val member = forProperty.member.member.asToNotNull<AnnotatedElement>()
        println("forProperty.fullName:" + forProperty.fullName)
        val originValue = member.valueOf(beanInstance)

        val ob = values[key]?.value(member, beanInstance, originValue)
        require(!(ob == null && !values.containsKey(key))) {
            """No injectable id with value '$key' found (for property '${forProperty.name}')"""
        }
        val useInput = member.annotation(JacksonInject::class.java)?.useInput
        if (useInput == OptBoolean.TRUE || useInput == OptBoolean.DEFAULT) {
            return originValue ?: ob
        }
        return ob
    }
}

public interface InjectableValueSupplier {
    public val name: String
    public fun value(e: AnnotatedElement?, instance: Any?, originValue: Any?): Any?
}

public abstract class AbstractInjectableValueSupplier(override val name: String) : InjectableValueSupplier {
    public constructor(type: Class<*>) : this(type.name)
}
