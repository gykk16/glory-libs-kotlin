package io.glory.coremvc.response

import io.glory.core.codes.ResponseCode

/**
 * Error code
 */
enum class ErrorCode(
    override val status: Int,
    override val message: String
) : ResponseCode {
    //
    FORBIDDEN(403, "권한이 없습니다."),
    UNAUTHORIZED(401, "인증이 필요합니다."),
    UNAUTHORIZED_IP(401, "허용되지 않은 IP 입니다."),
    NOT_FOUND(404, "요청하신 자원을 찾을 수 없습니다."),
    INVALID_ARGUMENT(400, "요청 인자가 올바르지 않습니다."),
    NOT_READABLE(400, "요청 메시지가 올바르지 않습니다."),

    //
    SERVER_ERROR(500, "서버 오류 입니다."),
}
