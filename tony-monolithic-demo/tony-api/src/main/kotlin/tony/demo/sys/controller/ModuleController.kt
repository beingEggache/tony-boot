package tony.demo.sys.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import tony.RowsWrapper
import tony.demo.sys.dto.req.ModuleQuery
import tony.demo.sys.dto.req.ModuleSubmitReq
import tony.demo.sys.service.ModuleService

/**
 * ModuleController is
 * @author tangli
 * @date 2024/07/05 11:39
 * @since 1.0.0
 */
@Tag(name = "模块")
@RestController
class ModuleController(
    private val moduleService: ModuleService,
) {
    @Operation(summary = "提交全部", description = "提交全部")
    @PostMapping("/sys/module/submit-all")
    fun submitAll(
        @Validated
        @RequestBody
        req: RowsWrapper<ModuleSubmitReq>,
    ) =
        moduleService.submitAll(req)

    @Operation(summary = "树", description = "树")
    @PostMapping("/sys/module/tree")
    fun tree() =
        moduleService.tree("")

    @Operation(summary = "列表", description = "列表")
    @PostMapping("/sys/module/list")
    fun list(
        @RequestBody
        req: ModuleQuery,
    ) =
        moduleService.list(req)
}
