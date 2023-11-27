/**
 * clients
 *
 * TODO
 *
 * @author Tang Li
 * @date 2021/12/27 9:31
 */
package com.tony.test.feign.module.enums.client

import com.tony.annotation.web.auth.NoLoginCheck
import com.tony.test.feign.module.enums.controller.EnumTest
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "feignTestEnumClient", url = "http://localhost:10001")
interface FeignTestEnumClient {
    @NoLoginCheck
    @GetMapping("/test-int-enum")
    fun testIntEnum(
        @RequestParam
        myIntEnum: Int
    )

    @NoLoginCheck
    @GetMapping("/test-string-enum")
    fun testStringEnum(
        @RequestParam
        myStringEnum: String
    )

    @NoLoginCheck
    @PostMapping("/test-post-enum")
    fun testPostEnum(
        @RequestBody
        enumTest: EnumTest
    )
}
