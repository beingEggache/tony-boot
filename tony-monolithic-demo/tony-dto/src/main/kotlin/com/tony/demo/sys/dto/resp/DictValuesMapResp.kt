package com.tony.demo.sys.dto.resp

import io.swagger.v3.oas.annotations.media.Schema

/**
 * DictValuesResp is
 * @author tangli
 * @date 2024/07/30 09:37
 * @since 1.0.0
 */
@Schema(description = "字典值")
data class DictValuesMapResp(
    @param:Schema(description = "值映射")
    val dictValuesMap: Map<String, Map<String, DictResp>>,
)
