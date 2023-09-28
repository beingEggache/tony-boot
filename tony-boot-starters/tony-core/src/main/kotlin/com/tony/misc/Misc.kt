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

package com.tony.misc

import com.tony.ApiResultLike
import java.math.BigDecimal
import java.math.BigInteger
import java.time.temporal.TemporalAccessor
import java.util.Date

/**
 * 不支持响应包装类
 */
public val notSupportResponseWrapClasses: Array<Class<*>?> =
    arrayOf(
        ApiResultLike::class.java,
        Enum::class.java,
        Date::class.java,
        TemporalAccessor::class.java,
        // converterType is use StringHttpMessageConverter
        java.lang.CharSequence::class.java,
        CharSequence::class.java,
        Char::class.javaObjectType,
        Char::class.javaPrimitiveType,
        Boolean::class.javaObjectType,
        Boolean::class.javaPrimitiveType,
        Long::class.javaObjectType, Long::class.javaPrimitiveType,
        Int::class.javaObjectType, Int::class.javaPrimitiveType,
        Double::class.javaObjectType, Double::class.javaPrimitiveType,
        Byte::class.javaObjectType, Byte::class.javaPrimitiveType,
        Short::class.javaObjectType, Short::class.javaPrimitiveType,
        Float::class.javaObjectType, Float::class.javaPrimitiveType,
        BigInteger::class.java,
        BigDecimal::class.java
    )
