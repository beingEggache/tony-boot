package com.tony.config

import com.tony.misc.YamlPropertySourceFactory
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

/**
 * PowerJobConfig is
 * @author tangli
 * @date 2024/01/23 10:40
 * @since 1.0.0
 */
@PropertySource("classpath:powerjob.config.yml", factory = YamlPropertySourceFactory::class)
@ComponentScan("com.tony.job")
@Configuration
class PowerJobConfig
