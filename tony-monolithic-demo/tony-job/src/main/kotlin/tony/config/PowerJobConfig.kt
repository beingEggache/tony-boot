package tony.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import tony.core.misc.YamlPropertySourceFactory

/**
 * PowerJobConfig is
 * @author tangli
 * @date 2024/01/23 10:40
 */
@ComponentScan("tony.job")
@PropertySource("classpath:powerjob.config.yml", factory = YamlPropertySourceFactory::class)
@Configuration(proxyBeanMethods = false)
class PowerJobConfig
