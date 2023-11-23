package com.tony.test.feign.module.file.api

import com.tony.ApiResult
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.multipart.MultipartFile

interface FeignFileTestApi {

    @PostMapping("/upload-many", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadMany(
        @RequestPart("files")
        files: List<MultipartFile>,
        @RequestParam("remark")
        remark: String?
    ): ApiResult<*>

    @PostMapping("/upload-single", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadSingle(
        @RequestPart("file")
        file: MultipartFile,
        @RequestParam("remark")
        remark: String?
    ): ApiResult<*>
}
