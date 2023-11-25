/**
 * OpenFeignFileTest
 *
 * TODO
 *
 * @author Tang Li
 * @date 2021/12/7 14:40
 */
package com.tony.test.feign.module.enums

import com.tony.test.feign.module.enums.client.FeignTestEnumClient
import com.tony.test.feign.module.enums.controller.EnumTest
import jakarta.annotation.Resource
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(
    properties = ["server.port=9090"],
    classes = [FeignTestEnumApp::class],
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
)
class FeignEnumTest {

    @Resource
    lateinit var openFeignTestEnumClient: FeignTestEnumClient

    @Test
    fun testEnum() {
        openFeignTestEnumClient.testIntEnum(1)
        openFeignTestEnumClient.testStringEnum("yes")
        openFeignTestEnumClient.testPostEnum(EnumTest().apply {
            myIntEnum = 1
            myStringEnum = "yes"
        })
    }

}
