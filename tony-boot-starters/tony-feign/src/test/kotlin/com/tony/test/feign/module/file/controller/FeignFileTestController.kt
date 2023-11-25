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
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
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

    private val resourceResolver = PathMatchingResourcePatternResolver()
    private val absolutePath = resourceResolver
        .resourceLoader
        .getResource("to")
        .file
        .absolutePath

    @PostMapping("/upload-many", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    override fun uploadMany(
        @RequestPart("files")
        files: List<MultipartFile>,
        @RequestParam("remark")
        remark: String?
    ):ApiResult<*> {
        println(remark)
        println(absolutePath)
        files.forEach {
            println(it.name)
            println(it.originalFilename)
            it.transferTo(Paths.get("$absolutePath/${it.originalFilename}"))
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
        println(absolutePath)
        file.transferTo(Paths.get("$absolutePath/${file.originalFilename}"))
        return ApiResult.message()
    }
}
