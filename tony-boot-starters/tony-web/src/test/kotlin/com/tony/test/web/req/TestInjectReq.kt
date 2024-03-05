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

package com.tony.test.web.req

import com.fasterxml.jackson.annotation.JacksonInject
import com.fasterxml.jackson.annotation.OptBoolean

/**
 * TestInjectReq is
 * @author tangli
 * @date 2023/07/06 19:26
 */
data class TestDefaultInjectReq(

    @get:JacksonInject("string0", useInput = OptBoolean.TRUE)
    val valNotNullTrue: String,

    @get:JacksonInject("string1", useInput = OptBoolean.FALSE)
    val valNotNullFalse: String,

    @set:JacksonInject("string2", useInput = OptBoolean.TRUE)
    var varSetterTrue: String?,

    @set:JacksonInject("string3", useInput = OptBoolean.FALSE)
    var varSetterFalse: String?,
)
