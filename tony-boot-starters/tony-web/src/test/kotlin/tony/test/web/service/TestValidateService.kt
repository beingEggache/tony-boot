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

package tony.test.web.service

import tony.test.web.req.TestReq
import tony.utils.getLogger
import tony.utils.toJsonString
import jakarta.validation.Valid
import jakarta.validation.constraints.Future
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotEmpty
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import java.time.LocalDate

/**
 * TestValidateService is
 * @author tangli
 * @date 2024/06/25 17:48
 */
@Validated
@Service
class TestValidateService {

    val logger = getLogger()

    fun validateServiceMethodParameter(
        @NotEmpty(message = "列表不能为空")
        list:List<String>?,
        @Min(value = 10, message = "最小{value}")
        @Max(value = 18, message = "最大{value}")
        age:Int?
    ) {
        logger.info("validating $list, $age")
    }
    fun validateServiceMethodParameter(
        @NotEmpty
        array:Array<String>?,
        @Future
        future:LocalDate?
    ) {
        logger.info("validating $array, $future")
    }
    fun validateServiceMethodObjParameter(
        @Valid
        testReq: TestReq,
    ) {
        logger.info("validating ${testReq.toJsonString()}")
    }
}
