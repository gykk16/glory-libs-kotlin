package io.glory.core.util.datetime

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertThrows
import java.time.Duration
import java.time.LocalDateTime
import kotlin.test.Test

class LocalDateTimeRangeTest {

    private val start = LocalDateTime.now()
    private val end = start.plus(Duration.ofMinutes(2))

    @Test
    fun `init`(): Unit {
        // given

        // when
        val range = LocalDateTimeRange(start, end)
        println("range = $range")

        // then
        assertThat(range).isNotNull
        assertThat(range.start).isEqualTo(start)
        assertThat(range.end).isEqualTo(end)
    }

    @Test
    fun `contains`(): Unit {
        // given
        val range = rangeOfSuccess()

        // when
        for (i in 0L..2) {
            val target = start.plusMinutes(i)
            val contains = range.contains(target)

            // then
            assertThat(contains).isTrue
        }
    }

    @Test
    fun `not contains`(): Unit {
        // given
        val range = rangeOfSuccess()
        val date = end.plusNanos(1)

        // when
        val contains = range.contains(date)

        // then
        assertThat(contains).isFalse
    }

    @Test
    fun `difference`(): Unit {
        // given
        val range = rangeOfSuccess()
        val expected = Duration.between(start, end)

        // when
        val difference = range.difference()

        // then
        assertThat(difference).isEqualTo(expected)
    }

    @Test
    fun `when end is before start throws IllegalArgumentException`(): Unit {
        // given
        val invalidStart = end.plusSeconds(1)

        // when then
        assertThrows<IllegalArgumentException> {
            val range = rangeOfSuccess().copy(start = invalidStart)
        }.message?.contains("start must be before end, start: $start , end: $invalidStart")
    }

    private fun rangeOfSuccess(): LocalDateTimeRange {
        return LocalDateTimeRange(start, end)
    }

}