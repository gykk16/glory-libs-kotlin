package io.glory.coremvc.runner

import LogUtil
import io.github.oshai.kotlinlogging.KotlinLogging
import io.glory.coremvc.ConditionalOnFeature
import io.glory.coremvc.util.IpAddrUtil
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
import java.util.*

private val logger = KotlinLogging.logger {}

@Component
class WebAppRunner {

    @Value("\${spring.application.version:}")
    private val appVersion: String = ""

    @Value("\${spring.profiles.active:}")
    private val activeProfile: String = ""

    @Value("\${server.port:}")
    private val port: String = ""

    @Bean
    fun run(env: Environment): CommandLineRunner {
        return CommandLineRunner { _: Array<String> ->
            logger.info { LogUtil.LOG_LINE }
            logger.info { "# ==> active profiles = $activeProfile, ${Arrays.stream(env.activeProfiles).toList()}" }
            logger.info { "# ==> app version = $appVersion " }
            logger.info { "# ==> server ip = ${IpAddrUtil.getServerIp()} , port = $port" }
            logger.info { "# ==> APP START COMPLETE!!!" }
            logger.info { LogUtil.LOG_LINE }

        }
    }
}