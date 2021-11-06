/**
 *
 * @author tangli
 * @since 2021/8/5 14:29
 */
package com.tony.admin.global

import com.tony.ApiProperty
import com.tony.ApiResult
import com.tony.ApiResult.Companion.EMPTY_RESULT
import com.tony.admin.utils.jsonBody
import com.tony.utils.getLogger
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler
import org.springframework.core.annotation.Order
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Order(-1)
@Component
class GlobalExceptionHandler : ErrorWebExceptionHandler {

    private val logger = getLogger()

    override fun handle(exchange: ServerWebExchange, ex: Throwable): Mono<Void> {
        logger.error(ex.localizedMessage, ex)
        val response = exchange.response
        if (response.isCommitted) {
            return Mono.error(ex)
        }

        response.headers.contentType = MediaType.APPLICATION_JSON
        return if (ex is ResponseStatusException) {
            val httpStatus = ex.status
            response.jsonBody(
                ApiResult(
                    EMPTY_RESULT,
                    httpStatus.value() * 100,
                    httpStatus.reasonPhrase
                )
            )
        } else response.jsonBody(
            ApiResult(
                EMPTY_RESULT,
                ApiProperty.errorCode,
                "访客太多，请稍后再试"
            )
        )
    }
}
