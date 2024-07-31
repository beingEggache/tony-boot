package com.tony.demo.sys.dto.req

import io.swagger.v3.oas.annotations.media.Schema

/**
 * 字典类型查询
 * @author tangli
 * @date 2024/07/03 13:28
 * @since 1.0.0
 */
@Schema(description = "字典类型查询")
data class DictTypeQuery(
    val dictTypeName: String = "",
    val excludeDictTypeIds: Set<String> = emptySet(),
)
