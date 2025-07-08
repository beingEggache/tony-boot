package tony.demo.sys.dto.query

import io.swagger.v3.oas.annotations.media.Schema
import tony.demo.annotation.AppIdInject
import tony.demo.sys.dto.enums.ModuleType

/**
 * ModuleQuery is
 * @author tangli
 * @date 2024/07/08 11:06
 */
@Schema(description = "模块查询")
data class ModuleQuery(
    val moduleTypes: Collection<ModuleType> = listOf(ModuleType.ROUTE, ModuleType.COMPONENT),
    @get:AppIdInject
    @param:Schema(hidden = true)
    val appId: String = "",
)
