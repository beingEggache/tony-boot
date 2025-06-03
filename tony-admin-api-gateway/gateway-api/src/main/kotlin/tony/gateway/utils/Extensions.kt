/**
 *
 * @author tangli
 * @date 2021/8/5 14:29
 */
package tony.gateway.utils

import org.springframework.http.MediaType
import org.springframework.http.server.reactive.ServerHttpResponse
import reactor.core.publisher.Mono
import tony.utils.globalObjectMapper

fun ServerHttpResponse.jsonBody(obj: Any?): Mono<Void> =
    run {
        headers.contentType = MediaType.APPLICATION_JSON
        writeWith(
            Mono.fromSupplier {
                bufferFactory().wrap(globalObjectMapper.writeValueAsBytes(obj))
            }
        )
    }
