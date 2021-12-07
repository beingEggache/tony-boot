/**
 * tony-dependencies
 * TestController
 *
 * TODO
 *
 * @author tangli
 * @since 2021/12/7 14:23
 */
package com.tony.api.controller

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Paths

@RestController
class FileController {

    @PostMapping("/upload-many", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadMany(
        @RequestPart("files")
        files: List<MultipartFile>,
        @RequestParam("remark")
        remark: String?
    ) {
        println(remark)
        files.forEach {
            println(it.name)
            println(it.originalFilename)
            it.transferTo(Paths.get("C:\\wokspace\\pdf\\${it.originalFilename}"))
        }
    }

    @PostMapping("/upload-single", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadSingle(
        @RequestPart("file")
        file: MultipartFile,
        @RequestParam("remark")
        remark: String?
    ) {
        println(remark)
        println(file.name)
        println(file.originalFilename)
        file.transferTo(Paths.get("C:\\wokspace\\pdf\\${file.originalFilename}"))
    }
}
