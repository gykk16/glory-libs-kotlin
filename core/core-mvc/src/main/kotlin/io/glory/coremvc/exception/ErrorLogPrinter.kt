package io.glory.coremvc.exception

import LogUtil
import io.github.oshai.kotlinlogging.KotlinLogging
import io.glory.core.codes.ResponseCode
import io.glory.coremvc.util.IpAddrUtil
import jakarta.servlet.http.HttpServletRequest

private val logger = KotlinLogging.logger {}

/**
 * Log error message printer
 */
object ErrorLogPrinter {

    /**
     * log error message
     *
     * @param request HttpServletRequest
     * @param code ResponseCode
     * @param e Exception
     * @param printTrace log trace stack
     */
    @JvmOverloads
    @JvmStatic
    fun logError(request: HttpServletRequest, code: ResponseCode, e: Exception, printTrace: Boolean = false) {
        val method = request.method
        val requestURI = request.requestURI
        val rootCause = getRootCause(e)

        logger.info { LogUtil.title("ERROR LOG") }
        logger.info { "# ==> RequestURI = $method , $requestURI" }
        logger.info { "# ==> ServerIp = ${IpAddrUtil.getServerIp()} , ClientIp = ${IpAddrUtil.getClientIp(request)}" }
        logger.info { "# ==> httpStatus = ${code.status} , responseCode = ${code.name} - ${code.message}" }
        logger.error { "# ==> exception = ${e.javaClass.simpleName} , ${e.message}" }
        logger.error { "# ==> rootCause = ${rootCause.javaClass.simpleName} , ${rootCause.message}" }

        if (printTrace) {
            logger.error(e) { "# ==> cause - " }
        }
    }

    /**
     * get root cause
     */
    private tailrec fun getRootCause(throwable: Throwable): Throwable {
        val cause = throwable.cause
        return if (cause != null && cause !== throwable) {
            getRootCause(cause)
        } else {
            throwable
        }
    }

}
