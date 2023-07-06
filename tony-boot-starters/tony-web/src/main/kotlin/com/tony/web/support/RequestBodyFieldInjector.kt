package com.tony.web.support

import com.tony.utils.annotation
import com.tony.utils.defaultIfBlank
import com.tony.utils.getLogger
import com.tony.web.support.annotation.InjectRequestBodyField
import org.slf4j.Logger
import java.lang.reflect.Field

/**
 * RequestBodyFieldInjector is
 * @author tangli
 * @since 2023/06/08 10:56
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
            },
        )
    }

    protected open fun inject(annotatedField: Field, body: Any) {
        annotatedField.set(body, value(annotatedField.type))
    }

    internal fun internalInject(annotatedField: Field, body: Any): Boolean {
        annotatedField.trySetAccessible()
        val defaultIfNull = annotatedField.annotation(InjectRequestBodyField::class.java)!!.defaultIfNull
        return try {
            if (defaultIfNull && annotatedField.get(body) != null) {
                true
            } else {
                inject(annotatedField, body)
                true
            }
        } catch (e: IllegalArgumentException) {
            logger.warn(e.message, e)
            false
        }
    }

    override fun toString(): String {
        return this::class.java.simpleName.defaultIfBlank("RequestBodyFieldInjector") + "($name)"
    }
}
