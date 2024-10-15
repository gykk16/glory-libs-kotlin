package io.glory.coremvc.api

import io.github.oshai.kotlinlogging.KotlinLogging
import io.glory.core.util.idgenerator.TsidGenerator
import io.glory.coremvc.ConditionalOnFeature
import io.glory.coremvc.MvcCommonFeature.GLOBAL_CONTROLLER
import io.glory.coremvc.annotation.LogTrace
import io.glory.coremvc.response.ApiResponse
import io.glory.coremvc.response.SuccessCode.CREATED
import io.glory.coremvc.response.SuccessCode.SUCCESS
import org.springframework.core.env.Environment
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

private val logger = KotlinLogging.logger {}

@ConditionalOnFeature(features = [GLOBAL_CONTROLLER])
@RestController
@RequestMapping("/_global")
class GlobalController(
    private val env: Environment,
    private val idGenerator: TsidGenerator
) {

    init {
        logger.info { "# ==> GlobalController initialized" }
    }

    @GetMapping("/health")
    @LogTrace
    fun health(): String = "UP"

    @GetMapping("/profile")
    @LogTrace
    fun profile(): String = env.activeProfiles.firstOrNull { it.contains("set") } ?: ""

    @GetMapping("/trace-key")
    @LogTrace
    fun traceKey(): ResponseEntity<ApiResponse> = ApiResponse.of(CREATED, idGenerator.generate())

    @GetMapping("/trace-key/{traceKey}")
    @LogTrace
    fun parseTraceKey(@PathVariable traceKey: Long): ResponseEntity<ApiResponse> {
        val parsed = TsidGenerator.parse(traceKey)
        val generatedAt = TsidGenerator.generatedAt(traceKey)
        logger.info { "==> parsed.contentToString() = ${parsed.contentToString()} " }

        val result = mapOf(
            "id" to traceKey,
            "epoch" to parsed[0],
            "workerId" to parsed[1],
            "processId" to parsed[2],
            "sequence" to parsed[3],
            "generatedAt" to generatedAt
        )
        return ApiResponse.of(SUCCESS, result)
    }

}
