package com.tony.sys.controller

import com.tony.FlattenPageQuery
import com.tony.MonoApiWebContext.appId
import com.tony.PageResult
import com.tony.sys.dto.req.DictAddReq
import com.tony.sys.dto.req.DictDeleteReq
import com.tony.sys.dto.req.DictQuery
import com.tony.sys.dto.req.DictTypeAddReq
import com.tony.sys.dto.req.DictTypeDeleteReq
import com.tony.sys.dto.req.DictTypeQuery
import com.tony.sys.dto.req.DictTypeUpdateReq
import com.tony.sys.dto.req.DictUpdateReq
import com.tony.sys.dto.resp.DictResp
import com.tony.sys.dto.resp.DictTypeResp
import com.tony.sys.dto.resp.DictValuesMapResp
import com.tony.sys.service.DictService
import com.tony.web.WebContext
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

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
    @PostMapping("/dict/list")
    fun dictList(
        @RequestBody
        req: FlattenPageQuery<DictQuery>,
    ): PageResult<DictResp> =
        dictService.dictList(req)

    @Operation(summary = "字典映射", description = "字典映射")
    @PostMapping("/dict/values-map")
    fun dictValuesMap(): DictValuesMapResp =
        dictService.dictValuesMap(WebContext.appId)

    @Operation(summary = "新增字典", description = "新增字典")
    @PostMapping("/dict/add")
    fun addDict(
        @Validated
        @RequestBody
        req: DictAddReq,
    ) = dictService.addDict(req)

    @Operation(summary = "更新字典", description = "更新字典")
    @PostMapping("/dict/update")
    fun updateDict(
        @Validated
        @RequestBody
        req: DictUpdateReq,
    ) = dictService.updateDict(req)

    @Operation(summary = "删除字典", description = "删除字典")
    @PostMapping("/dict/delete")
    fun deleteDict(
        @Validated
        @RequestBody
        req: DictDeleteReq,
    ) = dictService.deleteDict(req)

    @Operation(summary = "字典类型树", description = "字典类型树")
    @PostMapping("/dict-type/tree")
    fun dictTypeTree(
        @RequestBody
        req: DictTypeQuery,
    ): List<DictTypeResp> =
        dictService.dictTypeTree(req)

    @Operation(summary = "新增字典类型", description = "新增字典类型")
    @PostMapping("/dict-type/add")
    fun addDictType(
        @Validated
        @RequestBody
        req: DictTypeAddReq,
    ) = dictService.addDictType(req)

    @Operation(summary = "更新典类型", description = "更新典类型")
    @PostMapping("/dict-type/update")
    fun updateDictType(
        @Validated
        @RequestBody
        req: DictTypeUpdateReq,
    ) = dictService.updateDictType(req)

    @Operation(summary = "删除字典类型", description = "删除字典类型")
    @PostMapping("/dict-type/delete")
    fun delete(
        @Validated
        @RequestBody
        req: DictTypeDeleteReq,
    ) = dictService.delete(req)
}
