package io.glory.coremvc.aop.logtrace

interface LogTrace {

    fun begin(message: String): TraceStatus

    fun end(status: TraceStatus)

    fun exception(status: TraceStatus, e: Exception)

}
