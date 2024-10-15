package io.glory.coremvc.aop.logtrace

import io.github.oshai.kotlinlogging.KotlinLogging
import io.glory.coremvc.ConditionalOnFeature
import io.glory.coremvc.MvcCommonFeature.*
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

@Component
@ConditionalOnFeature(features = [LOG_TRACE_AOP])
@Aspect
@Order(2)
class LogTraceAspect(private val logTrace: LogTrace) {

    init {
        logger.info { "# ==> LogTraceAspect initialized" }
    }

    @Around("io.glory.coremvc.aop.logtrace.TracePointcuts.all()")
    @Throws(Throwable::class)
    fun doTimer(joinPoint: ProceedingJoinPoint): Any? {

        val status = logTrace.begin(joinPoint.signature.toShortString())
        return try {
            val result = joinPoint.proceed()
            logTrace.end(status)
            result
        } catch (e: Exception) {
            logTrace.exception(status, e)
            throw e
        }
    }
}
