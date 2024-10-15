package io.glory.coremvc.response

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.domain.Page

/**
 * Standard no offset page response format
 *
 * @param content page content
 * @param offsetInfo page information
 */
data class NoOffsetPageResponse(
    @JsonProperty("content") val content: Collection<Any>,
    @JsonProperty("offsetInfo") val offsetInfo: OffsetInfo
) {

    /**
     * returns [NoOffsetPageResponse] instance from [Page] and [lastIndex]
     */
    constructor(lastIndex: String, page: Page<*>) : this(
        content = page.content,
        offsetInfo = OffsetInfo(lastIndex, page)
    )

    /**
     * Page information
     * @param lastIndex last index
     * @param totalPages total pages
     * @param totalElements total elements
     * @param pageNumber page number
     * @param pageElements page elements
     * @param isLast is last page
     * @param isEmpty is empty page
     */
    data class OffsetInfo(
        @JsonProperty("lastIndex") val lastIndex: String,
        @JsonProperty("totalPages") val totalPages: Int,
        @JsonProperty("totalElements") val totalElements: Long,
        @JsonProperty("pageNumber") val pageNumber: Int,
        @JsonProperty("pageElements") val pageElements: Int,
        @JsonProperty("isLast") val isLast: Boolean,
        @JsonProperty("isEmpty") val isEmpty: Boolean
    ) {

        /**
         * returns [OffsetInfo] instance from [Page] and [lastIndex]
         */
        constructor(lastIndex: String, page: Page<*>) : this(
            lastIndex = lastIndex,
            totalPages = page.totalPages,
            totalElements = page.totalElements,
            pageNumber = page.number,
            pageElements = page.numberOfElements,
            isLast = page.isLast,
            isEmpty = page.isEmpty
        )
    }

}
