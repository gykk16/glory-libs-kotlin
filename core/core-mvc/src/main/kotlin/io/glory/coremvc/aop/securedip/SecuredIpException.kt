package io.glory.coremvc.aop.securedip

import io.glory.core.codes.ErrorCode
import io.glory.coremvc.exception.BizRuntimeException

class SecuredIpException(
    clientIp: String,
) : BizRuntimeException(ErrorCode.UNAUTHORIZED_IP, clientIp)