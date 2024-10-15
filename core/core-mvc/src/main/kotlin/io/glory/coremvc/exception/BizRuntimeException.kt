package io.glory.coremvc.exception

import io.glory.core.codes.ResponseCode

/**
 * RuntimeException for business logic
 *
 * @param code response code
 * @param msg message
 * @param printStackTrace whether to print stack trace
 * @param cause cause of exception
 * @see io.glory.coremvc.response.ResponseCode
 */
open class BizRuntimeException @JvmOverloads constructor(
    val code: ResponseCode,
    val msg: String = code.message,
    val printStackTrace: Boolean = false,
    cause: Throwable? = null
) : RuntimeException(
    msg, cause
)
