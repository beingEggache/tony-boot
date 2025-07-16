package tony.demo.sys.controller

import io.swagger.v3.oas.annotations.Operation
import java.time.LocalDateTime
import java.util.Locale
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import tony.annotation.web.auth.NoLoginCheck
import tony.core.utils.toString
import tony.demo.permission.NoPermissionCheck

@RestController
@Validated
class TestController {
    @Operation(summary = "区域")
    @GetMapping("/locale")
    @NoLoginCheck
    @NoPermissionCheck
    fun locale(): String =
        Locale.getDefault().toLanguageTag()

    @Operation(summary = "时间戳")
    @GetMapping("/now")
    @NoLoginCheck
    @NoPermissionCheck
    fun now(): String =
        LocalDateTime.now().toString("yyyy-MM-dd HH:mm:ss")

    @Operation(summary = "空")
    @NoLoginCheck
    @NoPermissionCheck
    @GetMapping("/void")
    fun void() {
    }

    @Operation(summary = "异常")
    @NoLoginCheck
    @NoPermissionCheck
    @GetMapping("/exception")
    fun exception(): Unit =
        throw Exception("exception")

    @Operation(summary = "/build")
    @NoLoginCheck
    @NoPermissionCheck
    @GetMapping("/build")
    fun build(): String =
        "tony:build-script:0.1-SNAPSHOT"
}
