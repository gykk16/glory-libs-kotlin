package io.glory.core.util.extensions

import kotlin.math.pow
import kotlin.random.Random

/**
 * @return 0 or 1 randomly
 */
fun Int.zeroOrOne(): Int = Random.nextInt(2)

/**
 * @return 0 or 1 or 2 randomly
 */
fun Int.zeroOrOneOrTwo(): Int = Random.nextInt(3)

/**
 * @return random integer between [min] and [max] inclusive
 */
fun Int.randomBetween(min: Int, max: Int): Int = Random.nextInt((max - min) + 1) + min

/**
 * @param length 1 to 10
 * @return random integer of [length]
 */
fun Int.randomOfLength(length: Int): Int {
    require(length in 1..10) { "Length must be between 1 and 10." }
    val minValue = 10.0.pow(length - 1).toInt()
    val maxValue = 10.0.pow(length).toInt() - 1
    return Random.nextInt(minValue, maxValue + 1)
}
