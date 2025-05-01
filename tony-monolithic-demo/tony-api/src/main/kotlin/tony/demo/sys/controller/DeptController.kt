package tony.demo.sys.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import tony.demo.sys.dto.req.DeptAddReq
import tony.demo.sys.dto.req.DeptDeleteReq
import tony.demo.sys.dto.req.DeptQuery
import tony.demo.sys.dto.req.DeptUpdateReq
import tony.demo.sys.dto.resp.DeptResp
import tony.demo.sys.service.DeptService

/**
 * DeptController is
 * @author tangli
 * @date 2024/07/03 13:33
 * @since 1.0.0
 */
@Tag(name = "部门")
@RestController
class DeptController(
    private val deptService: DeptService,
) {
    @Operation(summary = "部门树", description = "部门树")
    @PostMapping("/sys/dept/tree")
    fun tree(
        @RequestBody
        req: DeptQuery,
    ): List<DeptResp> =
        deptService.tree(req)

    @Operation(summary = "新增部门", description = "新增部门")
    @PostMapping("/sys/dept/add")
    fun add(
        @Validated
        @RequestBody
        req: DeptAddReq,
    ) =
        deptService.add(req)

    @Operation(summary = "更新部门", description = "更新部门")
    @PostMapping("/sys/dept/update")
    fun update(
        @Validated
        @RequestBody
        req: DeptUpdateReq,
    ) =
        deptService.update(req)

    @Operation(summary = "删除部门", description = "删除部门")
    @PostMapping("/sys/dept/delete")
    fun delete(
        @Validated
        @RequestBody
        req: DeptDeleteReq,
    ) =
        deptService.delete(req)
}
