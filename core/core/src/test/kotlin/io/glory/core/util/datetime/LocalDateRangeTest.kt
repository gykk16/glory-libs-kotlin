package io.glory.core.util.datetime

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import java.time.Period
import kotlin.test.Test

class LocalDateRangeTest {

    private val start = LocalDate.now()
    private val end = start.plus(Period.ofDays(2))

    @Test
    fun `init`(): Unit {
        // given

        // when
        val range = LocalDateRange(start, end)
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
            val date = start.plusDays(i)
            val contains = range.contains(date)

            // then
            assertThat(contains).isTrue
        }
    }

    @Test
    fun `not contains`(): Unit {
        // given
        val range = rangeOfSuccess()
        val date = end.plusDays(1)

        // when
        val contains = range.contains(date)

        // then
        assertThat(contains).isFalse
    }

    @Test
    fun `difference`(): Unit {
        // given
        val range = rangeOfSuccess()
        val expected = Period.between(start, end)

        // when
        val difference = range.difference()

        // then
        assertThat(difference).isEqualTo(expected)
    }

    @Test
    fun `differenceInDays`(): Unit {
        // given
        val expectedDays = 40L
        val targetDate = start.plusDays(expectedDays)
        val range = LocalDateRange(start, targetDate)

        // when
        val differenceInDays = range.differenceInDays()

        // then
        assertThat(differenceInDays).isEqualTo(expectedDays)
    }

    @Test
    fun `when end is before start throws IllegalArgumentException`(): Unit {
        // given
        val invalidStart = end.plusDays(1)

        // when then
        assertThrows<IllegalArgumentException> {
            val range = rangeOfSuccess().copy(start = invalidStart)
        }.message?.contains("start must be before end, start: $start , end: $invalidStart")
    }

    private fun rangeOfSuccess(): LocalDateRange {
        return LocalDateRange(start, end)
    }

}