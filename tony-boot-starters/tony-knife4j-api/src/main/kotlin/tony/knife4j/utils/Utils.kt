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

@file:JvmName("-SwaggerUtils")

package tony.knife4j.utils

import java.io.InputStream
import java.lang.reflect.Type
import org.springframework.core.ResolvableType
import org.springframework.core.io.InputStreamSource
import tony.core.utils.isTypesOrSubTypesOf

/**
 * Utils is
 * @author tangli
 * @date 2025/07/24 09:33
 */
internal fun isDownloadType(type: Type): Boolean =
    type == ByteArray::class.java ||
        type.isTypesOrSubTypesOf(InputStream::class.java) ||
        type.isTypesOrSubTypesOf(InputStreamSource::class.java)

internal fun Type.typeParam(index: Int = 0): Type =
    ResolvableType.forType(this).getGeneric(index).type
