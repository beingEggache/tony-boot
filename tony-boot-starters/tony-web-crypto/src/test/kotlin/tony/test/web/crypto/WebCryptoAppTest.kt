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

package tony.test.web.crypto

import jakarta.annotation.Resource
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.openfeign.EnableFeignClients
import tony.core.utils.getLogger
import tony.test.web.crypto.client.FeignCryptoTestClient

@EnableFeignClients
@SpringBootTest(classes = [TestWebCryptoApp::class], webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class WebCryptoAppTest {

    @Resource
    lateinit var feignCryptoTestClient: FeignCryptoTestClient

    private final val logger: Logger = getLogger()

    @Test
    fun decrypt() {
        val monoResult = feignCryptoTestClient.mono()
        logger.info(monoResult.toString())
    }
}
