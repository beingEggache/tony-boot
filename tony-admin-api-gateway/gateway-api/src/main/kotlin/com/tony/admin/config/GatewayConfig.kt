/**
 *
 * @author tangli
 * @since 2021/8/5 14:29
 */
package com.tony.admin.config

import com.alibaba.nacos.api.annotation.NacosProperties
import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties
import com.quzhu.core.utils.defaultIfBlank
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import org.springframework.util.AntPathMatcher
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

@ConfigurationProperties(prefix = "quzhu.admin.gateway")
@Component
@RefreshScope
@NacosConfigurationProperties(
    properties = NacosProperties(serverAddr = "nacos.tony.com:8848"),
    dataId = "tony-admin-gateway.yml",
    prefix = "tony.admin.gateway"
)
class GatewayRouteConfigProperties {

    var noLoginCheckUrls: List<String>? = null
    var noPermissionCheckUrls: List<String>? = null

    private val antPathMatcher = AntPathMatcher()

    private fun AntPathMatcher.matchAny(patterns: List<String>?, path: String?) =
        patterns?.any { match(it, path.defaultIfBlank()) } ?: false

    fun noLoginCheck(path: String?) = antPathMatcher.matchAny(noLoginCheckUrls, path)

    fun noPermissionCheck(path: String?) = if (noLoginCheck(path)) true
    else antPathMatcher.matchAny(noPermissionCheckUrls, path)
}
