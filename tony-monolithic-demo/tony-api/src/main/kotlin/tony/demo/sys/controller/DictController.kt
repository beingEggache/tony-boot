package tony.demo.sys.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import tony.FlattenPageQuery
import tony.PageResult
import tony.demo.MonoApiWebContext.appId
import tony.demo.sys.dto.req.DictAddReq
import tony.demo.sys.dto.req.DictDeleteReq
import tony.demo.sys.dto.req.DictQuery
import tony.demo.sys.dto.req.DictTypeAddReq
import tony.demo.sys.dto.req.DictTypeDeleteReq
import tony.demo.sys.dto.req.DictTypeQuery
import tony.demo.sys.dto.req.DictTypeUpdateReq
import tony.demo.sys.dto.req.DictUpdateReq
import tony.demo.sys.dto.resp.DictResp
import tony.demo.sys.dto.resp.DictTypeResp
import tony.demo.sys.dto.resp.DictValuesMapResp
import tony.demo.sys.service.DictService
import tony.web.WebContext

/**
 * DictController is
 * @author tangli
 * @date 2024/07/03 13:33
 * @since 1.0.0
 */
@Tag(name = "字典")
@RestController
class DictController(
    private val dictService: DictService,
) {
    @Operation(summary = "字典列表", description = "字典列表")
    @PostMapping("/sys/dict/list")
    fun dictList(
        @RequestBody
        req: FlattenPageQuery<DictQuery>,
    ): PageResult<DictResp> =
        dictService.dictList(req)

    @Operation(summary = "字典映射", description = "字典映射")
    @PostMapping("/sys/dict/values-map")
    fun dictValuesMap(): DictValuesMapResp =
        dictService.dictValuesMap(WebContext.appId)

    @Operation(summary = "新增字典", description = "新增字典")
    @PostMapping("/sys/dict/add")
    fun addDict(
        @Validated
        @RequestBody
        req: DictAddReq,
    ) =
        dictService.addDict(req)

    @Operation(summary = "更新字典", description = "更新字典")
    @PostMapping("/sys/dict/update")
    fun updateDict(
        @Validated
        @RequestBody
        req: DictUpdateReq,
    ) =
        dictService.updateDict(req)

    @Operation(summary = "删除字典", description = "删除字典")
    @PostMapping("/sys/dict/delete")
    fun deleteDict(
        @Validated
        @RequestBody
        req: DictDeleteReq,
    ) =
        dictService.deleteDict(req)

    @Operation(summary = "字典类型树", description = "字典类型树")
    @PostMapping("/sys/dict-type/tree")
    fun dictTypeTree(
        @RequestBody
        req: DictTypeQuery,
    ): List<DictTypeResp> =
        dictService.dictTypeTree(req)

    @Operation(summary = "新增字典类型", description = "新增字典类型")
    @PostMapping("/sys/dict-type/add")
    fun addDictType(
        @Validated
        @RequestBody
        req: DictTypeAddReq,
    ) =
        dictService.addDictType(req)

    @Operation(summary = "更新典类型", description = "更新典类型")
    @PostMapping("/sys/dict-type/update")
    fun updateDictType(
        @Validated
        @RequestBody
        req: DictTypeUpdateReq,
    ) =
        dictService.updateDictType(req)

    @Operation(summary = "删除字典类型", description = "删除字典类型")
    @PostMapping("/sys/dict-type/delete")
    fun delete(
        @Validated
        @RequestBody
        req: DictTypeDeleteReq,
    ) =
        dictService.delete(req)
}
