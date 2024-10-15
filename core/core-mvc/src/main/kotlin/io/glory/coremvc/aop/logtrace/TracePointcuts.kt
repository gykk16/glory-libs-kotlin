package io.glory.coremvc.aop.logtrace

import org.aspectj.lang.annotation.Pointcut

class TracePointcuts {

    @Pointcut("@annotation(io.glory.coremvc.annotation.ExcludeLogTrace)")
    fun excludeLogTraceAnnotation() {
    }

    @Pointcut("@annotation(io.glory.coremvc.annotation.LogTrace)")
    fun logTraceAnnotation() {
    }

    @Pointcut("execution(* io.glory..*Controller.*(..))")
    fun allController() {
    }

    @Pointcut("execution(* io.glory..*Service.*(..))")
    fun allService() {
    }

    @Pointcut("execution(* io.glory..*Repository.*(..))")
    fun allRepository() {
    }

    @Pointcut("(allController() || allService() || allRepository() || logTraceAnnotation()) && !excludeLogTraceAnnotation()")
    fun all() {
    }
}
