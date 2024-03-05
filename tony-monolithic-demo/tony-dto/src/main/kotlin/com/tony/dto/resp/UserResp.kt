package com.tony.dto.resp

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

/**
 *
 * @author tangli
 * @date 2020-11-14 15:18
 */
@Schema(description = "用户响应")
class UserResp {
    @Schema(description = "用户Id")
    var userId: String? = null

    @Schema(description = "用户登录名")
    var userName: String? = null

    @Schema(description = "用户真实姓名")
    var realName: String? = null

    @Schema(description = "手机号")
    var mobile: String? = null

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    var createTime: LocalDateTime? = null

    @Schema(description = "备注")
    var remark: String? = null

    @Schema(description = "状态：1-启用，0-禁用")
    var enabled: Boolean? = null
}
