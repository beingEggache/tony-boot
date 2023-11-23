/**
 * OpenFeignTestFileApp
 *
 * TODO
 *
 * @author Tang Li
 * @date 2021/12/27 11:39
 */
package com.tony.test.feign.module.file

import com.tony.annotation.EnableTonyBoot
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@EnableFeignClients
@SpringBootApplication
@EnableTonyBoot
class FeignFileTestApp
