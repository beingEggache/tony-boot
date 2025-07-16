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

package tony.feign.codec

import feign.Response
import feign.codec.ErrorDecoder
import tony.core.exception.ApiException

/**
 * Feign 错误默认解析器.
 * 抛出全局统一框架层异常 [ApiException]
 * @author tangli
 * @date 2023/09/13 19:33
 */
internal class DefaultErrorDecoder : ErrorDecoder {
    private val default = ErrorDecoder.Default()

    override fun decode(
        methodKey: String?,
        response: Response,
    ): Exception {
        val status = response.status()
        return if (status in 400..<500) {
            val url =
                response
                    .request()
                    .url()
            ApiException(
                "$methodKey error,status:$status,reason:${response.reason()},url: $url",
                status * 100
            )
        } else {
            default.decode(methodKey, response)
        }
    }
}
