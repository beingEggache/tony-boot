package com.tony.dto.resp

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

/**
 *
 * @author tangli
 * @since 2020-11-14 15:18
 */
@Schema(title = "用户响应")
data class UserResp(

    @Schema(title = "用户Id")
    val userId: String?,

    @Schema(title = "用户登录名")
    val userName: String?,

    @Schema(title = "用户真实姓名")
    val realName: String?,

    @Schema(title = "手机号")
    val mobile: String?,

    @Schema(title = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val createTime: LocalDateTime?,

    @Schema(title = "备注")
    val remark: String?,

    @Schema(title = "用户状态：1:启用，0:禁用。 可根据需求扩展")
    val states: Int?
)
