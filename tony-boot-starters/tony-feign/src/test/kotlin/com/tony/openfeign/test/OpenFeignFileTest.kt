/**
 * tony-dependencies
 * OpenFeignFileTest
 *
 * TODO
 *
 * @author tangli
 * @since 2021/12/7 14:40
 */
package com.tony.openfeign.test

import com.tony.ApiProperty
import com.tony.ApiResult
import com.tony.exception.BizException
import com.tony.feign.misc.ByteArrayMultipartFile
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths
import javax.annotation.Resource

@SpringBootTest(classes = [OpenFeignApp::class])
class OpenFeignFileTest {

    @Resource
    lateinit var testFileClient: TestFileClient

    @Test
    fun testMultiFileUpload() {
        val bytes1 = Files.readAllBytes(Paths.get("C:\\wokspace\\pdf\\48A246021EF4497AA544AC9ECE5F60CB.png"))
        val file1 = ByteArrayMultipartFile(
            "uploadMany1.png",
            bytes1
        )
        val file2 = ByteArrayMultipartFile(
            "uploadMany2.png",
            bytes1
        )
        val result = testFileClient.uploadMany(listOf(file1, file2), "test")
        if (result.code != ApiProperty.successCode) {
            throw BizException("fail")
        }
    }

    @Test
    fun testSingleFileUpload() {
        val bytes1 = Files.readAllBytes(Paths.get("C:\\wokspace\\pdf\\48A246021EF4497AA544AC9ECE5F60CB.png"))
        val file1 = ByteArrayMultipartFile(
            "uploadSingle.png",
            bytes1
        )
        val result = testFileClient.uploadSingle(file1, "test")
        if (result.code != ApiProperty.successCode) {
            throw BizException("fail")
        }
    }
}


@FeignClient(name = "testFileClient", url = "http://localhost:8080")
interface TestFileClient {

    @PostMapping("/upload-many", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadMany(
        @RequestPart("files")
        files: List<MultipartFile>,
        @RequestParam("remark")
        remark: String?
    ): ApiResult<Any>

    @PostMapping("/upload-single", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadSingle(
        @RequestPart("file")
        file: MultipartFile,
        @RequestParam("remark")
        remark: String?
    ): ApiResult<Any>
}
