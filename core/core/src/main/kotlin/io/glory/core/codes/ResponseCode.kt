package io.glory.core.codes

/**
 * Response code interface
 */
interface ResponseCode {

    /**
     * http status code
     */
    val status: Int

    /**
     * response message
     */
    val message: String

    /**
     * response code name (Enum)
     */
    val name: String

}