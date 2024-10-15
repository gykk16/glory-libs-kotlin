package io.glory.coremvc.aop.logtrace

import io.glory.coremvc.X_TRACE_KEY
import org.slf4j.MDC
import java.util.*

const val FIRST_LEVEL = 1

class TraceId {
    val id: String = generateId()
    var level: Int = FIRST_LEVEL
        private set

    fun nextLevel(): TraceId {
        level++
        return this
    }

    fun prevLevel(): TraceId {
        level--
        return this
    }

    val isFirstLevel: Boolean
        get() = level == FIRST_LEVEL

    private fun generateId(): String {
        return MDC.get(X_TRACE_KEY) ?: UUID.randomUUID().toString()
    }

}
