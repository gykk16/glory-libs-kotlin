package io.glory.coremvc.filter

import io.github.oshai.kotlinlogging.KotlinLogging
import io.glory.core.util.idgenerator.IdGenerator
import io.glory.coremvc.FILTER_EXCLUDE_PATH
import io.glory.coremvc.X_TRACE_KEY
import io.glory.coremvc.util.IpAddrUtil
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.annotation.WebFilter
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import java.util.*

private val log = KotlinLogging.logger {}
const val WARN_PROCESS_TIME_MS = 2_000L

/**
 * Trace key filter
 * <p>This filter is used to generate trace key and log request information.</p>
 */
@WebFilter(filterName = "TraceKeyFilter", urlPatterns = ["/**"])
@Order(value = Ordered.HIGHEST_PRECEDENCE + 1)
class TraceKeyFilter(
    private val idGenerator: IdGenerator
) : OncePerRequestFilter() {

    init {
        log.info { "# ==> TraceKeyFilter initialized" }
    }

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val reqStartTimeMs = System.currentTimeMillis()
        val reqTraceKey = generateTraceKey()
        MDC.put(X_TRACE_KEY, reqTraceKey)
        response.setHeader(X_TRACE_KEY, reqTraceKey)

        try {
            log.info { "# REQ START #######################################################################################################" }

            logDefaultParameters(request)

            /* Perform the filter chain */
            filterChain.doFilter(request, response)

            val requestProcessTime = System.currentTimeMillis() - reqStartTimeMs
            log.info { "# Process time = ${requestProcessTime}ms" }

            if (requestProcessTime >= WARN_PROCESS_TIME_MS) {
                log.warn { "# Process time over ${WARN_PROCESS_TIME_MS}ms" }
            }

        } finally {
            log.info { "# REQ END   #######################################################################################################" }
            MDC.clear()
        }
    }

    @Throws(ServletException::class)
    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val path = request.requestURI
        return FILTER_EXCLUDE_PATH.any { exclusion -> path.startsWith(exclusion) || path.endsWith(exclusion) }
    }

    private fun generateTraceKey(): String {
        return idGenerator.generate().toString()
    }

    /**
     * Log default request parameters
     *
     * @param request HttpServletRequest
     */
    private fun logDefaultParameters(request: HttpServletRequest) {
        val method = request.method
        val requestURI = request.requestURI
        val contentType = request.contentType
        val referer = request.getHeader(HttpHeaders.REFERER)
        val accept = request.getHeader(HttpHeaders.ACCEPT)
        val userAgent = request.getHeader(HttpHeaders.USER_AGENT)
        val authorization = request.getHeader(HttpHeaders.AUTHORIZATION)
        val clientIp = IpAddrUtil.getClientIp(request)
        val serverIp = IpAddrUtil.getServerIp()

        log.info { "# RequestURI = $method, $requestURI" }

        referer?.takeIf { it.isNotBlank() }?.let {
            log.info { "# Referer = $it" }
        }

        log.info { "# Content-Type = $contentType, Accept = $accept, User-Agent = $userAgent" }

        authorization?.takeIf { it.isNotBlank() }?.let {
            log.info { "# Authorization = $it" }
        }

        log.info { "# ServerIp = $serverIp, ClientIp = $clientIp" }
    }


}
