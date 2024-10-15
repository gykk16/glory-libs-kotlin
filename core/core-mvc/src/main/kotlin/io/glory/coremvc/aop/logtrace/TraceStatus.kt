package io.glory.coremvc.aop.logtrace

/**
 * Trace status for logging
 * @see io.glory.coremvc.aop.logtrace.LogTrace
 */
@JvmRecord
data class TraceStatus(
    @JvmField val traceId: TraceId,
    @JvmField val startTimeMs: Long,
    @JvmField val message: String
)
