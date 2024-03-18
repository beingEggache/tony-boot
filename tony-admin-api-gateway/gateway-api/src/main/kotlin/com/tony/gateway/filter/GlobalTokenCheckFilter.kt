/**
 *
 * @author tangli
 * @date 2021/8/5 14:29
 */
package com.tony.gateway.filter

import com.auth0.jwt.exceptions.JWTVerificationException
import com.tony.ApiProperty
import com.tony.ApiResult
import com.tony.gateway.TOKEN_HEADER_NAME
import com.tony.gateway.USER_ID_HEADER_NAME
import com.tony.gateway.config.GatewayRouteConfigProperties
import com.tony.gateway.utils.jsonBody
import com.tony.jwt.JwtToken
import com.tony.utils.ifNullOrBlank
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.core.Ordered
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class GlobalTokenCheckFilter(
    private val gatewayRouteConfigProperties: GatewayRouteConfigProperties,
) : GlobalFilter,
    Ordered {
    override fun filter(
        exchange: ServerWebExchange,
        chain: GatewayFilterChain,
    ): Mono<Void> {
        val originalPath = exchange.request.uri.path
        if (gatewayRouteConfigProperties.noLoginCheck(originalPath)) {
            return chain.filter(exchange)
        }

        val request = exchange.request
        val token =
            try {
                JwtToken.parse(request.headers.getFirst(TOKEN_HEADER_NAME).ifNullOrBlank())
            } catch (e: JWTVerificationException) {
                null
            } ?: return exchange.response.jsonBody(ApiResult(Unit, ApiProperty.unauthorizedCode, "请登录"))
        val mutReq = request.mutate().header(USER_ID_HEADER_NAME, token.getClaim("userId").asString()).build()
        return chain.filter(exchange.mutate().request(mutReq).build())
    }

    override fun getOrder(): Int =
        10151
}
