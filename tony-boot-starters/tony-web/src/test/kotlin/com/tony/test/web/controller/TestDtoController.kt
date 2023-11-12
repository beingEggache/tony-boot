package com.tony.test.web.controller

import com.tony.PageQuery
import com.tony.flattenResult
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

/**
 * TestDtoController is
 * @author Tang Li
 * @date 2023/07/11 09:11
 */
@Tag(name = "测试对象")
@RestController
class TestDtoController {

    @Operation(description = "dto")
    @PostMapping("/page-query-dto")
    fun testPageQuery(
        @Validated
        @RequestBody
        req: PageQuery<String>
    ) = req

    @Operation(description = "dto")
    @PostMapping("/page-query-flatten-api-result")
    fun testFlattenApiResult(
        @Validated
        @RequestBody
        req: PageQuery<String>
    ) = req.flattenResult()
}
