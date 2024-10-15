package io.glory.core.util.string

object StringUtil {

    /**
     * Remove all spaces from a string
     */
    @JvmStatic
    fun String.removeAllSpaces(): String = this.replace(" ", "")

}