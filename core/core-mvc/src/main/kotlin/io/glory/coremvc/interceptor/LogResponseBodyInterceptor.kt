package io.glory.coremvc.interceptor

import io.github.oshai.kotlinlogging.KotlinLogging
import io.glory.coremvc.annotation.LogResponseBody
import io.glory.coremvc.util.HttpServletUtil.getResponseBody
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor

const val TRUNCATION_SUFFIX = " (truncated)..."
private val logger = KotlinLogging.logger {}

/**
 * Interceptor to log response body
 *
 * [LogResponseBody] annotation is used to log response body.
 *
 * @see LogResponseBody
 */
class LogResponseBodyInterceptor : HandlerInterceptor {

    init {
        logger.info { "# ==> LogResponseBodyInterceptor initialized" }
    }

    @Throws(Exception::class)
    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?
    ) {
        if (handler is HandlerMethod) {
            val annotation = handler.getMethodAnnotation(LogResponseBody::class.java)
            annotation?.let { logResponseBody(it) }
        }
    }

    companion object {

        private fun logResponseBody(annotation: LogResponseBody) {
            if (!annotation.value) {
                return
            }

            val maxLength = annotation.maxLength
            val responseBody = getResponseBody().let {
                if (it.length > maxLength) {
                    it.substring(0, maxLength - 15) + TRUNCATION_SUFFIX
                } else {
                    it
                }
            }
            logger.info { "# Response Body = $responseBody" }
        }
    }
}
