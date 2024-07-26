package com.tony.dto.resp

import com.fasterxml.jackson.annotation.JsonIgnore
import com.tony.dto.trait.ForestLike
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

/**
 * 部门树形响应
 * @author tangli
 * @date 2024/07/03 13:18
 * @since 1.0.0
 */
@Schema(description = "部门树形响应")
data class DeptResp(
    @Schema(description = "id")
    val deptId: String,
    /**
     * 上级部门id
     */
    @Schema(description = "上级id")
    val parentDeptId: String,
    /**
     * 部门名
     */
    @Schema(description = "名称")
    val deptName: String,
    /**
     * 部门编码
     */
    @Schema(description = "编码")
    val deptCode: String,
    /**
     * 部门编码序列
     */
    @Schema(description = "编码序列")
    val deptCodeSeq: String,
    /**
     * 排序
     */
    @get:JsonIgnore(false)
    @Schema(description = "排序")
    override val sort: Int,
    /**
     * 备注
     */
    @Schema(description = "备注")
    val remark: String,
    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    val createTime: LocalDateTime,
    /**
     * 状态：1-启用，0-禁用
     */
    @Schema(description = "状态：1-启用，0-禁用")
    val enabled: Boolean? = null,
) : ForestLike<DeptResp> {
    override val code: String
        get() = deptCodeSeq
    override var children: MutableList<DeptResp> = mutableListOf()
}
