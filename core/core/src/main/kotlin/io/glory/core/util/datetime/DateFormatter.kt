package io.glory.core.util.datetime

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object DateFormatter {

    const val TIME_FORMAT = "HH:mm:ss"
    const val DATE_FORMAT = "yyyy-MM-dd"
    const val DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"

    const val NUMERIC_TIME_FORMAT = "HHmmss"
    const val NUMERIC_DATE_FORMAT = "yyyyMMdd"
    const val NUMERIC_DATE_TIME_FORMAT = "yyyyMMddHHmmss"

    const val KOREAN_TIME_FORMAT = "H시 m분"
    const val KOREAN_DATE_FORMAT = "yyyy년 M월 d일"
    const val KOREAN_DATE_TIME_FORMAT = "yyyy년 M월 d일 H시 m분"

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

    /**
     * @return the provided [time] as a string in korean format
     */
    @JvmStatic
    fun LocalTime.toKorean(): String {
        return this.toStr(KOREAN_TIME_FORMAT)
    }

    /**
     * @return the provided [date] as a string in korean format
     */
    @JvmStatic
    fun LocalDate.toKorean(): String {
        return this.toStr(KOREAN_DATE_FORMAT)
    }

    /**
     * @return the provided [dateTime] as a string in korean format
     */
    @JvmStatic
    fun LocalDateTime.toKorean(): String {
        return this.toStr(KOREAN_DATE_TIME_FORMAT)
    }

}
