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
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
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
            "files",
            "uploadMany1.png",
            bytes1
        )
        val file2 = ByteArrayMultipartFile(
            "files",
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
            "file",
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

class ByteArrayMultipartFile(
    private val name: String,
    private val originalFilename: String,
    private val bytes: ByteArray,
    private val contentType: String? = null
) : MultipartFile {

    override fun isEmpty(): Boolean = bytes.isEmpty()

    override fun getSize(): Long = bytes.size.toLong()

    override fun getBytes(): ByteArray = bytes

    override fun getInputStream(): InputStream = ByteArrayInputStream(bytes)

    override fun getName(): String = name

    override fun getOriginalFilename(): String = originalFilename

    override fun getContentType(): String? = contentType

    override fun transferTo(destination: File) {
        FileOutputStream(destination).use {
            it.write(bytes)
        }
    }
}
