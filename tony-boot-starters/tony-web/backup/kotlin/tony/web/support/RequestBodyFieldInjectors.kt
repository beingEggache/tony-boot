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

@file:Suppress("unused")

package tony.web.support

import tony.core.annotation.web.support.InjectEmptyIfNull.Companion.DEFAULT_EMPTY
import tony.core.utils.isArrayLikeType
import tony.core.utils.isNumberTypes
import tony.core.utils.isStringLikeType
import tony.core.utils.isTypesOrSubTypesOf
import tony.core.utils.jsonToObj
import java.time.temporal.Temporal
import java.util.Date
import org.slf4j.LoggerFactory

/**
 * RequestBodyFieldInjectors is
 * @author tangli
 * @date 2023/07/06 19:59
 */
internal class IfNullRequestBodyFieldInjector : RequestBodyFieldInjector(DEFAULT_EMPTY) {
    private val logger = LoggerFactory.getLogger(IfNullRequestBodyFieldInjector::class.java)

    override fun value(fieldType: Class<*>): Any? =
        when {
            fieldType.isStringLikeType() -> ""

            fieldType.isArrayLikeType() -> "[]".jsonToObj(fieldType)

            !fieldType.isTypesOrSubTypesOf(
                Enum::class.java,
                Date::class.java,
                Temporal::class.java,
                Boolean::class.java,
                Boolean::class.javaPrimitiveType
            ) &&
                !fieldType.isNumberTypes() &&
                fieldType != Any::class.java -> "{}".jsonToObj(fieldType)

            else -> {
                logger.warn("Do not support ${fieldType.typeName}")
                null
            }
        }
}
