/**
 *
 * @author tangli
 * @since 2021/8/5 14:29
 */
package com.tony.admin.global

import com.auth0.jwt.exceptions.JWTVerificationException
import com.tony.core.ApiResult
import com.tony.core.utils.defaultIfBlank
import com.tony.jwt.config.JwtToken
import com.tony.admin.TokenHeaderName
import com.tony.admin.UserIdHeaderName
import com.tony.admin.config.GatewayRouteConfigProperties
import com.tony.admin.utils.jsonBody
import com.tony.core.ApiProperty
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.core.Ordered
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class GlobalTokenCheckFilter(
    private val gatewayRouteConfigProperties: GatewayRouteConfigProperties
) : GlobalFilter, Ordered {
    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        val originalPath = exchange.request.uri.path
        if (gatewayRouteConfigProperties.noLoginCheck(originalPath)) {
            return chain.filter(exchange)
        }

        val request = exchange.request
        val token = try {
            JwtToken.parse(request.headers.getFirst(TokenHeaderName).defaultIfBlank())
        } catch (e: JWTVerificationException) {
            null
        } ?: return exchange.response.jsonBody(ApiResult("请登录", ApiProperty.unauthorizedCode))
        val mutReq = request.mutate().header(UserIdHeaderName, token.getClaim("userId").asString()).build()
        return chain.filter(exchange.mutate().request(mutReq).build())
    }

    override fun getOrder(): Int = 10151
}
