package io.glory.coremvc.annotation

/**
 * @see io.glory.coremvc.aop.securedip.SecuredIpAspect
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class SecuredIp(
    /**
     * Allowed ip addresses.
     * `*` is allowed for all ip addresses.
     */
    val ips: Array<String> = [""]
)
