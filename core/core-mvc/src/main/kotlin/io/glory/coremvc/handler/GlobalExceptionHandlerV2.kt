package io.glory.coremvc.handler

import io.github.oshai.kotlinlogging.KotlinLogging
import io.glory.core.codes.ErrorCode
import io.glory.core.codes.ResponseCode
import io.glory.coremvc.ConditionalOnFeature
import io.glory.coremvc.MvcCommonFeature
import io.glory.coremvc.exception.BizException
import io.glory.coremvc.exception.BizRuntimeException
import io.glory.coremvc.exception.ErrorLogPrinter
import io.glory.coremvc.response.v2.ApiResource
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingRequestValueException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.HandlerMethodValidationException
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.multipart.MultipartException
import org.springframework.web.multipart.support.MissingServletRequestPartException
import org.springframework.web.servlet.resource.NoResourceFoundException

private val logger = KotlinLogging.logger {}

/**
 * Global exception handler V2
 *
 * @see BizRuntimeException
 * @see BizException
 */
@RestControllerAdvice
@ConditionalOnFeature(features = [MvcCommonFeature.GLOBAL_EXCEPTION_HANDLER])
@Order(Ordered.LOWEST_PRECEDENCE)
class GlobalExceptionHandlerV2 {

    init {
        logger.info { "# ==> GlobalExceptionHandlerV2 initialized" }
    }

    @ExceptionHandler(BizRuntimeException::class)
    fun bizRuntimeException(
        request: HttpServletRequest, e: BizRuntimeException
    ): ResponseEntity<ApiResource> {
        return createApiResource(request, e.code, e, e.printStackTrace)
    }

    @ExceptionHandler(BizException::class)
    fun bizException(
        request: HttpServletRequest, e: BizRuntimeException
    ): ResponseEntity<ApiResource> {
        return createApiResource(request, e.code, e, e.printStackTrace)
    }

    @ExceptionHandler(
        NoResourceFoundException::class,
        HttpRequestMethodNotSupportedException::class,
        MethodArgumentNotValidException::class,
        MissingRequestValueException::class,
        MethodArgumentTypeMismatchException::class,
        MissingServletRequestPartException::class,
        HandlerMethodValidationException::class,
        HttpMessageNotReadableException::class,
        MultipartException::class,
        Exception::class
    )
    fun handleException(request: HttpServletRequest, ex: Exception): ResponseEntity<ApiResource> {
        return when (ex) {
            is NoResourceFoundException -> {
                handleNoResourceFoundException(request, ex, ErrorCode.NOT_FOUND)
            }

            is HttpRequestMethodNotSupportedException -> {
                handleHttpRequestMethodNotSupportedException(request, ex, ErrorCode.NOT_FOUND)
            }

            is MethodArgumentNotValidException -> {
                handleMethodArgumentNotValidException(request, ex, ErrorCode.INVALID_ARGUMENT)
            }

            is MethodArgumentTypeMismatchException -> {
                handleMethodArgumentTypeMismatchException(request, ex, ErrorCode.INVALID_ARGUMENT)
            }

            is MissingRequestValueException -> {
                handleMissingRequestValueException(request, ex, ErrorCode.INVALID_ARGUMENT)
            }

            is MissingServletRequestPartException -> {
                handleMissingServletRequestPartException(request, ex, ErrorCode.INVALID_ARGUMENT)
            }

            is HandlerMethodValidationException -> {
                handleHandlerMethodValidationException(request, ex, ErrorCode.INVALID_ARGUMENT)
            }

            is HttpMessageNotReadableException,
            is HttpMediaTypeNotSupportedException -> {
                handleHttpMessageNotReadableException(request, ex, ErrorCode.NOT_READABLE)
            }

            is MultipartException -> {
                handleMultipartException(request, ex, ErrorCode.INVALID_ARGUMENT)
            }

            is IllegalArgumentException -> {
                createApiResource(request, ErrorCode.ILLEGAL_ARGUMENT, ex)
            }

            is IllegalStateException -> {
                createApiResource(request, ErrorCode.ILLEGAL_STATE, ex)
            }

            else -> {
                createApiResource(request, ErrorCode.SERVER_ERROR, ex, true)
            }
        }
    }

    private fun handleNoResourceFoundException(
        request: HttpServletRequest, ex: NoResourceFoundException, errorCode: ResponseCode
    ): ResponseEntity<ApiResource> {
        return createApiResource(request, errorCode, ex)
    }

    private fun handleHttpRequestMethodNotSupportedException(
        request: HttpServletRequest, ex: HttpRequestMethodNotSupportedException, errorCode: ResponseCode
    ): ResponseEntity<ApiResource> {
        return createApiResource(request, errorCode, ex)
    }

    private fun handleMethodArgumentNotValidException(
        request: HttpServletRequest, ex: MethodArgumentNotValidException, errorCode: ResponseCode
    ): ResponseEntity<ApiResource> {
        val errors = mutableMapOf<String, String?>()
        ex.bindingResult.allErrors.forEach { error ->
            if (error is FieldError) {
                val fieldName = error.field
                val errorMessage = error.defaultMessage
                errors[fieldName] = errorMessage
            }
        }

        return createApiResource(request, errorCode, ex, false, errors)
    }

    private fun handleMethodArgumentTypeMismatchException(
        request: HttpServletRequest, ex: MethodArgumentTypeMismatchException, errorCode: ResponseCode
    ): ResponseEntity<ApiResource> {
        return createApiResource(request, errorCode, ex)
    }

    private fun handleMissingRequestValueException(
        request: HttpServletRequest, ex: MissingRequestValueException, errorCode: ResponseCode
    ): ResponseEntity<ApiResource> {
        val detail = ex.body.detail ?: errorCode.message
        return createApiResource(request, errorCode, ex, false, detail)
    }

    private fun handleMissingServletRequestPartException(
        request: HttpServletRequest, ex: MissingServletRequestPartException, errorCode: ResponseCode
    ): ResponseEntity<ApiResource> {
        val detail = ex.body.detail ?: errorCode.message
        return createApiResource(request, errorCode, ex, true, detail)
    }

    private fun handleHandlerMethodValidationException(
        request: HttpServletRequest, ex: HandlerMethodValidationException, errorCode: ResponseCode
    ): ResponseEntity<ApiResource> {
        val errors = mutableMapOf<String, String?>()
        ex.allValidationResults
            .flatMap { it.resolvableErrors }
            .forEach { fieldError ->
                val fieldName = fieldError.codes?.lastOrNull().orEmpty()
                val errorMessage = fieldError.defaultMessage
                errors[fieldName] = errorMessage
            }

        return createApiResource(request, errorCode, ex, false, errors)
    }

    private fun handleHttpMessageNotReadableException(
        request: HttpServletRequest, ex: Exception, errorCode: ResponseCode
    ): ResponseEntity<ApiResource> {
        val message = ex.message?.split(":")?.get(0) ?: errorCode.message
        return createApiResource(request, errorCode, ex, false, message)
    }

    private fun handleMultipartException(
        request: HttpServletRequest, ex: MultipartException, errorCode: ResponseCode
    ): ResponseEntity<ApiResource> {
        return createApiResource(request, errorCode, ex)
    }

    companion object {

        @JvmOverloads
        fun createApiResource(
            request: HttpServletRequest,
            code: ResponseCode,
            e: Exception,
            printStackTrace: Boolean = false,
            data: Any = e.message ?: code.message
        ): ResponseEntity<ApiResource> {
            ErrorLogPrinter.logError(request, code, e, printStackTrace)
            return ApiResource.of(code, data)
        }

    }

}