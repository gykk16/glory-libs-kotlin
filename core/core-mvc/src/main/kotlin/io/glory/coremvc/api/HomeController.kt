package io.glory.coremvc.api

import io.github.oshai.kotlinlogging.KotlinLogging
import io.glory.coremvc.annotation.LogResponseBody
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

private val logger = KotlinLogging.logger {}

@RestController
class HomeController {

    init {
        logger.info { "# ==> HomeController initialized" }
    }

    @Value("\${spring.application.name:}")
    private lateinit var name: String

    @Value("\${spring.application.version:0.0.0}")
    private lateinit var version: String

    @RequestMapping
    @LogResponseBody
    fun home() = "application name = $name , version = $version"

}