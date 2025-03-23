package com.tony.demo.sys.dto.resp

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

/**
 * 用户响应
 * @author tangli
 * @date 2020-11-14 15:18
 */
@Schema(description = "用户响应")
data class EmployeeResp(
    @param:Schema(description = "用户Id")
    val employeeId: String,
    @param:Schema(description = "用户名")
    val account: String,
    @param:Schema(description = "用户姓名")
    val realName: String,
    @param:Schema(description = "用户手机号")
    val employeeMobile: String,
    @param:Schema(description = "创建时间")
    val createTime: LocalDateTime,
    @param:Schema(description = "备注")
    val remark: String,
    @param:Schema(description = "状态：1-启用，0-禁用")
    val enabled: Boolean,
) {
    @get:Schema(description = "部门id")
    var deptIds: Collection<String> = setOf()

    @get:Schema(description = "角色id")
    var roleIds: Collection<String> = setOf()
}
