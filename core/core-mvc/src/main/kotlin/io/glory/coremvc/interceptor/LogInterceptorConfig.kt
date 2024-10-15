package io.glory.coremvc.interceptor

import io.glory.coremvc.ConditionalOnFeature
import io.glory.coremvc.INTERCEPTOR_EXCLUDE_PATH
import io.glory.coremvc.MvcCommonFeature.LOG_INTERCEPTOR
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@ConditionalOnFeature(features = [LOG_INTERCEPTOR])
class LogInterceptorConfig : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(logInterceptor())
            .order(1)
            .addPathPatterns("/**")
            .excludePathPatterns(INTERCEPTOR_EXCLUDE_PATH)
        registry.addInterceptor(logResponseBodyInterceptor())
            .order(2)
            .addPathPatterns("/**")
            .excludePathPatterns(INTERCEPTOR_EXCLUDE_PATH)
    }

    @Bean
    @ConditionalOnMissingBean
    fun logInterceptor(): LogInterceptor {
        return LogInterceptor()
    }

    @Bean
    @ConditionalOnMissingBean
    fun logResponseBodyInterceptor(): LogResponseBodyInterceptor {
        return LogResponseBodyInterceptor()
    }

}