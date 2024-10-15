package io.glory.coremvc.aop.stopwatch

import io.github.oshai.kotlinlogging.KotlinLogging
import java.time.Duration
import java.time.LocalDateTime

private val logger = KotlinLogging.logger {}

/**
 * StopWatch
 * <p>log the process time of the function.
 *
 * @param function a function that takes no arguments and returns a value of type T.
 */
fun <T> stopWatch(function: () -> T) {
    stopWatch(function.javaClass.simpleName, function)
}

/**
 * StopWatch
 * <p>log the process time of the function.
 *
 * @param title the title of the process.
 * @param function a function that takes no arguments and returns a value of type T.
 */
fun <T> stopWatch(title: String, function: () -> T): T {
    val startAt = LocalDateTime.now()
    logger.info { "# $title , Start At: $startAt" }

    try {
        /* invoke function */
        return function.invoke()

    } finally {
        val endAt = LocalDateTime.now()
        logger.info { "# $title , End At  : $endAt , Process time: ${Duration.between(startAt, endAt).toMillis()}ms" }
    }

}