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

package com.tony.exception

/**
 * 全局异常类型
 * @author Tang Li
 * @date 2021/12/6 10:51
 */
import com.tony.ApiProperty

/**
 * 全局异常类型基类.
 *
 * @param message 异常消息.
 * @param code 错误码.
 * @param cause 引用异常.
 *
 * @author Tang Li
 * @date 2021/12/6 10:51
 */
public open class BaseException
    @JvmOverloads
    constructor(
        override val message: String? = "",
        public open val code: Int,
        override val cause: Throwable? = null,
    ) : RuntimeException(message, cause)

/**
 * 自定义框架层异常.
 *
 * @param message 异常信息.
 * @param code 错误码. 默认 [ApiProperty.errorCode]
 * @param cause 引用异常.
 *
 * @author Tang Li
 * @date 2021/12/6 10:51
 */
public open class ApiException
    @JvmOverloads
    constructor(
        override val message: String? = "",
        override val code: Int = ApiProperty.errorCode,
        cause: Throwable? = null,
    ) : BaseException(message.toString(), code, cause) {
        public constructor(message: String?, cause: Throwable) : this(message, ApiProperty.errorCode, cause)
    }

/**
 * 全局业务用异常
 * @param message 异常消息.
 * @param code 错误码. 默认 [ApiProperty.preconditionFailedCode]
 *
 * @author Tang Li
 * @date 2021/12/6 10:51
 */
public open class BizException
    @JvmOverloads
    constructor(
        override val message: String,
        override val code: Int = ApiProperty.preconditionFailedCode,
    ) : BaseException(message, code)
