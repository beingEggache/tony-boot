/**
 * OpenFeignFileTest
 *
 * TODO
 *
 * @author Tang Li
 * @date 2021/12/7 14:40
 */
package com.tony.test.feign.module.file

import com.tony.ApiProperty
import com.tony.exception.BizException
import com.tony.feign.multipart.ByteArrayMultipartFile
import com.tony.test.feign.module.file.client.FeignFileTestClient
import com.tony.utils.getLogger
import com.tony.utils.toJsonString
import jakarta.annotation.Resource
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.support.PathMatchingResourcePatternResolver

@SpringBootTest(
    properties = ["server.port=9091"],
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
