package tony.demo.sys.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import tony.FlattenPageQuery
import tony.PageResult
import tony.demo.sys.dto.req.RoleAddReq
import tony.demo.sys.dto.req.RoleAssignModulesReq
import tony.demo.sys.dto.req.RoleDeleteReq
import tony.demo.sys.dto.req.RoleModuleQuery
import tony.demo.sys.dto.req.RoleQuery
import tony.demo.sys.dto.req.RoleUpdateReq
import tony.demo.sys.dto.resp.ModuleResp
import tony.demo.sys.dto.resp.RoleResp
import tony.demo.sys.service.RoleService

/**
 * 角色Controller
 * @author tangli
 * @date 2024/07/03 13:33
 */
@Tag(name = "角色")
@RestController
class RoleController(
    private val service: RoleService,
) {
    @Operation(summary = "列表", description = "列表")
    @PostMapping("/sys/role/list")
    fun tree(
        @Validated
        @RequestBody
        req: FlattenPageQuery<RoleQuery>,
    ): PageResult<RoleResp> =
        service.list(req)

    @Operation(summary = "新增", description = "新增")
    @PostMapping("/sys/role/add")
    fun add(
        @Validated
        @RequestBody
        req: RoleAddReq,
    ) =
        service.add(req)

    @Operation(summary = "更新", description = "更新")
    @PostMapping("/sys/role/update")
    fun update(
        @Validated
        @RequestBody
        req: RoleUpdateReq,
    ) =
        service.update(req)

    @Operation(summary = "删除", description = "删除")
    @PostMapping("/sys/role/delete")
    fun delete(
        @Validated
        @RequestBody
        req: RoleDeleteReq,
    ) =
        service.delete(req)

    @Operation(summary = "分配模块", description = "分配模块")
    @PostMapping("/sys/role/assign-modules")
    fun assignModules(
        @Validated
        @RequestBody
        req: RoleAssignModulesReq,
    ) =
        service.assignModules(req)

    @Operation(summary = "角色模块id列表", description = "分配模块")
    @PostMapping("/sys/role/modules")
    fun roleModules(
        @Validated
        @RequestBody
        req: RoleModuleQuery,
    ): Collection<ModuleResp> =
        service.listRoleModules(req)
}
