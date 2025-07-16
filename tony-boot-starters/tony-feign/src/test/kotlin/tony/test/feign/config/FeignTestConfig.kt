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

package tony.test.feign.config

import feign.RequestTemplate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import tony.core.TOKEN_HEADER_NAME
import tony.core.utils.getLogger
import tony.core.utils.ifNullOrBlank
import tony.core.utils.jsonNode
import tony.core.utils.toString
import tony.feign.genSign
import tony.feign.interceptor.request.RequestProcessor
import tony.feign.sortRequestBody
import java.time.LocalDateTime

/**
 * FeignTestConfig is
 * @author tangli
 * @date 2023/11/27 19:43
 */
@Configuration
class FeignTestConfig {
    @Bean
    fun addTokenRequestProcessor(): AddTokenRequestProcessor =
        AddTokenRequestProcessor()

    @Bean
    fun signatureRequestProcessor(): SignatureRequestProcessor {
        return SignatureRequestProcessor()
    }
}


val mockWebAttributes = mutableMapOf<String, String>()

class AddTokenRequestProcessor : RequestProcessor {
    override fun invoke(template: RequestTemplate) {
        template.header(TOKEN_HEADER_NAME, mockWebAttributes["token"].ifNullOrBlank())
    }
}

class SignatureRequestProcessor : RequestProcessor {

    private val logger = getLogger()
    override operator fun invoke(template: RequestTemplate) {
        val body = template.body()
        if (body?.isNotEmpty() != true) {
            return
        }
        val appId = "appId"
        val secret = "secret"

        val timestampStr = LocalDateTime.now().toString("yyyy-MM-dd HH:mm:ss")
        logger.info("timestampStr:$timestampStr")
        val sortedJsonStr = body.jsonNode().sortRequestBody(timestampStr)
        logger.info("sortedJsonStr:$sortedJsonStr")
        val sign = sortedJsonStr.genSign(appId, secret)

        template
            .header("X-App-Id", appId)
            .header("X-Signature", sign)
            .header("X-Timestamp", timestampStr)
            .body(sortedJsonStr)
    }
}
