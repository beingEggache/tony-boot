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
 * TestController
 *
 * TODO
 *
 * @author tangli
 * @date 2021/12/7 14:23
 */
package tony.test.feign.module.file.controller

import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import tony.annotation.web.auth.NoLoginCheck
import tony.core.model.ApiResult
import tony.test.feign.module.file.api.FeignFileTestApi
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
