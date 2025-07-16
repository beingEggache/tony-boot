/**
 *
 * @author tangli
 * @date 2021/8/5 14:29
 */
package tony.gateway.global

import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import tony.core.ApiProperty
import tony.core.model.ApiResult
import tony.core.utils.getLogger
import tony.gateway.utils.jsonBody

@Order(-1)
@Component
class GlobalExceptionHandler : ErrorWebExceptionHandler {
    private val logger = getLogger()

    override fun handle(
        exchange: ServerWebExchange,
        ex: Throwable,
    ): Mono<Void> {
        logger.error(ex.localizedMessage, ex)
        val response = exchange.response
        if (response.isCommitted) {
            return Mono.error(ex)
        }
        return if (ex is ResponseStatusException) {
            val httpStatus = ex.statusCode
            response.statusCode = httpStatus
            response.jsonBody(
                ApiResult(
                    Unit,
                    httpStatus.value(),
                    ex.reason ?: ""
                )
            )
        } else {
            response.jsonBody(
                ApiResult(
                    Unit,
                    ApiProperty.errorCode,
                    ApiProperty.errorMsg
                )
            )
        }
    }
}
