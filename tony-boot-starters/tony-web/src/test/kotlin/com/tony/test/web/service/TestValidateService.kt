package com.tony.test.web.service

import com.tony.test.web.req.TestReq
import com.tony.utils.getLogger
import com.tony.utils.toJsonString
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
 * @since 1.0.0
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
        @NotEmpty(message = "列表不能为空")
        array:Array<String>?,
        @Future(message = "得是一个未来")
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
