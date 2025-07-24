package tony.knife4j.customizers

import io.swagger.v3.core.converter.AnnotatedType
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.parameters.Parameter
import org.springdoc.core.customizers.GlobalOperationCustomizer
import org.springdoc.core.customizers.ParameterCustomizer
import org.springdoc.core.customizers.PropertyCustomizer
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.web.method.HandlerMethod
import tony.core.enums.EnumValue
import tony.core.enums.IntEnumValue
import tony.core.enums.StringEnumValue
import tony.core.enums.enumCreatorOf
import tony.core.utils.asTo
import tony.core.utils.isTypesOrSubTypesOf
import tony.core.utils.toJavaType

/**
 * 统一枚举值[tony.core.enums.EnumValue] 参数定制器
 * @author tangli
 * @date 2025/07/21 14:52
 */
internal class EnumValueCustomizer(
    private val defaultResponseName: String = "200",
) : ParameterCustomizer,
    PropertyCustomizer,
    GlobalOperationCustomizer {
    override fun customize(
        parameterModel: Parameter,
        methodParameter: MethodParameter,
    ): Parameter {
        if (methodParameter.parameterType.isTypesOrSubTypesOf(EnumValue::class.java)) {
            parameterModel.schema.enum = enumCreatorOf(methodParameter.parameterType).enumValues()
        }
        return parameterModel
    }

    override fun customize(
        property: Schema<*>,
        type: AnnotatedType,
    ): Schema<*> {
        val clazz = type.type.toJavaType().rawClass
        if (clazz.isTypesOrSubTypesOf(IntEnumValue::class.java)) {
            property.types = setOfNotNull("integer")
            property.enum = enumCreatorOf(clazz).enumValues().asTo()
        } else if (clazz.isTypesOrSubTypesOf(StringEnumValue::class.java)) {
            property.types = setOfNotNull("string")
            property.enum = enumCreatorOf(clazz).enumValues().asTo()
        }
        return property
    }

    override fun customize(
        operation: Operation,
        handlerMethod: HandlerMethod,
    ): Operation {
        val returnClass = handlerMethod.returnType.parameterType
        if (returnClass.isTypesOrSubTypesOf(IntEnumValue::class.java)) {
            operation
                .responses[defaultResponseName]
                ?.content[MediaType.APPLICATION_JSON_VALUE]
                ?.schema
                ?.apply {
                    types = setOfNotNull("integer")
                    enum = enumCreatorOf(returnClass).enumValues().asTo()
                }
        } else if (returnClass.isTypesOrSubTypesOf(StringEnumValue::class.java)) {
            operation
                .responses[defaultResponseName]
                ?.content[MediaType.APPLICATION_JSON_VALUE]
                ?.schema
                ?.apply {
                    types = setOfNotNull("string")
                    enum = enumCreatorOf(returnClass).enumValues().asTo()
                }
        }
        return operation
    }
}
