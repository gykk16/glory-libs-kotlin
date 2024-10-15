package io.glory.core.util.datetime

import java.time.Duration
import java.time.LocalDateTime

@JvmRecord
data class LocalDateTimeRange private constructor(
    val start: LocalDateTime,
    val end: LocalDateTime
) {

    init {
        require(!start.isAfter(end)) {
            "start must be before end, start: $start , end: $end"
        }
    }

    /**
     * @return a [LocalDateTimeRange] with the specified [start] and [end] datetime
     */
    companion object {
        @JvmStatic
        fun from(start: LocalDateTime, end: LocalDateTime): LocalDateTimeRange {
            return LocalDateTimeRange(start, end)
        }

        operator fun invoke(start: LocalDateTime, end: LocalDateTime): LocalDateTimeRange {
            return from(start, end)
        }
    }

    /**
     * @return true if the specified [dateTime] is within the range.
     * The start date and end date are included.
     */
    fun contains(dateTime: LocalDateTime): Boolean {
        return !dateTime.isBefore(start) && !dateTime.isAfter(end)
    }

    /**
     * @return [Duration] consisting of the number of years, months, days between two datetime.
     * The start date is inclusive and the end date is exclusive
     */
    fun difference(): Duration {
        return Duration.between(start, end)
    }

}