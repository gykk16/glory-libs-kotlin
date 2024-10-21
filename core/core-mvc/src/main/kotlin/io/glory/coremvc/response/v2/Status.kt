package io.glory.coremvc.response.v2

import com.fasterxml.jackson.annotation.JsonProperty
import io.glory.core.codes.ResponseCode

/**
 * Standard status response format
 */
data class Status(
    @JsonProperty("status")
    val status: Int,
    @JsonProperty("code")
    val code: String,
    @JsonProperty("message")
    val message: String
) {
    companion object {
        @JvmStatic
        fun of(responseCode: ResponseCode): Status {
            return Status(responseCode.status, responseCode.name, responseCode.message)
        }
    }
}
