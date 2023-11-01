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

package com.tony.web.advice

import com.tony.ApiProperty
import com.tony.exception.ApiException
import com.tony.exception.BizException
import com.tony.utils.getLogger
import com.tony.web.WebApp.badRequest
import com.tony.web.WebApp.errorResponse
import com.tony.web.WebContext
import com.tony.web.WebContext.toResponse
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.ConstraintViolationException
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.BindException
import org.springframework.validation.BindingResult
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MissingRequestValueException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * 全局异常处理
 *
 * @author Tang Li
 * @date 2023/5/25 10:53
 */
@RestControllerAdvice
internal class ExceptionHandler : ErrorController {
    private val logger = getLogger()

    /**
     * 业务异常
     * @param [e] e
     * @author Tang Li
     * @date 2023/09/13 10:45
     * @since 1.0.0
     */
    @ExceptionHandler(BizException::class)
    @ResponseBody
    fun bizException(e: BizException) =
        e.toResponse()

    /**
     * 框架异常
     * @param [e] e
     * @author Tang Li
     * @date 2023/09/13 10:45
     * @since 1.0.0
     */
    @ExceptionHandler(ApiException::class)
    @ResponseBody
    fun apiException(e: ApiException) =
        run {
            e.cause?.apply {
                logger.warn(message, cause)
            }
            e.toResponse()
        }

    /**
     * 异常
     * @param [e] e
     * @param [response] 响应
     * @author Tang Li
     * @date 2023/09/13 10:46
     * @since 1.0.0
     */
    @ExceptionHandler(Exception::class)
    @ResponseBody
    fun exception(
        e: Exception,
        response: HttpServletResponse,
    ) = run {
        logger.error(e.message, e)
        // handle the json generate exception
        response.resetBuffer()
        errorResponse(ApiProperty.errorMsg)
    }

    private fun bindingResultMessages(bindingResult: BindingResult) =
        bindingResult.fieldErrors.first().let {
            if (it.isBindingFailure) {
                ApiProperty.badRequestMsg
            } else {
                it.defaultMessage ?: ""
            }
        }

    @ExceptionHandler(BindException::class)
    @ResponseBody
    fun bindingResultException(e: BindException) =
        badRequest(bindingResultMessages(e.bindingResult))

    @ExceptionHandler(ConstraintViolationException::class)
    @ResponseBody
    fun constraintViolationException(e: ConstraintViolationException) =
        badRequest(
            e
                .constraintViolations
                .first()
                .message
        )

    @ExceptionHandler(
        value = [
            MissingRequestValueException::class,
            HttpMessageNotReadableException::class,
            HttpRequestMethodNotSupportedException::class
        ]
    )
    @ResponseBody
    fun badRequestException(e: Exception) =
        run {
            logger.warn(e.localizedMessage, e)
            badRequest(ApiProperty.badRequestMsg)
        }

    @RequestMapping("\${server.error.path:\${error.path:/error}}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    fun error() =
        when {
            WebContext.httpStatus == 999 -> errorResponse("", ApiProperty.okCode)
            WebContext.httpStatus >= 500 -> {
                logger.error(WebContext.errorMessage)
                errorResponse(ApiProperty.errorMsg, ApiProperty.errorCode)
            }

            else -> errorResponse(WebContext.error, WebContext.httpStatus * 100)
        }
}
