package tony.demo.sys.controller

import io.swagger.v3.oas.annotations.Operation
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import tony.annotation.web.auth.NoLoginCheck
import tony.core.model.MonoResult
import tony.core.model.MonoResult.Companion.ofMonoResult
import tony.demo.permission.NoPermissionCheck
import tony.demo.sys.dto.req.WrappedReq
import tony.redis.RedisManager

/**
 * RedisController is
 * @author tangli
 * @date 2023/07/10 19:21
 */
@RestController
@Validated
class RedisController {
    @Operation(summary = "redis - set value")
    @NoLoginCheck
    @NoPermissionCheck
    @PostMapping("/redis/set-value")
    fun setValue(
        @Validated
        @RequestBody
        req: WrappedReq<Pair<String, String>>,
    ) {
        RedisManager.values.set(req.value!!.first, req.value!!.second)
    }

    @Operation(summary = "redis - get value")
    @NoLoginCheck
    @NoPermissionCheck
    @PostMapping("/redis/get-value")
    fun getValue(
        @Validated
        @RequestBody
        req: WrappedReq<String>,
    ): MonoResult<String>? =
        RedisManager.values.get<String>(req.value!!)?.ofMonoResult()
}
