package com.tony.webcore.advice

import com.tony.core.exception.BaseException
import com.tony.core.utils.getLogger
import com.tony.webcore.WebContext
import com.tony.webcore.WebContext.toResponse
import com.tony.webcore.badRequest
import com.tony.webcore.config.WebProperties
import com.tony.webcore.errorResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.http.HttpStatus
import org.springframework.validation.BindException
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.lang.Exception
import javax.validation.ConstraintViolationException

@RestControllerAdvice
@ConditionalOnWebApplication
@RestController
internal class ExceptionHandlerAdvice(
    private val webProperties: WebProperties,
    @Value("\${server.error.path:\${error.path:/error}}")
    private val errorPath: String
) : ErrorController {

    private val logger = getLogger()

    @ExceptionHandler(BaseException::class)
    fun apiException(e: BaseException) =
        run {
            e.cause?.apply {
                logger.warn(message, cause)
            }
            e.toResponse()
        }

    private fun bindingResultMessages(bindingResult: BindingResult) =
        bindingResult.fieldErrors.first().let {
            if (it.isBindingFailure) webProperties.validationErrorMsg
            else it.defaultMessage ?: ""
        }

    @ExceptionHandler(BindException::class)
    fun bindingResultException(e: BindException) =
        badRequest(bindingResultMessages(e.bindingResult))

    @ExceptionHandler(ConstraintViolationException::class)
    fun constraintViolationException(e: ConstraintViolationException) =
        badRequest(e.constraintViolations.first().message)

    @ExceptionHandler(Exception::class)
    fun exception(e: Exception) = run {
        logger.error(e.message, e)
        errorResponse(webProperties.errorMsg)
    }

    @RequestMapping("\${server.error.path:\${error.path:/error}}")
    @ResponseStatus(HttpStatus.OK)
    fun error() = errorResponse(
        when {
            WebContext.httpStatus == 999 -> ""
            WebContext.httpStatus >= 500 -> webProperties.errorMsg
            else -> WebContext.error
        },
        when {
            WebContext.httpStatus == 999 -> webProperties.successCode
            WebContext.httpStatus >= 500 -> webProperties.errorCode
            else -> WebContext.httpStatus * 100
        }
    )

    override fun getErrorPath(): String = errorPath
}
