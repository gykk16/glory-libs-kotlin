package io.glory.coremvc.response.v2

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import io.glory.coremvc.X_TRACE_KEY
import io.glory.coremvc.response.v1.NoOffsetPageResponse
import io.glory.coremvc.response.v1.PageResponse
import org.slf4j.MDC
import java.time.LocalDateTime
import java.util.*

/**
 * Standard meta response format
 * <p>null fields are not serialized
 *
 * @param traceId trace id
 * @param responseDt response date time
 * @param size size of data
 * @param pageInfo page information
 * @param offsetInfo offset information
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
data class Meta(
    @JsonProperty("traceId")
    val traceId: String = getTraceId(),
    @JsonFormat(pattern = "yyyy-mm-dd'T'HH:mm:ss.SSS")
    @JsonSerialize(using = LocalDateTimeSerializer::class)
    @JsonProperty("responseDt")
    val responseDt: LocalDateTime = LocalDateTime.now(),
    @JsonProperty("size")
    val size: Int? = null,
    @JsonProperty("pageInfo")
    val pageInfo: PageResponse.PageInfo? = null,
    @JsonProperty("offsetInfo")
    val offsetInfo: NoOffsetPageResponse.OffsetInfo? = null,
) {

}

fun getTraceId(): String = MDC.get(X_TRACE_KEY) ?: UUID.randomUUID().toString().substring(0, 8)
