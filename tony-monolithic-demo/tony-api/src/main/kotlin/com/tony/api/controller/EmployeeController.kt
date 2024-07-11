package com.tony.api.controller

import com.tony.db.service.EmployeeService
import com.tony.dto.req.EmployeeAddReq
import com.tony.dto.req.EmployeeAssignRoleReq
import com.tony.dto.req.EmployeeDetailReq
import com.tony.dto.req.EmployeeQuery
import com.tony.dto.req.EmployeeResetPwdReq
import com.tony.dto.req.EmployeeToggleEnabledReq
import com.tony.dto.req.EmployeeUpdateReq
import com.tony.FlattenPageQuery
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@Tag(name = "用户")
@RestController
class EmployeeController(
    private val service: EmployeeService,
) {
    @Operation(summary = "新增", description = "新增")
    @PostMapping("/employee/add")
    fun add(
        @Validated
        @RequestBody
        req: EmployeeAddReq,
    ) = service.add(req)

    @Operation(summary = "更新", description = "更新")
    @PostMapping("/employee/update")
    fun update(
        @Validated
        @RequestBody
        req: EmployeeUpdateReq,
    ) = service.update(req)

    @Operation(summary = "列表", description = "列表")
    @PostMapping("/employee/list")
    fun list(
        @Validated
        @RequestBody req: FlattenPageQuery<EmployeeQuery>,
    ) = service.list(req)

    @Operation(summary = "详情", description = "详情")
    @PostMapping("/employee/detail")
    fun detail(
        @Validated
        @RequestBody req: EmployeeDetailReq,
    ) = service.detail(req)

    @Operation(summary = "启用,停用", description = "用户启用,停用")
    @PostMapping("/employee/toggle-enabled")
    fun toggleEnabled(
        @Validated
        @RequestBody req: EmployeeToggleEnabledReq,
    ) = service.toggleEnabled(req)

    @Operation(summary = "重置密码", description = "重置密码")
    @PostMapping("/employee/reset-pwd")
    fun resetPwd(
        @Validated
        @RequestBody req: EmployeeResetPwdReq,
    ) = service.resetPwd(req)

    @Operation(summary = "分配角色", description = "分配角色")
    @PostMapping("/employee/assign-roles")
    fun assignRoles(
        @Validated
        @RequestBody req: EmployeeAssignRoleReq,
    ) = service.assignRoles(req)
}
