package com.example.demo.api

import io.github.oshai.kotlinlogging.KotlinLogging
import io.glory.coremvc.annotation.LogTrace
import io.glory.coremvc.annotation.SecuredIp
import io.glory.coremvc.response.v2.ApiResource
import org.springframework.data.domain.PageImpl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("/_test")
//@SecuredIp
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

    @GetMapping("/api-resource/list")
    fun testApiResourceList(): ResponseEntity<ApiResource> {
        val list = listOf(1, 2, 3)
        return ApiResource.ofCollection(collection = list)
    }

    @GetMapping("/api-resource/map")
    fun testApiResourceMap(): ResponseEntity<ApiResource> {
        val map = mapOf("key1" to "value1", "key2" to "value2")
        return ApiResource.ofMap(map = map)
    }

    @GetMapping("/api-resource/list-of-map")
    fun testApiResourceListOfMap(): ResponseEntity<ApiResource> {
        val list = listOf(
            mapOf("key1" to "value1", "key2" to "value2"),
            mapOf("key3" to "value3", "key4" to "value4"),
            mapOf("key5" to "value5", "key6" to "value6")
        )
        return ApiResource.of(list)
    }

    @GetMapping("/api-resource/page")
    fun testApiResourcePage(): ResponseEntity<ApiResource> {
        val pageImpl = PageImpl(listOf(1, 2, 3))
        return ApiResource.ofPage(page = pageImpl)
    }

}