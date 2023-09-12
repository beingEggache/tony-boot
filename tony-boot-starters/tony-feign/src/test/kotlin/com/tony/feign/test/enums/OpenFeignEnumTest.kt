/**
 * OpenFeignFileTest
 *
 * TODO
 *
 * @author Tang Li
 * @date 2021/12/7 14:40
 */
package com.tony.feign.test.enums

import com.tony.feign.test.enums.client.OpenFeignTestEnumClient
import com.tony.feign.test.enums.controller.EnumTest
import jakarta.annotation.Resource
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [OpenFeignTestEnumApp::class], webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class OpenFeignEnumTest {

    @Resource
    lateinit var openFeignTestEnumClient: OpenFeignTestEnumClient

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
