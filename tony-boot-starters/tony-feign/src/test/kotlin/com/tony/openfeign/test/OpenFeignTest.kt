package com.tony.openfeign.test

import com.tony.ApiProperty
import com.tony.ApiResult
import com.tony.feign.genSign
import com.tony.feign.interceptor.ByHeaderRequestProcessor
import com.tony.feign.jsonNode
import com.tony.feign.sortRequestBody
import com.tony.utils.getLogger
import com.tony.utils.toJsonString
import com.tony.utils.toString
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import java.time.LocalDateTime
import javax.annotation.Resource

@SpringBootTest(classes = [OpenFeignApp::class])
class OpenFeignTest {

    @Resource
    lateinit var testFeignClient: TestFeignClient

    @Resource
    lateinit var baiduFeignClient: BaiduFeignClient

    private val logger = getLogger()

    @Test
    fun testSignature() {
        val person = Person(listOf(1, 2, 3).toIntArray(), 123, "432", mapOf("qwe" to 123))
        val result = testFeignClient.testSignature(person)
        if (result.code != ApiProperty.successCode) {
            logger.error(result.toJsonString())
            throw RuntimeException("error")
        }
        logger.info(result.toJsonString())
    }
}

@FeignClient(name = "test", url = "localhost:8080")
interface TestFeignClient {

    @PostMapping("/login")
    fun login(@RequestBody map: Map<String, String>): ApiResult<Map<String, *>>

    @GetMapping("/test")
    fun test(): ApiResult<Person>

    @GetMapping("/test-url-get", consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun test1(person: Person)

    @PostMapping(
        "/test-url-post",
        consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE],
        headers = ["X-Header-Process=byHeaderRequestProcessor"]
    )
    fun test2(@RequestBody person: Map<String, *>): ApiResult<Any>

    @PostMapping("/test/test-json-post", headers = ["X-Header-Process=byHeaderRequestProcessor"])
    fun testSignature(@RequestBody person: Person): ApiResult<Any>
}

@FeignClient(name = "baidu", url = "www.baidu.com")
interface BaiduFeignClient {

    @GetMapping("")
    fun index(): String
}

class MyByHeaderRequestProcessor : ByHeaderRequestProcessor {

    private val logger = getLogger()

    override fun process(request: Request): Request {
        val requestBody = request.body
        if (requestBody == null) {
            logger.warn("request body is null")
            return request
        }

        val timestampStr = LocalDateTime.now().toString("yyyy-MM-dd HH:mm:ss")
        val sortedJsonStr = requestBody.jsonNode().sortRequestBody(timestampStr)
        val sign = sortedJsonStr.genSign("appId", "secret")
        return request.newBuilder()
            .addHeader("x-signature", sign)
            .addHeader("x-timestamp", timestampStr)
            .method(request.method, sortedJsonStr.toRequestBody(requestBody.contentType()))
            .build()
    }

}

data class Person(
    val array: IntArray?,
    val number: Int?,
    val string: String?,
    val map: Map<String, *>?
)
