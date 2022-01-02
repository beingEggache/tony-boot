@file:Suppress("unused")

package com.tony.web

import com.tony.ApiProperty
import com.tony.exception.ApiException
import com.tony.exception.BizException
import com.tony.utils.getLogger
import com.tony.web.WebApp.badRequest
import com.tony.web.WebApp.errorResponse
import com.tony.web.WebContext.toResponse
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.BindException
import org.springframework.validation.BindingResult
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MissingRequestValueException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.validation.ConstraintViolationException

@RestControllerAdvice
@RestController
internal class ExceptionHandler : ErrorController {

    private val logger = getLogger()

    @ExceptionHandler(BizException::class)
    fun bizException(e: BizException) = e.toResponse()

    @ExceptionHandler(ApiException::class)
    fun apiException(e: ApiException) = run {
        e.cause?.apply {
            logger.warn(message, cause)
        }
        e.toResponse()
    }

    @ExceptionHandler(Exception::class)
    fun exception(e: Exception) = run {
        logger.error(e.message, e)
        // handle the json generate exception
        WebContext.response?.resetBuffer()
        errorResponse(ApiProperty.errorMsg)
    }

    private fun bindingResultMessages(bindingResult: BindingResult) =
        bindingResult.fieldErrors.first().let {
            if (it.isBindingFailure) ApiProperty.validationErrorMsg
            else it.defaultMessage ?: ""
        }

    @ExceptionHandler(BindException::class)
    fun bindingResultException(e: BindException) =
        badRequest(bindingResultMessages(e.bindingResult))

    @ExceptionHandler(ConstraintViolationException::class)
    fun constraintViolationException(e: ConstraintViolationException) =
        badRequest(e.constraintViolations.first().message)

    @ExceptionHandler(
        value = [
            MissingRequestValueException::class,
            HttpMessageNotReadableException::class,
            HttpRequestMethodNotSupportedException::class
        ]
    )
    fun badRequestException(e: Exception) = run {
        logger.warn(e.localizedMessage)
        badRequest(ApiProperty.validationErrorMsg)
    }

    @RequestMapping("\${server.error.path:\${error.path:/error}}")
    @ResponseStatus(HttpStatus.OK)
    fun error() = errorResponse(
        when {
            WebContext.httpStatus == 999 -> ""
            WebContext.httpStatus >= 500 -> ApiProperty.errorMsg
            else -> WebContext.error
        },
        when {
            WebContext.httpStatus == 999 -> ApiProperty.successCode
            WebContext.httpStatus >= 500 -> ApiProperty.errorCode
            else -> WebContext.httpStatus * 100
        }
    )
}
