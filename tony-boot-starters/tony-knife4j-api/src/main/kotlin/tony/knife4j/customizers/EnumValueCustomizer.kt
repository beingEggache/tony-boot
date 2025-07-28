/*
 * MIT License
 *
 * Copyright (c) 2023-present, tangli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
