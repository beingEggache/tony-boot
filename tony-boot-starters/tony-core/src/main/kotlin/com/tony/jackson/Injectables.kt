package com.tony.jackson

import com.fasterxml.jackson.annotation.JacksonInject
import com.fasterxml.jackson.annotation.OptBoolean
import com.fasterxml.jackson.databind.BeanProperty
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.InjectableValues
import com.fasterxml.jackson.databind.util.ClassUtil
import com.tony.utils.annotation
import com.tony.utils.asToNotNull
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
 * 对于kotlin, 建议对应注解只加在 字段上, 比如 [@field:JacksonInject("string", useInput = OptBoolean.FALSE)].
 *
 * 加在getter 或 setter上 有一些古怪的行为.
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
        if (valueId !is String) {
            ctxt.reportBadDefinition<Any>(
                ClassUtil.classOf(valueId),
                String.format(
                    "Unrecognized inject value id type (%s), expecting String",
                    ClassUtil.classNameOf(valueId)
                )
            )
        }
        val key = valueId as String
        val member = forProperty.member.member.asToNotNull<AnnotatedElement>()
        val originValue = if (beanInstance != null) forProperty.member?.getValue(beanInstance) else null

        val ob = values[key]?.value(member, beanInstance, originValue)
        if (ob == null && !values.containsKey(key)) {
            throw IllegalArgumentException(
                """No injectable id with value '$key' found (for property '${forProperty.name}')"""
            )
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
