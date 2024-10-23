@file:JvmName("LocalDateExt")

package io.glory.core.util.extensions

import java.time.LocalDate

/**
 * @return true if this date is today
 */
fun LocalDate.isToday(): Boolean = this == LocalDate.now()

/**
 * @return true if this date is yesterday
 */
fun LocalDate.isYesterday(): Boolean = this == LocalDate.now().minusDays(1)

/**
 * @return true if this date is tomorrow
 */
fun LocalDate.isTomorrow(): Boolean = this == LocalDate.now().plusDays(1)

/**
 * @return true if this date is in the past
 */
fun LocalDate.isPast(): Boolean = this < LocalDate.now()

/**
 * @return true if this date is in the future
 */
fun LocalDate.isFuture(): Boolean = this > LocalDate.now()
