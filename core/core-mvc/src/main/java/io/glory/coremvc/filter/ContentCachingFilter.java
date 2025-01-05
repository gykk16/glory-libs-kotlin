package io.glory.coremvc.filter;

import java.io.IOException;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import io.glory.coremvc.filter.servletwrapperprovider.CachedBodyHttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

/**
 * Request/Response Body 를 Caching 하는 Filter
 */
@WebFilter(filterName = "ContentCachingFilter", urlPatterns = "/**")
@Order(value = Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class ContentCachingFilter extends OncePerRequestFilter {

    @PostConstruct
    private void init() {
        log.info("# ==> ContentCachingFilter initialized");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var cachedBodyHttpServletRequest = new CachedBodyHttpServletRequest(request);
        var contentCachingResponse = new ContentCachingResponseWrapper(response);

        // ==============================
        // doFilter
        // ==============================
        filterChain.doFilter(cachedBodyHttpServletRequest, contentCachingResponse);

        ContentCachingResponseWrapper wrapper =
                WebUtils.getNativeResponse(contentCachingResponse, ContentCachingResponseWrapper.class);

        Assert.notNull(wrapper, "wrapper response is null");
        wrapper.copyBodyToResponse();
    }

}
