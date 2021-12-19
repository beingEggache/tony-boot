package com.tony.openfeign.test

import com.tony.ApiResult
import com.tony.feign.genSign
import com.tony.feign.interceptor.ByHeaderRequestProcessor
import com.tony.feign.jsonNode
import com.tony.feign.sortRequestBody
import com.tony.utils.getLogger
import com.tony.utils.println
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
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.InetSocketAddress
import java.net.Socket
import java.time.LocalDateTime
import javax.annotation.Resource
import kotlin.streams.toList

@SpringBootTest(classes = [OpenFeignApp::class])
class OpenFeignTest {

    @Resource
    lateinit var testFeignClient: TestFeignClient

    @Resource
    lateinit var baiduFeignClient: BaiduFeignClient

    @Test
    fun test1() {
        val person = Person(listOf(1, 2, 3).toIntArray(), 123, "432", mapOf("qwe" to 123))
        val result = testFeignClient.test3(person)
        println(result.toJsonString())
    }

    @Test
    fun test2() {
        val socket = Socket()
        val address = InetSocketAddress("localhost", 8080)
        socket.connect(address)
        val httpRequest = """|POST /test-url-post HTTP/1.1
                             |Accept: */*
                             |Content-Type: application/x-www-form-urlencoded
                             |Content-Length: 21
                             |Host: localhost:8080
                             |Connection: Keep-Alive
                             |Accept-Encoding: gzip
                             |User-Agent: okhttp/4.9.1
                             |
                             |number=123&string=432""".trimMargin()
        val httpRequest1 = """|POST /test-url-post HTTP/1.1
                            |Host: localhost:8080
                            |Connection: keep-alive
                            |Content-Length: 21
                            |Accept: */*
                            |Content-Type: application/x-www-form-urlencoded; charset=UTF-8
                            |sec-ch-ua-platform: "Windows"
                            |Origin: http://localhost:8080
                            |Sec-Fetch-Site: same-origin
                            |Sec-Fetch-Mode: cors
                            |Sec-Fetch-Dest: empty
                            |Referer: http://localhost:8080/doc.html
                            |Accept-Encoding: gzip, deflate, br
                            |Accept-Language: zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6
                              |
                              |number=123&string=432""".trimMargin()
        socket.use { s ->
            s.getOutputStream().use { outputStream ->
                OutputStreamWriter(outputStream).use { osw ->
                    BufferedWriter(osw).use { bw ->
                        bw.write(
                            httpRequest
                        )
                        bw.flush()
                    }
                    s.getInputStream().use { inputStream ->
                        BufferedInputStream(inputStream).use { bi ->
                            InputStreamReader(bi).use { isr ->
                                BufferedReader(isr).use { br ->
                                    br.lines().use { it.toList().joinToString("\n") }.println()
                                }
                            }
                        }
                    }
                }
            }
        }
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
    fun test3(@RequestBody person: Person): ApiResult<Any>
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
