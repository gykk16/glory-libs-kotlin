package io.glory

import io.glory.coremvc.MvcCommonFeaturesAutoConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Import
import kotlin.test.Test

@Import(MvcCommonFeaturesAutoConfig::class)
@TestConfiguration
@SpringBootApplication
@ConfigurationPropertiesScan
class TestConfig {

    @Test
    fun `context loads`(): Unit {
    }

}