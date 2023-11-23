/**
 * TestController
 *
 * TODO
 *
 * @author Tang Li
 * @date 2021/12/7 14:23
 */
package com.tony.test.feign.module.file.controller

import com.tony.ApiResult
import com.tony.annotation.web.auth.NoLoginCheck
import com.tony.test.feign.module.file.api.FeignFileTestApi
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Paths

@NoLoginCheck
@RestController
class FeignFileTestController : FeignFileTestApi {

    @Value("\${test-file-path-to:}")
    lateinit var testFilePathTo: String

    @PostMapping("/upload-many", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    override fun uploadMany(
        @RequestPart("files")
        files: List<MultipartFile>,
        @RequestParam("remark")
        remark: String?
    ):ApiResult<*> {
        println(remark)
        files.forEach {
            println(it.name)
            println(it.originalFilename)
            it.transferTo(Paths.get("$testFilePathTo/${it.originalFilename}"))
        }
        return ApiResult.message()
    }

    @PostMapping("/upload-single", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    override fun uploadSingle(
        @RequestPart("file")
        file: MultipartFile,
        @RequestParam("remark")
        remark: String?
    ):ApiResult<*> {
        println(remark)
        println(file.name)
        println(file.originalFilename)
        file.transferTo(Paths.get("$testFilePathTo/${file.originalFilename}"))
        return ApiResult.message()
    }
}
