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
 * @author tangli
 * @since 2023/5/25 10:53
 */
@RestControllerAdvice
internal class ExceptionHandler : ErrorController {

    private val logger = getLogger()

    @ExceptionHandler(BizException::class)
    @ResponseBody
    fun bizException(e: BizException) = e.toResponse()

    @ExceptionHandler(ApiException::class)
    @ResponseBody
    fun apiException(e: ApiException) =
        run {
            e.cause?.apply {
                logger.warn(message, cause)
            }
            e.toResponse()
        }

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
        badRequest(e.constraintViolations.first().message)

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
    fun error() = when {
        WebContext.httpStatus == 999 -> errorResponse("", ApiProperty.okCode)
        WebContext.httpStatus >= 500 -> {
            logger.error(WebContext.errorMessage)
            errorResponse(ApiProperty.errorMsg, ApiProperty.errorCode)
        }

        else -> errorResponse(WebContext.error, WebContext.httpStatus * 100)
    }
}
