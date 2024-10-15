package io.glory.coremvc.filter

import io.glory.core.util.idgenerator.TsidGenerator
import io.glory.coremvc.ConditionalOnFeature
import io.glory.coremvc.MvcCommonFeature.CONTENT_CACHING_FILTER
import io.glory.coremvc.MvcCommonFeature.TRACE_KEY_FILTER
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FilterConfig {

    @Bean
    @ConditionalOnFeature(features = [TRACE_KEY_FILTER])
    fun traceKeyFilter(idGenerator: TsidGenerator): TraceKeyFilter {
        return TraceKeyFilter(idGenerator)
    }

    @Bean
    @ConditionalOnFeature(features = [CONTENT_CACHING_FILTER])
    fun contentCachingFilter(): ContentCachingFilter {
        return ContentCachingFilter()
    }

    @Bean
    @ConditionalOnFeature(features = [TRACE_KEY_FILTER])
    fun idGenerator(properties: TsidProperties): TsidGenerator {
        return TsidGenerator(properties.workerId, properties.processId)
    }

}