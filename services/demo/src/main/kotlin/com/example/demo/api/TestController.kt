package com.example.demo.api

import io.github.oshai.kotlinlogging.KotlinLogging
import io.glory.coremvc.annotation.LogTrace
import io.glory.coremvc.annotation.SecuredIp
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("/_test")
@SecuredIp
class TestController {

    @GetMapping("/secured-ip")
    @SecuredIp
    fun testSecuredIp(): String {
        logger.info { "==> Test securedIp annotation" }
        return "Test securedIp annotation"
    }

    @GetMapping("/secured-ip-blocked")
    @SecuredIp(ips = ["-"])
    fun testSecuredIpBlocked(): String {
        logger.info { "==> Test securedIp annotation" }
        return "Test securedIp annotation"
    }

    @GetMapping("/log-trace")
    @LogTrace
    fun testTrace(): String {
        logger.info { "==> Test trace annotation" }
        return "Test trace annotation"
    }

}