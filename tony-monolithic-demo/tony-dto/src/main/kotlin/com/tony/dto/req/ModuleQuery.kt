package com.tony.dto.req

import com.tony.annotation.AppIdInject
import com.tony.dto.enums.ModuleType
import io.swagger.v3.oas.annotations.media.Schema

/**
 * ModuleQuery is
 * @author tangli
 * @date 2024/07/08 11:06
 * @since 1.0.0
 */
@Schema(description = "模块查询")
data class ModuleQuery(
    val moduleTypes: Collection<ModuleType> = listOf(ModuleType.ROUTE, ModuleType.COMPONENT),
    @AppIdInject
    @Schema(hidden = true)
    val appId: String = "",
)
