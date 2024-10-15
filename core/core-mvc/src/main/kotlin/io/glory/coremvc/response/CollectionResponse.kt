package io.glory.coremvc.response

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Standard collection response format
 *
 * @param totalCount total count of collection
 * @param content collection content
 * */
data class CollectionResponse(
    @JsonProperty("totalCount") val totalCount: Int,
    @JsonProperty("content") val content: Any
) {

    /**
     * returns [CollectionResponse] instance from [Collection]
     */
    constructor(content: Collection<Any>) : this(content.size, content)

    /**
     * returns [CollectionResponse] instance from [Map]
     */
    constructor(content: Map<*, *>) : this(content.size, content)

}
