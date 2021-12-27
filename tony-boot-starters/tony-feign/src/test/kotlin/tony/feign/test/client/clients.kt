/**
 * tony-dependencies
 * clients
 *
 * TODO
 *
 * @author tangli
 * @since 2021/12/27 9:31
 */
package tony.feign.test.client

import com.tony.ApiResult
import tony.feign.test.dto.Person
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
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

@FeignClient(name = "openFeignTestSignatureClient", url = "http://localhost:8080")
interface OpenFeignTestSignatureClient {

    @PostMapping(
        "/test-url-post",
        consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE],
        headers = ["X-Header-Process=byHeaderRequestProcessor"]
    )
    fun test2(@RequestBody person: Map<String, *>): ApiResult<Any>

    @PostMapping("/test/test-json-post", headers = ["X-Header-Process=byHeaderRequestProcessor"])
    fun testSignature(@RequestBody person: Person): ApiResult<Any>
}
