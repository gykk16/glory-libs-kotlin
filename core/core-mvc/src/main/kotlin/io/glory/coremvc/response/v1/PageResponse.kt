package io.glory.coremvc.response.v1

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.domain.Page

/**
 * Standard page response format
 *
 * @param content page content
 * @param pageInfo page information
 */
data class PageResponse(
    @JsonProperty("content") val content: Collection<Any>,
    @JsonProperty("pageInfo") val pageInfo: PageInfo
) {

    /**
     * returns [PageResponse] instance from [Page]
     */
    constructor(page: Page<*>) : this(
        content = page.content,
        pageInfo = PageInfo(page)
    )

    /**
     * Page information
     * @param totalPages total pages
     * @param totalElements total elements
     * @param pageNumber page number
     * @param pageElements page elements
     * @param isFirst is first page
     * @param isLast is last page
     * @param isEmpty is empty page
     */
    data class PageInfo(
        @JsonProperty("totalPages") val totalPages: Int,
        @JsonProperty("totalElements") val totalElements: Long,
        @JsonProperty("pageNumber") val pageNumber: Int,
        @JsonProperty("pageElements") val pageElements: Int,
        @JsonProperty("isFirst") val isFirst: Boolean,
        @JsonProperty("isLast") val isLast: Boolean,
        @JsonProperty("isEmpty") val isEmpty: Boolean
    ) {

        /**
         * returns [PageInfo] instance from [Page]
         */
        constructor(page: Page<*>) : this(
            totalPages = page.totalPages,
            totalElements = page.totalElements,
            pageNumber = page.number,
            pageElements = page.numberOfElements,
            isFirst = page.isFirst,
            isLast = page.isLast,
            isEmpty = page.isEmpty
        )
    }

}