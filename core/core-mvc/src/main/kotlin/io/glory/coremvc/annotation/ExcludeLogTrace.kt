package io.glory.coremvc.annotation

/**
 * Exclude log trace annotation
 * @see io.glory.coremvc.aop.logtrace.LogTraceAspect
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ExcludeLogTrace
