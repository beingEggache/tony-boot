/**
 *
 * @author tangli
 * @date 2021/8/5 14:29
 */
package tony.gateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import tony.annotation.EnableTonyBoot

fun main(args: Array<String>) {
    runApplication<GatewayWebApp>(*args)
}

@EnableDiscoveryClient
@EnableTonyBoot
@SpringBootApplication
class GatewayWebApp
