package com.tony.gateway.filter.factory

import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.cloud.gateway.support.GatewayToStringStyler
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

/**
 * RemoveResponseHeadersGatewayFilterFactories is
 * @author tangli
 * @date 2023/07/20 19:20
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
class RemoveResponseHeadersGatewayFilterFactory :
    AbstractGatewayFilterFactory<RemoveHeaders>(RemoveHeaders::class.java) {
    override fun apply(config: RemoveHeaders): GatewayFilter =
        object : GatewayFilter {
            override fun filter(
                exchange: ServerWebExchange,
                chain: GatewayFilterChain,
            ): Mono<Void> =
                chain
                    .filter(exchange)
                    .then(
                        Mono.fromRunnable {
                            val response = exchange.response
                            if (!response.isCommitted) {
                                config.headers.forEach { response.headers.remove(it) }
                            }
                        }
                    )

            override fun toString(): String =
                GatewayToStringStyler
                    .filterToStringCreator(this@RemoveResponseHeadersGatewayFilterFactory)
                    .append("headers", config.headers)
                    .toString()
        }
}

data class RemoveHeaders(
    val headers: List<String>,
)
