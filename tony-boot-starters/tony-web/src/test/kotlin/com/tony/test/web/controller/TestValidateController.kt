/*
 * MIT License
 *
 * Copyright (c) 2023-present, tangli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.tony.test.web.controller

import com.tony.PageQuery
import com.tony.codec.enums.Encoding
import com.tony.enums.validate.SimpleStringEnum
import com.tony.test.web.req.TestPatternReq
import com.tony.test.web.req.TestQuery
import com.tony.test.web.req.TestReq
import com.tony.test.web.service.TestValidateService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import org.springdoc.core.annotations.ParameterObject
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@Tag(name = "测试验证")
@RestController
class TestValidateController(
    private val testValidateService: TestValidateService,
) {

    @Operation(summary = "pattern验证", description = "pattern验证")
    @PostMapping("/pattern-validate")
    fun testLoginCheck(@Validated @RequestBody req: TestPatternReq) = req.mobile

    @Operation(summary = "pageQuery 验证", description = "pageQuery 验证")
    @PostMapping("/page-query-validate")
    fun testPageQueryValidation(
        @Validated @RequestBody req: PageQuery<TestQuery>
    ) = "req.query"

    @Operation(description = "string-enum-validate")
    @PostMapping("/string-enum-validate")
    fun stringEnumValidate(
        @RequestParam
        @SimpleStringEnum(enums = ["BASE64"], message = "不对", required = true)
        testStringEnum: Encoding
    ): Encoding = Encoding.BASE64

    @Operation(description = "validate-form")
    @PostMapping("/validate-form")
    fun validateForm(
        @Validated
        @ParameterObject
        testReq: TestReq
    ): TestReq = testReq

    @Operation(description = "validate-request-params")
    @PostMapping("/validate-request-params")
    fun validateRequestParams(
        @Positive(message = "自然数")
        age: Int?,
        @NotBlank(message = "不能为空")
        name: String?,
    ) = Unit

    @Operation(description = "validate-service-method-parameter1")
    @PostMapping("/validate-service-method-parameter1")
    fun validateServiceMethodParameter1(
        @RequestParam
        list: List<String>?,
        age: Int?
    ) {
        testValidateService.validateServiceMethodParameter(list, age)
    }

    @Operation(description = "validate-service-method-parameter2")
    @PostMapping("/validate-service-method-parameter2")
    fun validateServiceMethodParameter2(
        array: Array<String>?,
        future: LocalDate?
    ) {
        testValidateService.validateServiceMethodParameter(array, future)
    }

    @Operation(description = "validate-service-method-obj-parameter")
    @PostMapping("/validate-service-method-obj-parameter")
    fun validateServiceMethodObjParameter(
        @RequestBody
        testReq: TestReq
    ) {
        testValidateService.validateServiceMethodObjParameter(testReq)
    }
}
