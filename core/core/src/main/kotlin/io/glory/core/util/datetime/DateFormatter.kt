package io.glory.core.util.datetime

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

private const val TIME_FORMAT = "HH:mm:ss"
private const val DATE_FORMAT = "yyyy-MM-dd"
private const val DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss"

object DateFormatter {

    /**
     * @return [LocalTime] from the provided time string and [format]
     */
    @JvmStatic
    @JvmOverloads
    fun String.toTime(format: String = TIME_FORMAT): LocalTime {
        val formatter = DateTimeFormatter.ofPattern(format)
        return LocalTime.parse(this, formatter)
    }

    /**
     * @return [LocalDate] from the provided date string and [format]
     */
    @JvmStatic
    @JvmOverloads
    fun String.toDate(format: String = DATE_FORMAT): LocalDate {
        val formatter = DateTimeFormatter.ofPattern(format)
        return LocalDate.parse(this, formatter)
    }

    /**
     * @return [LocalDateTime] from the provided datetime string and [format]
     */
    @JvmStatic
    @JvmOverloads
    fun String.toDateTime(format: String = DATE_TIME_FORMAT): LocalDateTime {
        val formatter = DateTimeFormatter.ofPattern(format)
        return LocalDateTime.parse(this, formatter)
    }

    /**
     * @return the provided [time] as a string in the given [format]
     */
    @JvmStatic
    @JvmOverloads
    fun LocalTime.toStr(format: String = TIME_FORMAT): String {
        return this.format(DateTimeFormatter.ofPattern(format))
    }

    /**
     * @return the provided [date] as a string in the given [format]
     */
    @JvmStatic
    @JvmOverloads
    fun LocalDate.toStr(format: String = DATE_FORMAT): String {
        return this.format(DateTimeFormatter.ofPattern(format))
    }

    /**
     * @return the provided [dateTime] as a string in the given [format]
     */
    @JvmStatic
    @JvmOverloads
    fun LocalDateTime.toStr(format: String = DATE_TIME_FORMAT): String {
        return this.format(DateTimeFormatter.ofPattern(format))
    }

}
