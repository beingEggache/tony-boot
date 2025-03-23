package com.tony.demo.sys.dto.req

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

/**
 * 新增字典请求
 * @author tangli
 * @date 2024/07/26 11:47
 * @since 1.0.0
 */
@Schema(description = "新增字典请求")
data class DictAddReq(
    @param:Schema(description = "字典类型id", required = true)
    @get:NotBlank(message = "请选择字典类型")
    val dictTypeId: String = "",
    @param:Schema(description = "名称", required = true)
    @get:NotBlank(message = "请输入名称")
    val dictName: String = "",
    @param:Schema(description = "编码", required = true)
    @get:NotBlank(message = "请输入编码")
    val dictCode: String = "",
    @param:Schema(description = "值", required = true)
    @get:NotBlank(message = "请输入名称")
    val dictValue: String = "",
    @param:Schema(description = "meta", required = true)
    val dictMeta: String = "{}",
    @param:Schema(description = "排序", required = true)
    val sort: Int = -1,
    @param:Schema(description = "备注")
    val remark: String = "",
)
