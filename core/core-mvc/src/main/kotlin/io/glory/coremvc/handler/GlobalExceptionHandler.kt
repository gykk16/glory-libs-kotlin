package io.glory.coremvc.handler

import io.github.oshai.kotlinlogging.KotlinLogging
import io.glory.core.codes.ErrorCode
import io.glory.core.codes.ResponseCode
import io.glory.coremvc.exception.BizException
import io.glory.coremvc.exception.BizRuntimeException
import io.glory.coremvc.exception.ErrorLogPrinter.logError
import io.glory.coremvc.response.v1.ApiResponse
import io.glory.coremvc.response.v2.ApiResource
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.method.annotation.HandlerMethodValidationException
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.resource.NoResourceFoundException

private val logger = KotlinLogging.logger {}

/**
 * Global exception handler
 *
 * @see BizRuntimeException
 * @see BizException
 */
//@RestControllerAdvice
//@ConditionalOnFeature(features = [GLOBAL_EXCEPTION_HANDLER])
//@Order(Ordered.LOWEST_PRECEDENCE)
class GlobalExceptionHandler {

    init {
        logger.info { "# ==> GlobalExceptionHandler initialized" }
    }

    @ExceptionHandler(BizRuntimeException::class)
    fun bizRuntimeException(
        request: HttpServletRequest, e: BizRuntimeException
    ): ResponseEntity<ApiResponse> {
        return createApiResponse(request, e.code, e, e.printStackTrace)
    }

    @ExceptionHandler(BizException::class)
    fun bizException(
        request: HttpServletRequest, e: BizRuntimeException
    ): ResponseEntity<ApiResponse> {
        return createApiResponse(request, e.code, e, e.printStackTrace)
    }

    @ExceptionHandler(
        NoResourceFoundException::class,
        HttpRequestMethodNotSupportedException::class,
        MethodArgumentNotValidException::class,
        MethodArgumentTypeMismatchException::class,
        HandlerMethodValidationException::class,
        HttpMessageNotReadableException::class,
        Exception::class
    )
    fun handleException(request: HttpServletRequest, ex: Exception): ResponseEntity<ApiResponse> {
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
                handleMethodArgumentTypeMismatchException(request, ex, ErrorCode.NOT_READABLE)
            }

            is HandlerMethodValidationException -> {
                handleHandlerMethodValidationException(request, ex, ErrorCode.INVALID_ARGUMENT)
            }

            is HttpMessageNotReadableException -> {
                handleHttpMessageNotReadableException(request, ex, ErrorCode.NOT_READABLE)
            }

            else -> {
                createApiResponse(request, ErrorCode.SERVER_ERROR, ex, true)
            }
        }
    }

    private fun handleNoResourceFoundException(
        request: HttpServletRequest, ex: NoResourceFoundException, errorCode: ResponseCode
    ): ResponseEntity<ApiResponse> {
        return createApiResponse(request, errorCode, ex)
    }

    private fun handleHttpRequestMethodNotSupportedException(
        request: HttpServletRequest, ex: HttpRequestMethodNotSupportedException, errorCode: ResponseCode
    ): ResponseEntity<ApiResponse> {
        return createApiResponse(request, errorCode, ex)
    }

    private fun handleMethodArgumentNotValidException(
        request: HttpServletRequest, ex: MethodArgumentNotValidException, errorCode: ResponseCode
    ): ResponseEntity<ApiResponse> {
        val errors = mutableMapOf<String, String?>()
        ex.bindingResult.allErrors.forEach { error ->
            if (error is FieldError) {
                val fieldName = error.field
                val errorMessage = error.defaultMessage
                errors[fieldName] = errorMessage
            }
        }

        return createApiResponse(request, errorCode, ex, false, errors)
    }

    private fun handleMethodArgumentTypeMismatchException(
        request: HttpServletRequest, ex: MethodArgumentTypeMismatchException, errorCode: ResponseCode
    ): ResponseEntity<ApiResponse> {
        return createApiResponse(request, errorCode, ex)
    }

    private fun handleHandlerMethodValidationException(
        request: HttpServletRequest, ex: HandlerMethodValidationException, errorCode: ResponseCode
    ): ResponseEntity<ApiResponse> {
        val errors = mutableMapOf<String, String?>()
        ex.allValidationResults
            .flatMap { it.resolvableErrors }
            .forEach { fieldError ->
                val fieldName = fieldError.codes?.lastOrNull().orEmpty()
                val errorMessage = fieldError.defaultMessage
                errors[fieldName] = errorMessage
            }

        return createApiResponse(request, errorCode, ex)
    }

    private fun handleHttpMessageNotReadableException(
        request: HttpServletRequest, ex: HttpMessageNotReadableException, errorCode: ResponseCode
    ): ResponseEntity<ApiResponse> {
        return createApiResponse(request, errorCode, ex)
    }

    companion object {

        @JvmOverloads
        fun createApiResponse(
            request: HttpServletRequest,
            code: ResponseCode,
            e: Exception,
            printStackTrace: Boolean = false,
            data: Any = e.message ?: code.message
        ): ResponseEntity<ApiResponse> {
            logError(request, code, e, printStackTrace)
            return ApiResponse.of(code, data)
        }

        fun createApiResource(
            request: HttpServletRequest,
            code: ResponseCode,
            e: Exception,
            printStackTrace: Boolean = false,
            data: Any = e.message ?: code.message
        ): ResponseEntity<ApiResource> {
            logError(request, code, e, printStackTrace)
            return ApiResource.of(code, data)
        }

    }

}
