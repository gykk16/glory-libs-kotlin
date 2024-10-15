package io.glory.core.util.datetime

import io.glory.core.util.datetime.DateFormatter.toDate
import io.glory.core.util.datetime.DateFormatter.toDateTime
import io.glory.core.util.datetime.DateFormatter.toStr
import io.glory.core.util.datetime.DateFormatter.toTime
import org.assertj.core.api.Assertions.assertThat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.test.Test

class DateFormatterTest {

    @Test
    fun `convert string to LocalTime`(): Unit {
        // given
        val target = "12:34:56"
        val expected = LocalTime.of(12, 34, 56)

        // when
        val actual = target.toTime()

        // then
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `convert string to LocalTime with format`(): Unit {
        // given
        val format = "HHmmss"
        val target = "123456"
        val expected = LocalTime.of(12, 34, 56)

        // when
        val actual = target.toTime(format)

        // then
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `convert LocalTime to String`(): Unit {
        // given
        val target = LocalTime.of(12, 34, 56)
        val expected = "12:34:56"

        // when
        val actual = target.toStr()

        // then
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `convert LocalTime to String with format`(): Unit {
        // given
        val format = "HHmmss"
        val target = LocalTime.of(12, 34, 56)
        val expected = "123456"

        // when
        val actual = target.toStr(format = format)

        // then
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `convert string to LocalDate`(): Unit {
        // given
        val target = "2024-01-01"
        val expected = LocalDate.of(2024, 1, 1)

        // when
        val actual = target.toDate()

        // then
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `convert string to LocalDate with format`(): Unit {
        // given
        val format = "yyyyMMdd"
        val target = "20240101"
        val expected = LocalDate.of(2024, 1, 1)

        // when
        val actual = target.toDate(format)

        // then
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `convert LocalDate to String`(): Unit {
        // given
        val target = LocalDate.of(2024, 1, 1)
        val expected = "2024-01-01"

        // when
        val actual = target.toStr()

        // then
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `convert LocalDate to String with format`(): Unit {
        // given
        val format = "yyyyMMdd"
        val target = LocalDate.of(2024, 1, 1)
        val expected = "20240101"

        // when
        val actual = target.toStr(format = format)

        // then
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `convert string to LocalDateTime`(): Unit {
        // given
        val target = "2024-01-01 12:34:56"
        val expected = LocalDateTime.of(2024, 1, 1, 12, 34, 56)

        // when
        val actual = target.toDateTime()

        // then
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `convert string to LocalDateTime with format`(): Unit {
        // given
        val format = "yyyyMMddHHmmss"
        val target = "20240101123456"
        val expected = LocalDateTime.of(2024, 1, 1, 12, 34, 56)

        // when
        val actual = target.toDateTime(format)

        // then
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `convert LocalDateTime to String`(): Unit {
        // given
        val target = LocalDateTime.of(2024, 1, 1, 12, 34, 56)
        val expected = "2024-01-01 12:34:56"

        // when
        val actual = target.toStr()

        // then
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `convert LocalDateTime to String with format`(): Unit {
        // given
        val format = "yyyyMMddHHmmss"
        val target = LocalDateTime.of(2024, 1, 1, 12, 34, 56)
        val expected = "20240101123456"

        // when
        val actual = target.toStr(format = format)

        // then
        assertThat(actual).isEqualTo(expected)
    }

}