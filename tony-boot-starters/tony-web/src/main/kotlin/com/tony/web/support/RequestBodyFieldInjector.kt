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
