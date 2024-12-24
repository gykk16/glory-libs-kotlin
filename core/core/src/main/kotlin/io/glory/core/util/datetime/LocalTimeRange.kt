package io.glory.core.util.datetime

import java.time.Duration
import java.time.LocalTime
import java.time.temporal.ChronoUnit

@JvmRecord
data class LocalTimeRange private constructor(
    val start: LocalTime,
    val end: LocalTime
) {

    init {
        require(!start.isAfter(end)) {
            "start must be before end, start: $start , end: $end"
        }
    }

    /**
     * @return a [LocalTimeRange] with the specified [start] and [end] times
     */
    companion object {
        @JvmStatic
        fun from(start: LocalTime, end: LocalTime): LocalTimeRange {
            return LocalTimeRange(start, end)
        }

        operator fun invoke(start: LocalTime, end: LocalTime): LocalTimeRange {
            return from(start, end)
        }
    }

    /**
     * @return true if the specified [time] is within the range.
     * The start time and end time are included.
     */
    fun contains(time: LocalTime): Boolean {
        return !time.isBefore(start) && !time.isAfter(end)
    }

    /**
     * @return [Duration] consisting of the number of hours, minutes, and seconds between two times.
     * The start time is inclusive and the end time is exclusive
     */
    fun difference(): Duration {
        return Duration.between(start, end)
    }

    /**
     * @return the number of hours between [start] and [end] dates
     */
    fun differenceInHours(): Long {
        return ChronoUnit.HOURS.between(start, end)
    }

    /**
     * @return the number of minutes between [start] and [end] dates
     */
    fun differenceInMin(): Long {
        return ChronoUnit.MINUTES.between(start, end)
    }

}