package io.glory.coremvc.response.v2

import com.fasterxml.jackson.annotation.JsonProperty
import io.glory.core.codes.ErrorCode.SERVER_ERROR
import io.glory.core.codes.ResponseCode
import io.glory.core.codes.SuccessCode.SUCCESS
import io.glory.coremvc.response.v1.NoOffsetPageResponse.OffsetInfo
import io.glory.coremvc.response.v1.PageResponse.PageInfo
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity

/**
 * Standard api resource format
 *
 * @param status response status
 * @param meta response meta
 * @param data response data
 */
data class ApiResource(
    @JsonProperty("status")
    val status: Status,
    @JsonProperty("meta")
    val meta: Meta,
    @JsonProperty("data")
    val data: Any,
) {

    companion object {

        @JvmStatic
        @JvmOverloads
        fun success(data: Any = SUCCESS.message): ResponseEntity<ApiResource> {
            return of(SUCCESS, data)
        }

        @JvmStatic
        @JvmOverloads
        fun error(data: Any = SERVER_ERROR.message): ResponseEntity<ApiResource> {
            return of(SERVER_ERROR, data)
        }

        @JvmStatic
        fun of(data: Any): ResponseEntity<ApiResource> {
            return of(SUCCESS, data)
        }

        @JvmStatic
        @JvmOverloads
        fun of(code: ResponseCode, data: Any = code.message): ResponseEntity<ApiResource> {
            if (data is Map<*, *>) {
                return ofMap(data, code)
            }
            if (data is Collection<*>) {
                return ofCollection(data, code)
            }
            if (data is Page<*>) {
                return ofPage(data, code)
            }

            val status = Status.of(code)
            val meta = Meta()
            return toResponseEntity(ApiResource(status, meta, data))
        }

        @JvmStatic
        @JvmOverloads
        fun ofCollection(collection: Collection<*>, code: ResponseCode = SUCCESS): ResponseEntity<ApiResource> {
            val status = Status.of(code)
            val meta = Meta(size = collection.size)
            return toResponseEntity(ApiResource(status, meta, collection))
        }

        @JvmStatic
        @JvmOverloads
        fun ofMap(map: Map<*, *>, code: ResponseCode = SUCCESS): ResponseEntity<ApiResource> {
            val status = Status.of(code)
            val meta = Meta(size = map.size)
            return toResponseEntity(ApiResource(status, meta, map))
        }

        @JvmStatic
        @JvmOverloads
        fun ofPage(page: Page<*>, code: ResponseCode = SUCCESS): ResponseEntity<ApiResource> {
            val status = Status.of(code)
            val meta = Meta(pageInfo = PageInfo(page))
            return toResponseEntity(ApiResource(status, meta, page.content))
        }

        @JvmStatic
        @JvmOverloads
        fun ofNoOffsetPage(
            page: Page<*>,
            lastIndex: String,
            code: ResponseCode = SUCCESS,
        ): ResponseEntity<ApiResource> {
            val status = Status.of(code)
            val meta = Meta(offsetInfo = OffsetInfo(lastIndex, page))
            return toResponseEntity(ApiResource(status, meta, page.content))
        }

        @JvmStatic
        fun toResponseEntity(apiResource: ApiResource): ResponseEntity<ApiResource> {
            return ResponseEntity
                .status(apiResource.status.status)
                .body(apiResource)
        }

    }

}
