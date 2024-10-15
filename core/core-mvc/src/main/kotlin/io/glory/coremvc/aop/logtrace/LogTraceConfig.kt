package io.glory.coremvc.aop.logtrace

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class LogTraceConfig {

    @Bean
    fun logTrace(): LogTrace {
        return ThreadLocalLogTrace()
    }
}
