package tony.knife4j.customizers

import io.swagger.v3.oas.models.Operation
import org.springdoc.core.customizers.GlobalOperationCustomizer
import org.springframework.core.ResolvableType
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.method.HandlerMethod
import tony.core.utils.isTypesOrSubTypesOf
import tony.knife4j.utils.isDownloadType
import tony.knife4j.utils.typeParam

/**
 * OctetStreamResponseOperationCustomizer is
 * @author tangli
 * @date 2025/07/24 14:14
 */
internal class OctetStreamResponseOperationCustomizer : GlobalOperationCustomizer {
    override fun customize(
        operation: Operation,
        handlerMethod: HandlerMethod,
    ): Operation {
        val response = operation.responses["200"] ?: return operation
        val type = ResolvableType.forMethodReturnType(handlerMethod.method).type
        val returnType =
            if (type.isTypesOrSubTypesOf(ResponseEntity::class.java)) {
                type.typeParam(0)
            } else {
                type
            }
        if (!isDownloadType(returnType)) {
            return operation
        }
        val requestMapping = handlerMethod.getMethodAnnotation(RequestMapping::class.java)
        val produce = requestMapping?.produces?.firstOrNull()
        if (!produce.isNullOrEmpty()) {
            return operation
        }
        val originMediaType = response.content[MediaType.APPLICATION_JSON_VALUE]
        response.content.clear()
        response.content.put(MediaType.APPLICATION_OCTET_STREAM_VALUE, originMediaType)
        return operation
    }
}
