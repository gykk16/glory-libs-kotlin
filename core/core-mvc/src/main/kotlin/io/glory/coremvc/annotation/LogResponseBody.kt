package io.glory.coremvc.annotation

/**
 * Log response body annotation
 * @see io.glory.coremvc.interceptor.LogResponseBodyInterceptor
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class LogResponseBody(
    /**
     * Whether to log response body
     */
    val value: Boolean = true,
    /**
     * Maximum length of response body to log
     */
    val maxLength: Int = 1_000
)
