@file:Suppress("RedundantVisibilityModifier")

package com.tony.web.support

import com.tony.annotation.web.support.InjectRequestBodyField
import com.tony.utils.annotationFromSelfOrGetterOrSetter
import com.tony.utils.defaultIfBlank
import com.tony.utils.getLogger
import com.tony.utils.getter
import com.tony.utils.setValueFirstUseSetter
import com.tony.web.support.RequestBodyFieldInjectorComposite.Companion.fieldOverrideMap
import java.lang.reflect.Field
import org.slf4j.Logger

/**
 * RequestBodyFieldInjector is
 * @author Tang Li
 * @date 2023/06/08 10:56
 */
public abstract class RequestBodyFieldInjector(
    public val name: String,
) {

    public abstract fun value(fieldType: Class<*>): Any?

    private val logger: Logger = getLogger()

    init {
        @Suppress("LeakingThis")
        logger.info(
            "Request body field with {} will injected by $this.",
            if (name.isBlank()) {
                "@InjectRequestBodyField"
            } else {
                "@InjectRequestBodyField(value=\"$name\")"
            }
        )
    }

    protected open fun inject(annotatedField: Field, body: Any) {
        annotatedField.setValueFirstUseSetter(body, value(annotatedField.type))
    }

    internal fun internalInject(annotatedField: Field, body: Any): Boolean {
        val override =
            fieldOverrideMap.getOrPut(annotatedField) {
                annotatedField
                    .annotationFromSelfOrGetterOrSetter(InjectRequestBodyField::class.java)!!.override
            }

        return try {
            if (override && annotatedField.getter()?.invoke(body) != null) {
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
