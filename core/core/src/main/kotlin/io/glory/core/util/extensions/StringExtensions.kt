package io.glory.core.util.extensions

private const val MASK_STAR = "*"

/**
 * Remove all spaces from a string
 */
fun String.removeAllSpaces(): String = this.replace(" ", "")

/**
 * @return masked string from the string. If [strict] is true, the input string cannot be empty.
 */
@JvmOverloads
fun String.mask(strict: Boolean = false): String {
    if (strict) {
        require(this.isNotBlank()) { "Input string cannot be blank" }
    }

    val maskPattern = when (length) {
        5 -> ".(?=.)"
        6 -> ".(?=.{2})"
        7 -> ".(?=.{3})"
        8 -> ".(?=.{4})"
        9 -> "(?<=.).(?=.{4})"
        10 -> "(?<=.{2}).(?=.{4})"
        11 -> "(?<=.{3}).(?=.{4})"
        else -> "(?<=.{4}).(?=.{4})"
    }

    return if (length <= 4) {
        "****"
    } else {
        this.replace(Regex(maskPattern), MASK_STAR)
    }
}

/**
 * @return masked string from the string. If [strict] is true, the input string cannot be empty.
 */
@JvmOverloads
fun String.maskKorName(strict: Boolean = false): String {
    if (strict) {
        require(this.isNotBlank()) { "Input string cannot be blank" }
    }

    return when (length) {
        in 0..1 -> MASK_STAR.repeat(3)
        2 -> this[0] + MASK_STAR
        else -> this.replace(Regex("(?<=.).(?=.)"), MASK_STAR)
    }
}

