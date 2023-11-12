/**
 * clients
 *
 * TODO
 *
 * @author Tang Li
 * @date 2021/12/27 9:31
 */
package com.tony.test.feign.enums.client

import com.tony.test.feign.enums.controller.EnumTest
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "openFeignTestEnumClient", url = "http://localhost:9090")
interface OpenFeignTestEnumClient {
    @GetMapping("/test-int-enum")
    fun testIntEnum(
        @RequestParam
        myIntEnum: Int
    )

    @GetMapping("/test-string-enum")
    fun testStringEnum(
        @RequestParam
        myStringEnum: String
    )

    @PostMapping("/test-post-enum")
    fun testPostEnum(
        @RequestBody
        enumTest: EnumTest
    )
}
