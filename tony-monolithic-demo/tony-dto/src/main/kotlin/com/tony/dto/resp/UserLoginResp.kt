package com.tony.dto.resp

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

/**
 * UserLoginResp is
 * @author Tang Li
 * @date 2023/09/06 15:15
 */
@Schema(description = "登录响应")
data class UserLoginResp(
    val accessToken: String,
    val refreshToken: String,
    val expiresAt: LocalDateTime,
)
