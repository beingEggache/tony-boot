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
import com.tony.ApiResult
import com.tony.ERROR_CODE_HEADER_NAME
import com.tony.exception.ApiException
import com.tony.exception.BizException
import com.tony.utils.getLogger
import com.tony.utils.ifNullOrBlank
import com.tony.web.WebContext
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
import org.springframework.web.servlet.resource.NoResourceFoundException

/**
 * 全局异常处理
 *
 * @author tangli
 * @date 2023/05/25 19:53
 */
@RestControllerAdvice
internal class ExceptionHandler : ErrorController {
    private val logger = getLogger()

    /**
     * 业务异常
     * @param [e] e
     * @author tangli
     * @date 2023/09/13 19:45
     * @since 1.0.0
     */
    @ExceptionHandler(BizException::class)
    @ResponseBody
    fun bizException(e: BizException) =
        errorResponse(e.message.ifNullOrBlank(), e.code)

    /**
     * 框架异常
     * @param [e] e
     * @author tangli
     * @date 2023/09/13 19:45
     * @since 1.0.0
     */
    @ExceptionHandler(ApiException::class)
    @ResponseBody
    fun apiException(e: ApiException) =
        run {
            e.cause?.apply {
                logger.warn(message, cause)
            }
            errorResponse(e.message.ifNullOrBlank(), e.code)
        }

    /**
     * 异常
     * @param [e] e
     * @param [response] 响应
     * @author tangli
     * @date 2023/09/13 19:46
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
        bindingResult
            .allErrors
            .joinToString(System.lineSeparator()) {
                it.defaultMessage.ifNullOrBlank()
            }

    @ExceptionHandler(BindException::class)
    @ResponseBody
    fun bindingResultException(e: BindException) =
        errorResponse(
            bindingResultMessages(e.bindingResult),
            ApiProperty.badRequestCode
        )

    @ExceptionHandler(ConstraintViolationException::class)
    @ResponseBody
    fun constraintViolationException(e: ConstraintViolationException) =
        errorResponse(
            e
                .constraintViolations
                .joinToString(System.lineSeparator()) { it.message },
            ApiProperty.badRequestCode
        )

    @ExceptionHandler(
        value = [
            MissingRequestValueException::class,
            HttpMessageNotReadableException::class
        ]
    )
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    fun badRequestException(e: Exception) =
        run {
            logger.warn(e.localizedMessage)
            errorResponse(
                ApiProperty.badRequestMsg,
                HttpStatus.BAD_REQUEST.value()
            )
        }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ResponseBody
    fun methodNotSupportException(e: HttpRequestMethodNotSupportedException) =
        run {
            errorResponse(
                e.localizedMessage,
                HttpStatus.METHOD_NOT_ALLOWED.value()
            )
        }

    @ExceptionHandler(NoResourceFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    fun notFoundException(e: NoResourceFoundException) =
        errorResponse(
            "${e.resourcePath} not found.",
            HttpStatus.NOT_FOUND.value()
        )

    @RequestMapping("\${server.error.path:\${error.path:/error}}")
    @ResponseBody
    fun error() =
        run {
            WebContext.response?.status = HttpStatus.OK.value()
            when {
                WebContext.httpStatus == 999 -> errorResponse("", ApiProperty.okCode)

                WebContext.httpStatus >= 500 -> {
                    logger.error(WebContext.errorMessage)
                    errorResponse(ApiProperty.errorMsg)
                }

                else -> errorResponse(WebContext.error, WebContext.httpStatus)
            }
        }

    /**
     * 错误响应
     * @param [msg] 消息
     * @param [code] 默认为 [ApiProperty.errorCode]
     * @return [ApiResult]<*>
     * @author tangli
     * @date 2023/10/24 19:27
     * @since 1.0.0
     */
    private fun errorResponse(
        msg: String = "",
        code: Int = ApiProperty.errorCode,
    ): ApiResult<*> {
        WebContext.response?.addHeader(ERROR_CODE_HEADER_NAME, code.toString())
        return ApiResult(Unit, code, msg)
    }
}
