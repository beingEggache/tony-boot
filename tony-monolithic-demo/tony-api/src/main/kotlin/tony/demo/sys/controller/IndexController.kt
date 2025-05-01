package tony.demo.sys.controller

import io.swagger.v3.oas.annotations.Operation
import java.time.LocalDateTime
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import tony.annotation.web.auth.NoLoginCheck
import tony.demo.MonoApiWebContext.appId
import tony.demo.MonoApiWebContext.tenantId
import tony.demo.permission.NoPermissionCheck
import tony.demo.sys.dto.req.ChangePwdReq
import tony.demo.sys.dto.req.LoginReq
import tony.demo.sys.dto.resp.InfoResp
import tony.demo.sys.dto.resp.LoginResp
import tony.demo.sys.service.IndexService
import tony.jwt.JwtToken
import tony.jwt.config.JwtProperties
import tony.web.WebContext
import tony.web.auth.WebContextExtensions.userId

@RestController
@Validated
class IndexController(
    private val indexService: IndexService,
    private val jwtProperties: JwtProperties,
) {
    @Operation(summary = "首页")
    @GetMapping("/")
    @NoLoginCheck
    @NoPermissionCheck
    fun index(): String =
        ""

    @Operation(summary = "登录")
    @NoLoginCheck
    @NoPermissionCheck
    @PostMapping("/login")
    fun login(
        @Validated
        @RequestBody
        req: LoginReq,
    ): LoginResp {
        val token = JwtToken.gen("userId" to indexService.login(req))
        return LoginResp(token, LocalDateTime.now().minusMinutes(jwtProperties.expiredMinutes))
    }

    @Operation(summary = "登出")
    @NoLoginCheck
    @NoPermissionCheck
    @PostMapping("/logout")
    fun logout() =
        Unit

    @Operation(summary = "登录用户信息", description = "登录用户信息")
    @NoPermissionCheck
    @GetMapping("/info")
    fun info(): InfoResp =
        indexService.info(WebContext.userId, WebContext.appId, WebContext.tenantId)

    @Operation(summary = "修改密码", description = "修改密码")
    @NoPermissionCheck
    @PostMapping("/change-pwd")
    fun changePwd(
        @Validated
        @RequestBody
        req: ChangePwdReq,
    ) =
        indexService.changePwd(req)
}
