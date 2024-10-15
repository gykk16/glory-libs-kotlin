package io.glory.coremvc.filter

import io.github.oshai.kotlinlogging.KotlinLogging
import io.glory.coremvc.FILTER_EXCLUDE_PATH
import io.glory.coremvc.filter.servletwrapperprovider.CachedBodyHttpServletRequest
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.annotation.WebFilter
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.util.Assert
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingResponseWrapper
import org.springframework.web.util.WebUtils
import java.io.IOException

private val log = KotlinLogging.logger {}

/**
 * Content caching filter
 * <p>This filter is used to cache the request body and response body.</p>
 */
@WebFilter(filterName = "ContentCachingFilter", urlPatterns = ["/**"])
class ContentCachingFilter : OncePerRequestFilter() {

    init {
        log.info { "# ==> ContentCachingFilter initialized" }
    }

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val cachedBodyHttpServletRequest = CachedBodyHttpServletRequest(request)
        val contentCachingResponse = ContentCachingResponseWrapper(response)

        filterChain.doFilter(cachedBodyHttpServletRequest, contentCachingResponse)

        val wrapper = WebUtils.getNativeResponse(contentCachingResponse, ContentCachingResponseWrapper::class.java)

        Assert.notNull(wrapper, "wrapper response is null")
        wrapper!!.copyBodyToResponse()
    }

    @Throws(ServletException::class)
    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val path = request.requestURI
        return FILTER_EXCLUDE_PATH.any { exclusion -> path.startsWith(exclusion) || path.endsWith(exclusion) }
    }

}
