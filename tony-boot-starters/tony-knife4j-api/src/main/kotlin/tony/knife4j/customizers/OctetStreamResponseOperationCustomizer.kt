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
