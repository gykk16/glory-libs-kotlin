package io.glory.coremvc.response

import io.glory.core.codes.ResponseCode

/**
 * Success code
 */
enum class SuccessCode(
    override val status: Int,
    override val message: String
) : ResponseCode {
    //
    SUCCESS(200, "성공"),
    CREATED(201, "생성 성공"),
    ACCEPTED(202, "접수 성공"),
}
