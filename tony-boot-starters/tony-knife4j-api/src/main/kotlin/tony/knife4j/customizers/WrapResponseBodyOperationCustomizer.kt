package tony.knife4j.customizers

import io.swagger.v3.core.converter.AnnotatedType
import io.swagger.v3.core.converter.ModelConverters
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.media.Content
import io.swagger.v3.oas.models.media.Schema
import java.lang.reflect.Type
import java.util.Optional
import kotlin.jvm.java
import org.slf4j.Logger
import org.springdoc.core.customizers.GlobalOperationComponentsCustomizer
import org.springframework.core.PriorityOrdered
import org.springframework.core.ResolvableType
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.method.HandlerMethod
import tony.core.SpringContexts
import tony.core.model.ApiResultLike
import tony.core.model.MonoResultLike
import tony.core.model.RowsLike
import tony.core.utils.antPathMatchAny
import tony.core.utils.asTo
import tony.core.utils.getLogger
import tony.core.utils.isArrayType
import tony.core.utils.isSimpleType
import tony.core.utils.isTypesOrSubTypesOf
import tony.core.utils.isVoidLikeType
import tony.core.utils.rawClass
import tony.knife4j.utils.SchemaParameterizedType
import tony.knife4j.utils.isDownloadType
import tony.knife4j.utils.typeParam

/**
 * 包装响应 Schema
 * @author tangli
 * @date 2025/07/23 11:40
 */
internal class WrapResponseBodyOperationCustomizer(
    private val defaultResponseName: String = "200",
) : GlobalOperationComponentsCustomizer,
    PriorityOrdered {
    private val logger: Logger = getLogger()

    override fun customize(
        operation: Operation,
        components: Components,
        handlerMethod: HandlerMethod,
    ): Operation {
        if (isInWrapResponseExcludePatterns(handlerMethod)) {
            return operation
        }

        val response = operation.responses.get(defaultResponseName) ?: return operation
        val type = ResolvableType.forMethodReturnType(handlerMethod.method).type
        val returnType =
            if (type.isTypesOrSubTypesOf(
                    Optional::class.java,
                    ResponseEntity::class.java
                )
            ) {
                type.typeParam(0)
            } else {
                type
            }

        if (
            (response.content == null && !returnType.isVoidLikeType()) ||
            returnType.isTypesOrSubTypesOf(ApiResultLike::class.java) ||
            returnType.isSimpleType() ||
            isDownloadType(returnType)
        ) {
            return operation
        }

        if (returnType.isVoidLikeType()) {
            response.content =
                Content().addMediaType(
                    MediaType.APPLICATION_JSON_VALUE,
                    io.swagger.v3.oas.models.media
                        .MediaType()
                )
        }

        val mediaType = response.content[MediaType.APPLICATION_JSON_VALUE] ?: return operation
        val parameterizedType =
            if (returnType.isVoidLikeType()) {
                SchemaParameterizedType(
                    ApiResultLike::class.java,
                    Void::class.java
                )
            } else if (returnType.isTypesOrSubTypesOf(MonoResultLike::class.java)) {
                SchemaParameterizedType(
                    ApiResultLike::class.java,
                    SchemaParameterizedType(MonoResultLike::class.java, returnType.typeParam(0))
                )
            } else if (returnType.isTypesOrSubTypesOf(Collection::class.java)) {
                SchemaParameterizedType(
                    ApiResultLike::class.java,
                    SchemaParameterizedType(RowsLike::class.java, returnType.typeParam(0))
                )
            } else if (returnType.isArrayType()) {
                SchemaParameterizedType(
                    ApiResultLike::class.java,
                    SchemaParameterizedType(
                        RowsLike::class.java,
                        returnType.rawClass().componentType
                    )
                )
            } else {
                SchemaParameterizedType(
                    ApiResultLike::class.java,
                    returnType
                )
            }
        val newSchema = generateSchema(parameterizedType, components) ?: return operation
        mediaType.schema =
            Schema<Any>().apply {
                `$ref` = "#/components/schemas/${newSchema.name}"
            }

        return operation
    }

    override fun customize(
        operation: Operation,
        handlerMethod: HandlerMethod,
    ): Operation {
        TODO()
    }

    private fun generateSchema(
        apiResultType: Type,
        components: Components,
    ): Schema<*>? {
        val resolvedSchema =
            ModelConverters
                .getInstance()
                .resolveAsResolvedSchema(AnnotatedType(apiResultType))
        if (resolvedSchema == null) {
            logger.warn("Unable to resolve schema for $apiResultType")
        }

        resolvedSchema?.referencedSchemas?.forEach { (name, schema) ->
            components.schemas.putIfAbsent(name, schema)
        }

        return resolvedSchema?.schema
    }

    private fun isInWrapResponseExcludePatterns(handlerMethod: HandlerMethod): Boolean {
        val requestMapping = handlerMethod.getMethodAnnotation(RequestMapping::class.java)
        val wrapResponseExcludePatterns =
            SpringContexts
                .Env
                .getProperty("dynamic.wrapResponseExcludePatterns", Set::class.java)
                .asTo<Set<String>>()
        val isInWrapResponseExcludePatterns =
            requestMapping?.path?.any { it.antPathMatchAny(wrapResponseExcludePatterns) } == true
        return isInWrapResponseExcludePatterns
    }

    override fun getOrder(): Int =
        PriorityOrdered.LOWEST_PRECEDENCE
}
