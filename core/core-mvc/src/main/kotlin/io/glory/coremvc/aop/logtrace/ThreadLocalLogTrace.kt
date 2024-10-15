package io.glory.coremvc.aop.logtrace

import LogUtil.LOG_LINE
import io.github.oshai.kotlinlogging.KotlinLogging

private const val START_PREFIX = "--> "
private const val END_PREFIX = "<-- "
private const val EX_PREFIX = "<X- "

private val logger = KotlinLogging.logger {}

class ThreadLocalLogTrace : LogTrace {

    private val traceIdHolder = ThreadLocal<TraceId>()

    override fun begin(message: String): TraceStatus {
        syncTraceId()
        val traceId = traceIdHolder.get()
        val startTimeMs = System.currentTimeMillis()
        val prefix = addSpace(START_PREFIX, traceId.level)
        if (traceId.isFirstLevel) {
            logger.debug { LOG_LINE }
        }
        logger.info { "# ${prefix}${message}" }

        return TraceStatus(traceId, startTimeMs, message)
    }

    override fun end(status: TraceStatus) {
        complete(status, null)
    }

    override fun exception(status: TraceStatus, e: Exception) {
        complete(status, e)
    }

    private fun complete(status: TraceStatus, e: Exception?) {
        val endTimeMs = System.currentTimeMillis()
        val elapsedTimeMs = endTimeMs - status.startTimeMs

        val traceId = status.traceId
        val prefix = addSpace(if (e == null) END_PREFIX else EX_PREFIX, traceId.level)
        if (e == null) {
            logger.info { "# ${prefix}${status.message} , elapsedTimeMs=${elapsedTimeMs}ms" }
        } else {
            logger.warn { "# ${prefix}${status.message} , elapsedTimeMs=${elapsedTimeMs}ms , ex=${e.message}" }
        }
        if (traceId.isFirstLevel) {
            logger.debug { LOG_LINE }
        }
        releaseTraceId()
    }

    private fun syncTraceId() {
        traceIdHolder.set(traceIdHolder.get()?.nextLevel() ?: TraceId())
    }

    private fun releaseTraceId() {
        val traceId = traceIdHolder.get()
        if (traceId.isFirstLevel) {
            traceIdHolder.remove()
        } else {
            traceIdHolder.set(traceId.prevLevel())
        }
    }

    private fun addSpace(prefix: String, level: Int): String {
        return (0 until level).joinToString(separator = "") { if (it == level - 1) "|$prefix" else "|   " }
    }

}
