package io.glory.coremvc.aop.securedip

import io.github.oshai.kotlinlogging.KotlinLogging
import io.glory.coremvc.ConditionalOnFeature
import io.glory.coremvc.annotation.SecuredIp
import io.glory.coremvc.MvcCommonFeature.*
import io.glory.coremvc.util.IpAddrUtil
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import kotlin.reflect.full.findAnnotation

private val logger = KotlinLogging.logger {}

@Component
@ConditionalOnFeature(features = [SECURED_IP_AOP])
@Aspect
@Order(3)
class SecuredIpAspect {

    init {
        logger.info { "# ==> SecuredIpAspect initialized" }
    }

    @Pointcut("@within(io.glory.coremvc.annotation.SecuredIp) || @annotation(io.glory.coremvc.annotation.SecuredIp)")
    fun checkSecuredIp() {
    }

    @Before("checkSecuredIp()")
    @Throws(Throwable::class)
    fun checkClientIP(joinPoint: JoinPoint) {
        logger.debug { "# ==> Check secured ip" }

        val annotation = getAnnotation(joinPoint)
        val clientIp = IpAddrUtil.getClientIp()

        annotation.let {
            if (!isAllowedIp(clientIp, it.ips)) {
                throw SecuredIpException("Unauthorized IP: $clientIp")
            }
        }
    }

    private fun getAnnotation(joinPoint: JoinPoint): SecuredIp {
        return (joinPoint.signature.declaringType.kotlin.members
            .find { it.name == joinPoint.signature.name }
            ?.findAnnotation<SecuredIp>()
            ?: joinPoint.target::class.findAnnotation())!!
    }
}

private fun isAllowedIp(clientIp: String, allowedIps: Array<String>): Boolean {
    return if (allowedIps.contains("*")) {
        logger.debug { "# ==> All IPs are allowed" }
        true
    } else allowedIps.contains(clientIp)
}
