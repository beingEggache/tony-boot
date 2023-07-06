package com.tony.web.advice

import com.tony.ApiProperty
import com.tony.exception.ApiException
import com.tony.exception.BizException
import com.tony.fromInternalHeaderName
import com.tony.utils.doIf
import com.tony.utils.getLogger
import com.tony.web.WebApp.badRequest
import com.tony.web.WebApp.errorResponse
import com.tony.web.WebContext
import com.tony.web.WebContext.toResponse
import com.tony.wrapExceptionHeaderName
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
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.validation.ConstraintViolationException

/**
 * 全局异常处理
 *
 * @author tangli
 * @since 2023/5/25 10:53
 */
@RestControllerAdvice
@RestController
internal class ExceptionHandler : ErrorController {

    private val logger = getLogger()

    @ExceptionHandler(BizException::class)
    fun bizException(
        e: BizException,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ) =
        e.toResponse()
            .doIf(!request.getHeader(fromInternalHeaderName).isNullOrBlank()) {
                response.addHeader(wrapExceptionHeaderName, "true")
            }

    @ExceptionHandler(ApiException::class)
    fun apiException(
        e: ApiException,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ) =
        run {
            e.cause?.apply {
                logger.warn(message, cause)
            }
            e.toResponse()
        }.doIf(!request.getHeader(fromInternalHeaderName).isNullOrBlank()) {
            response.addHeader(wrapExceptionHeaderName, "true")
        }

    @ExceptionHandler(Exception::class)
    fun exception(
        e: Exception,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ) =
        run {
            logger.error(e.message, e)
            // handle the json generate exception
            response.resetBuffer()
            errorResponse(ApiProperty.errorMsg)
        }.doIf(!request.getHeader(fromInternalHeaderName).isNullOrBlank()) {
            response.addHeader(wrapExceptionHeaderName, "true")
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
    fun bindingResultException(
        e: BindException,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ) =
        badRequest(bindingResultMessages(e.bindingResult))
            .doIf(!request.getHeader(fromInternalHeaderName).isNullOrBlank()) {
                response.addHeader(wrapExceptionHeaderName, "true")
            }

    @ExceptionHandler(ConstraintViolationException::class)
    fun constraintViolationException(
        e: ConstraintViolationException,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ) =
        badRequest(e.constraintViolations.first().message)
            .doIf(!request.getHeader(fromInternalHeaderName).isNullOrBlank()) {
                response.addHeader(wrapExceptionHeaderName, "true")
            }

    @ExceptionHandler(
        value = [
            MissingRequestValueException::class,
            HttpMessageNotReadableException::class,
            HttpRequestMethodNotSupportedException::class,
        ],
    )
    fun badRequestException(
        e: Exception,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ) =
        run {
            logger.warn(e.localizedMessage, e)
            badRequest(ApiProperty.badRequestMsg)
        }.doIf(!request.getHeader(fromInternalHeaderName).isNullOrBlank()) {
            response.addHeader(wrapExceptionHeaderName, "true")
        }

    @RequestMapping("\${server.error.path:\${error.path:/error}}")
    @ResponseStatus(HttpStatus.OK)
    fun error(
        request: HttpServletRequest,
        response: HttpServletResponse,
    ) = when {
        WebContext.httpStatus == 999 -> errorResponse("", ApiProperty.okCode)
        WebContext.httpStatus >= 500 -> {
            logger.error(WebContext.errorMessage)
            errorResponse(ApiProperty.errorMsg, ApiProperty.errorCode)
        }

        else -> errorResponse(WebContext.error, WebContext.httpStatus * 100)
    }.doIf(!request.getHeader(fromInternalHeaderName).isNullOrBlank()) {
        response.addHeader(wrapExceptionHeaderName, "true")
    }
}
