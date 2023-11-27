/**
 * OpenFeignTestFileApp
 *
 * TODO
 *
 * @author Tang Li
 * @date 2021/12/27 11:39
 */
package com.tony.test.feign.module.enums

import com.tony.annotation.EnableTonyBoot
import com.tony.test.feign.config.FeignTestConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Import

@Import(FeignTestConfig::class)
@EnableFeignClients
@SpringBootApplication
@EnableTonyBoot
class FeignTestEnumApp
