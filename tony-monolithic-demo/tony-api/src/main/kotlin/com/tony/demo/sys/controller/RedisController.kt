package com.tony.demo.sys.controller

import com.tony.MonoResult
import com.tony.MonoResult.Companion.ofMonoResult
import com.tony.annotation.web.auth.NoLoginCheck
import com.tony.demo.permission.NoPermissionCheck
import com.tony.demo.sys.dto.req.WrappedReq
import com.tony.redis.RedisManager
import io.swagger.v3.oas.annotations.Operation
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

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
