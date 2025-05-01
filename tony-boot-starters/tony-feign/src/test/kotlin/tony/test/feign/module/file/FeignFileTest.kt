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

/**
 * OpenFeignFileTest
 *
 * TODO
 *
 * @author tangli
 * @date 2021/12/7 14:40
 */
package tony.test.feign.module.file

import tony.ApiProperty
import tony.exception.BizException
import tony.feign.multipart.ByteArrayMultipartFile
import tony.test.feign.module.file.client.FeignFileTestClient
import tony.utils.getLogger
import tony.utils.toJsonString
import jakarta.annotation.Resource
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.support.PathMatchingResourcePatternResolver

@Execution(ExecutionMode.CONCURRENT)
@SpringBootTest(
    properties = [
        "server.port=10002"
    ],
    classes = [FeignFileTestApp::class],
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
)
class FeignFileTest {

    @Resource
    lateinit var feignFileTestClient: FeignFileTestClient

    private val resourceResolver = PathMatchingResourcePatternResolver()

    private val logger = getLogger()

    @Test
    fun testMultiFileUpload() {
        val bytes1 = resourceResolver
            .resourceLoader
            .getResource("/feign-test.png")
            .contentAsByteArray
        val file1 = ByteArrayMultipartFile(
            "uploadMany1.png",
            bytes1
        )
        val file2 = ByteArrayMultipartFile(
            "uploadMany2.png",
            bytes1
        )
        val result = feignFileTestClient.uploadMany(listOf(file1, file2), "test")
        if (result.code != ApiProperty.okCode) {
            throw BizException("fail")
        }
    }

    @Test
    fun testSingleFileUpload() {
        val bytes1 = resourceResolver
            .resourceLoader
            .getResource("/feign-test.png")
            .contentAsByteArray
        val file1 = ByteArrayMultipartFile(
            "uploadSingle.png",
            bytes1
        )
        val result = feignFileTestClient.uploadSingle(file1, "test")
        if (result.code != ApiProperty.okCode) {
            logger.info(result.toJsonString())
            throw BizException("fail")
        }
    }
}
