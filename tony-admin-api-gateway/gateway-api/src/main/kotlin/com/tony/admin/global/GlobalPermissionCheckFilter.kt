/**
 *
 * @author tangli
 * @since 2021/8/5 14:29
 */
package com.tony.admin.global

import com.tony.admin.config.GatewayRouteConfigProperties
import com.tony.admin.utils.jsonBody
import com.tony.core.ApiResult
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.core.Ordered
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class GlobalPermissionCheckFilter(
    private val gatewayRouteConfigProperties: GatewayRouteConfigProperties
) : GlobalFilter, Ordered {
    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        val originalPath = exchange.request.uri.path

        if (gatewayRouteConfigProperties.noPermissionCheck(originalPath)) {
            return chain.filter(exchange)
        }
        // TODO check permission
        return exchange.response.jsonBody(ApiResult("未经许可的访问", 40300))
    }

    override fun getOrder(): Int = 10152
}
