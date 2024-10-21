package io.glory.coremvc.response.v1

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import io.glory.core.codes.ErrorCode.SERVER_ERROR
import io.glory.core.codes.ResponseCode
import io.glory.core.codes.SuccessCode.SUCCESS
import org.springframework.http.ResponseEntity
import java.time.LocalDateTime

/**
 * Standard api response format
 *
 * @param code response code
 * @param message response message
 * @param responseDt response date time
 * @param data response data
 * @param <T> data type
 * */
@Deprecated("Use ApiResource instead")
data class ApiResponse(
    @JsonProperty("code")
    val code: ResponseCode,
    @JsonProperty("message")
    val message: String,
    @JsonFormat(pattern = "yyyy-mm-dd'T'HH:mm:ss.SSS")
    @JsonSerialize(using = LocalDateTimeSerializer::class)
    @JsonProperty("responseDt")
    val responseDt: LocalDateTime,
    @JsonProperty("data")
    val data: Any,
) {

    companion object {

        @JvmStatic
        @JvmOverloads
        fun success(data: Any = SUCCESS.message): ResponseEntity<ApiResponse> {
            return of(SUCCESS, data)
        }

        @JvmStatic
        @JvmOverloads
        fun error(data: Any = SERVER_ERROR.message): ResponseEntity<ApiResponse> {
            return of(SERVER_ERROR, data)
        }

        @JvmStatic
        @JvmOverloads
        fun of(code: ResponseCode, data: Any = code.message): ResponseEntity<ApiResponse> {
            return toResponseEntity(ApiResponse(code, code.message, LocalDateTime.now(), data))
        }

        @JvmStatic
        fun toResponseEntity(apiResponse: ApiResponse): ResponseEntity<ApiResponse> {
            return ResponseEntity
                .status(apiResponse.code.status)
                .body(apiResponse)
        }
    }

}
