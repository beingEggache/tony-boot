package com.tony.api.controller

import com.tony.api.permission.NoPermissionCheck
import com.tony.auth.NoLoginCheck
import com.tony.core.ApiResult.Companion.toOneResult
import com.tony.core.utils.defaultZoneId
import com.tony.core.utils.println
import com.tony.core.utils.toString
import com.tony.db.service.UserService
import com.tony.dto.req.UserLoginReq
import com.tony.jwt.config.JwtToken
import com.tony.webcore.WebApp
import io.swagger.annotations.ApiOperation
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.util.Locale

@RestController
@Validated
class IndexController(
    private val userService: UserService
) {

    @ApiOperation("首页")
    @GetMapping("/")
    @NoLoginCheck
    @NoPermissionCheck
    fun index(): String = WebApp.appId

    @ApiOperation("区域")
    @GetMapping("/locale")
    @NoLoginCheck
    @NoPermissionCheck
    fun locale(): String = Locale.getDefault().toLanguageTag()

    @ApiOperation("zoneId")
    @GetMapping("/zone-id")
    @NoLoginCheck
    @NoPermissionCheck
    fun zoneId(): String = defaultZoneId.toString()

    @ApiOperation("时间戳")
    @GetMapping("/now")
    @NoLoginCheck
    @NoPermissionCheck
    fun now(): String = LocalDateTime.now().toString("yyyy-MM-dd HH:mm:ss")

    @ApiOperation("登录")
    @NoLoginCheck
    @NoPermissionCheck
    @PostMapping("/login")
    fun login(
        @Validated
        @RequestBody
        loginReq: UserLoginReq
    ) = JwtToken.gen("userId" to userService.login(loginReq).userId).toOneResult()

    @NoLoginCheck
    @NoPermissionCheck
    @GetMapping("/test")
    fun test() = Person(
        null,
        null,
        null,
        null
    )

    @NoLoginCheck
    @NoPermissionCheck
    @GetMapping("/test-url-get")
    fun test1(person: Person) = person.println()

    @NoLoginCheck
    @NoPermissionCheck
    @PostMapping("/test-url-post")
    fun test2(person: Person) = person.println()

    @NoLoginCheck
    @NoPermissionCheck
    @PostMapping("/test-json-post")
    fun test3(@RequestBody person: Person) = person.println()
}

data class Person(
    val array: IntArray?,
    val number: Int?,
    val string: String?,
    val map: Map<String, *>?
)
