/**
 *
 * @author tangli
 * @date 2021/8/5 14:29
 */
package com.tony.gateway.filter

import com.tony.ApiResult
import com.tony.gateway.config.GatewayRouteConfigProperties
import com.tony.gateway.utils.jsonBody
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.core.Ordered
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class GlobalPermissionCheckFilter(
    private val gatewayRouteConfigProperties: GatewayRouteConfigProperties,
) : GlobalFilter,
    Ordered {
    override fun filter(
        exchange: ServerWebExchange,
        chain: GatewayFilterChain,
    ): Mono<Void> {
        val originalPath = exchange.request.uri.path

        if (gatewayRouteConfigProperties.noPermissionCheck(originalPath)) {
            return chain.filter(exchange)
        }
        // TODO check permission
        return exchange.response.jsonBody(ApiResult(Unit, 40300, "未经许可的访问"))
    }

    override fun getOrder(): Int =
        10152
}
