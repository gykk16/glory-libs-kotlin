package io.glory.coremvc.interceptor

import io.github.oshai.kotlinlogging.KotlinLogging
import io.glory.coremvc.util.HttpServletUtil
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.util.StringUtils
import org.springframework.web.servlet.HandlerInterceptor
import java.io.IOException

/**
 * Log interceptor
 * <p>
 *     This interceptor is used to log request parameters and request body.
 * </p>
 */
class LogInterceptor : HandlerInterceptor {

    private val logger = KotlinLogging.logger {}

    init {
        logger.info { "# ==> LogInterceptor initialized" }
    }

    @Throws(Exception::class)
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        logRequestParameters()
        logRequestBody()
        return true
    }

    /**
     * Log request parameters if exists
     */
    private fun logRequestParameters() {
        val requestParameters = HttpServletUtil.getRequestParams()
        if (StringUtils.hasText(requestParameters)) {
            logger.debug { "# Request Parameters = $requestParameters" }
        }
    }

    /**
     * Log request body if exists
     */
    @Throws(IOException::class)
    private fun logRequestBody() {
        val requestBody = HttpServletUtil.getRequestBody()
        if (StringUtils.hasText(requestBody)) {
            logger.debug { "# Request Body = $requestBody" }
        }
    }

}
