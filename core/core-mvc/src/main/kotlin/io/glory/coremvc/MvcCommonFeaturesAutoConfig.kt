package io.glory.coremvc

import io.github.oshai.kotlinlogging.KotlinLogging
import io.glory.coremvc.cache.CaffeineProperties
import io.glory.coremvc.filter.TsidProperties
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.ComponentScan

private val logger = KotlinLogging.logger {}

@AutoConfiguration
@ConditionalOnProperty(
    name = ["core-mvc.enabled"],
    havingValue = "true"
)
@ComponentScan("io.glory.coremvc")
@EnableConfigurationProperties(
    TsidProperties::class,
    CaffeineProperties::class
)
class MvcCommonFeaturesAutoConfig {

    init {
        logger.info { "# ==> MvcCommonFeaturesAutoConfig initialized" }
    }

}