package io.glory.coremvc.cache

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.convert.DurationUnit
import java.time.Duration
import java.time.temporal.ChronoUnit

@ConfigurationProperties(prefix = "core-mvc.caffeine")
data class CaffeineProperties(
    var cachePolicyMap: Map<String, CachePolicy> = emptyMap()
) {

    data class CachePolicy(
        var maxSize: Int,
        @field:DurationUnit(ChronoUnit.MINUTES)
        var expireAfterAccess: Duration
    ) {

    }

}
