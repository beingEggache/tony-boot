/**
 *
 * @author tangli
 * @since 2021/8/5 14:29
 */
package com.tony.gateway.utils

import com.tony.utils.OBJECT_MAPPER
import reactor.core.publisher.Mono

import org.springframework.http.MediaType
import org.springframework.http.server.reactive.ServerHttpResponse

fun ServerHttpResponse.jsonBody(obj: Any?): Mono<Void> = run {
    headers.contentType = MediaType.APPLICATION_JSON
    writeWith(
        Mono.fromSupplier {
            bufferFactory().wrap(OBJECT_MAPPER.writeValueAsBytes(obj))
        }
    )
}
