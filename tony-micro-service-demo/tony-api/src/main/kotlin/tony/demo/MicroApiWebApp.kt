package tony.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import tony.core.annotation.EnableTonyBoot

fun main(args: Array<String>) {
    runApplication<MicroApiWebApp>(*args) {
        setHeadless(true)
    }
}

@EnableDiscoveryClient
@EnableTonyBoot
@SpringBootApplication
class MicroApiWebApp
