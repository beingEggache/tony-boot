package tony.knife4j.customizers

import io.swagger.v3.oas.models.OpenAPI
import org.springdoc.core.customizers.OpenApiCustomizer

/**
 * 扁平化属性 OpenApiCustomizer
 * @author tangli
 * @date 2025/07/21 11:41
 */
public class FlattenPropertiesOpenApiCustomizer : OpenApiCustomizer {
    override fun customise(openApi: OpenAPI) {
        openApi.components?.schemas?.forEach { (_, schema) ->
            schema.properties
                ?.filterValues { propertySchema ->
                    propertySchema.`$ref`.isNullOrEmpty() && propertySchema.types.isNullOrEmpty()
                }?.keys
                ?.forEach { key ->
                    schema.properties?.remove(key)
                }
        }
    }
}
