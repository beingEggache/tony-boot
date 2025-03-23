package com.tony.demo.sys.dto.req

import com.tony.demo.annotation.TenantIdInject
import com.tony.demo.sys.dto.enums.ModuleType
import com.tony.demo.trait.TreeLike
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

/**
 * 模块请求
 * @author tangli
 * @date 2024/07/05 11:30
 * @since 1.0.0
 */
@Schema(description = "模块请求")
data class ModuleSubmitReq(
    @get:NotBlank(message = "id不能为空")
    @param:Schema(description = "id", required = true)
    val moduleId: String = "",
    @param:Schema(description = "上级id")
    val parentModuleId: String = "",
    /**
     * 名称
     */
    @get:NotBlank(message = "名称不能为空")
    @param:Schema(description = "名称", required = true)
    val moduleName: String = "",
    /**
     * 编码
     */
    @get:NotBlank(message = "编码不能为空")
    @param:Schema(description = "编码", required = true)
    val moduleCode: String = "",
    /**
     * 编码序列
     */
    @get:NotBlank(message = "编码序列不能为空")
    @param:Schema(description = "编码序列", required = true)
    val moduleCodeSeq: String = "",
    /**
     * 模块值（接口URL，前端路由，前端组件名）
     */
    @get:NotBlank(message = "模块值不能为空")
    @param:Schema(description = "模块值（接口URL，前端路由，前端组件名）", required = true)
    val moduleValue: String = "",
    /**
     * 模块类型（1：接口，2：前端路由，3：前端组件）
     */
    @get:NotNull(message = "模块类型")
    @param:Schema(description = "模块类型（1：接口，2：前端路由，3：前端组件）", required = true)
    val moduleType: ModuleType = ModuleType.ROUTE,
    /**
     * 备注
     */
    val remark: String = "",
    /**
     * 状态：1-启用，0-禁用
     */
    val enabled: Boolean = true,
    /**
     * 子模块
     */
    override val children: List<ModuleSubmitReq> = mutableListOf(),
    @get:TenantIdInject
    @param:Schema(hidden = true)
    val tenantId: String = "",
) : TreeLike<ModuleSubmitReq>
