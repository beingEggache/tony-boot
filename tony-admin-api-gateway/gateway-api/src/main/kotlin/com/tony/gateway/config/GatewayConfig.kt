/**
 *
 * @author tangli
 * @since 2021/8/5 14:29
 */
package com.tony.gateway.config

import com.alibaba.nacos.api.annotation.NacosProperties
import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties
import com.tony.utils.antPathMatchAny
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

/**
 * 限流配置
 */
@Configuration
class GatewayConfig {

    /**
     * 根据IP做限流 , 在配置文件filter处加
     *
     *     name: RequestRateLimiter
     *     args:
     *      key-resolver: "#{@ipKeyResolver}"
     *      redis-rate-limiter.replenishRate: 1  #令牌桶每秒填充平均速率
     *      redis-rate-limiter.burstCapacity: 1  #令牌桶总容量
     */
    @Bean
    fun ipKeyResolver() = KeyResolver { exchange ->
        Mono.just(exchange.request.remoteAddress?.address?.hostAddress ?: "")
    }
}

@ConfigurationProperties(prefix = "tony.gateway")
@Component
@RefreshScope
@NacosConfigurationProperties(
    properties = NacosProperties(serverAddr = "\${spring.cloud.nacos.discovery.server-addr}"),
    dataId = "tony-gateway-routes-auth.yml",
    prefix = "tony.gateway"
)
class GatewayRouteConfigProperties {

    var noLoginCheckUrls: List<String>? = null
    var noPermissionCheckUrls: List<String>? = null

    fun noLoginCheck(path: String?) =
        path.antPathMatchAny(noLoginCheckUrls)

    fun noPermissionCheck(path: String?) = if (noLoginCheck(path)) true
    else path.antPathMatchAny(noPermissionCheckUrls)
}
