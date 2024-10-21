package io.glory.coremvc

/**
 * Mvc common features
 * @param property the property name of the feature
 */
enum class MvcCommonFeature(
    val property: String
) {

    /**
     * @see io.glory.coremvc.api.GlobalController
     */
    GLOBAL_CONTROLLER("global-controller"),

    /**
     * @see io.glory.coremvc.handler.GlobalExceptionHandler
     */
    GLOBAL_EXCEPTION_HANDLER("global-exception-handler"),

    /**
     * @see io.glory.coremvc.filter.ContentCachingFilter
     */
    CONTENT_CACHING_FILTER("content-caching-filter"),

    /**
     * @see io.glory.coremvc.filter.TraceKeyFilter
     */
    TRACE_KEY_FILTER("trace-key-filter"),

    /**
     * @see io.glory.coremvc.interceptor.LogInterceptorConfig
     */
    LOG_INTERCEPTOR("log-interceptor"),

    /**
     * log trace aop
     * @see io.glory.coremvc.aop.logtrace.LogTraceAspect
     */
    LOG_TRACE_AOP("log-trace-aop"),

    /**
     * @see io.glory.coremvc.aop.securedip.SecuredIpAspect
     */
    SECURED_IP_AOP("secured-ip-aop"),

    /**
     * @see io.glory.coremvc.cache.CaffeineConfig
     */
    CAFFEINE_CACHE("caffeine-cache"),
}