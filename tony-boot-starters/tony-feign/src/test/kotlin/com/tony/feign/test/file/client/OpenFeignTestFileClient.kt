package com.tony.feign.test.file.client

import com.tony.ApiResult
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.multipart.MultipartFile

@FeignClient(name = "openFeignTestFileClient", url = "http://localhost:8080")
interface OpenFeignTestFileClient {

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
