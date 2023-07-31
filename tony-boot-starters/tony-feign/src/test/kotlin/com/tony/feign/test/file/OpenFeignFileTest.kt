/**
 * OpenFeignFileTest
 *
 * TODO
 *
 * @author tangli
 * @since 2021/12/7 14:40
 */
package com.tony.feign.test.file

import com.tony.ApiProperty
import com.tony.exception.BizException
import com.tony.feign.misc.ByteArrayMultipartFile
import com.tony.feign.test.file.client.OpenFeignTestFileClient
import com.tony.utils.getLogger
import com.tony.utils.toJsonString
import jakarta.annotation.Resource
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import java.nio.file.Files
import java.nio.file.Paths

@SpringBootTest(classes = [OpenFeignTestFileApp::class], webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class OpenFeignFileTest {

    @Resource
    lateinit var openFeignTestFileClient: OpenFeignTestFileClient

    @Value("\${test-file-path-from:}")
    lateinit var testFilePathFrom: String

    private val logger = getLogger()

    @Test
    fun testMultiFileUpload() {
        val bytes1 = Files.readAllBytes(Paths.get("$testFilePathFrom/1.jpg"))
        val file1 = ByteArrayMultipartFile(
            "uploadMany1.png",
            bytes1
        )
        val file2 = ByteArrayMultipartFile(
            "uploadMany2.png",
            bytes1
        )
        val result = openFeignTestFileClient.uploadMany(listOf(file1, file2), "test")
        if (result.code != ApiProperty.okCode) {
            throw BizException("fail")
        }
    }

    @Test
    fun testSingleFileUpload() {
        val bytes1 = Files.readAllBytes(Paths.get("$testFilePathFrom/1.jpg"))
        val file1 = ByteArrayMultipartFile(
            "uploadSingle.png",
            bytes1
        )
        val result = openFeignTestFileClient.uploadSingle(file1, "test")
        if (result.code != ApiProperty.okCode) {
            logger.info(result.toJsonString())
            throw BizException("fail")
        }
    }
}
