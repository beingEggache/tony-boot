package tony.mono.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import kotlin.Pair;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tony.annotation.web.auth.NoLoginCheck;
import tony.core.model.MonoValue;
import tony.core.model.MonoValues;
import tony.core.model.PageQuery;
import tony.core.model.PageResult;
import tony.jwt.JwtToken;
import tony.mono.db.po.User;
import tony.mono.db.service.UserService;
import tony.mono.dto.req.UserLoginReq;
import tony.web.WebContext;
import tony.web.utils.Servlets;

import java.net.URL;
import java.util.Map;

/**
 * IndexController
 *
 * @author tangli
 * @date 2024/01/31 15:27
 */
@RequiredArgsConstructor
@Validated
@RestController
public class IndexController {

    private final UserService userService;

    @Operation(summary = "首页")
    @NoLoginCheck
    @GetMapping("/")
    public String index() {
        return "index";
    }

    @Operation(summary = "登录")
    @NoLoginCheck
    @PostMapping("/login")
    public MonoValue<String> login(
        @Validated
        @RequestBody final UserLoginReq req) {
        return MonoValues.wrap(JwtToken.gen(new Pair<>("userId", userService.login(req))));
    }

    @Operation(summary = "用户信息")
    @NoLoginCheck
    @PostMapping("/info")
    public void info() {
        // Yeah
    }

    @Operation(summary = "用户列表")
    @NoLoginCheck
    @PostMapping("/user/list")
    public PageResult<User> list(
        @Validated
        @RequestBody final PageQuery<String> req
    ) {
        return userService.list(req);
    }

    @Operation(summary = "requestHeaders")
    @NoLoginCheck
    @PostMapping("/request-headers")
    public Map<String, ?> requestHeaders() {
        return Servlets.requestHeaders();
    }

    @Operation(summary = "responseHeaders")
    @NoLoginCheck
    @PostMapping("/response-headers")
    public Map<String, ?> responseHeaders() {
        return Servlets.responseHeaders();
    }

    @Operation(summary = "origin")
    @NoLoginCheck
    @PostMapping("/origin")
    public MonoValue<String> origin() {
        return MonoValues.wrap(Servlets.origin());
    }

    @Operation(summary = "remoteIp")
    @NoLoginCheck
    @PostMapping("/remote-ip")
    public MonoValue<String> remoteIp() {
        return MonoValues.wrap(Servlets.remoteIp());
    }

    @Operation(summary = "url")
    @NoLoginCheck
    @PostMapping("/url")
    public URL url() {
        return Servlets.url();
    }

    @Operation(summary = "requestParsedMedia")
    @NoLoginCheck
    @PostMapping("/request-parsed-media")
    public MediaType requestParsedMedia() {
        return Servlets.parseMediaType(WebContext.request().getContentType());
    }

    @Operation(summary = "responseParsedMedia")
    @NoLoginCheck
    @PostMapping("/response-parsed-media")
    public MediaType responseParsedMedia() {
        return Servlets.parseMediaType(WebContext.response().getContentType());
    }
}
