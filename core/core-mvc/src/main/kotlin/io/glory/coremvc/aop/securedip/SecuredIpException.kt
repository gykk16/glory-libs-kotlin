package io.glory.coremvc.aop.securedip

import io.glory.coremvc.exception.BizRuntimeException
import io.glory.coremvc.response.ErrorCode

class SecuredIpException(
    clientIp: String,
) : BizRuntimeException(ErrorCode.UNAUTHORIZED_IP, clientIp)