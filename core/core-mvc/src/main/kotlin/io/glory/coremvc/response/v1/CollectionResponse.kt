package io.glory.coremvc.response.v1

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Standard collection response format
 *
 * @param size total count of collection
 * @param content collection content
 * */
data class CollectionResponse(
    @JsonProperty("size") val size: Int,
    @JsonProperty("content") val content: Any
) {

    /**
     * returns [CollectionResponse] instance from [Collection]
     */
    constructor(content: Collection<*>) : this(content.size, content)

    /**
     * returns [CollectionResponse] instance from [Map]
     */
    constructor(content: Map<*, *>) : this(content.size, content)

}
