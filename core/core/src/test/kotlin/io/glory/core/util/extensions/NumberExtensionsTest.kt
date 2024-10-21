package io.glory.core.util.extensions

import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

class NumberExtensionsTest {

    @Test
    fun `get zero or one`(): Unit {
        // given

        // when
        IntRange(1, 10).forEach {
            val result = it.zeroOrOne()
            // then
            assertThat(result).isIn(0, 1)
        }
    }

    @Test
    fun `get zero or one or two`(): Unit {
        // given

        // when
        IntRange(1, 20).forEach {
            val result = it.zeroOrOneOrTwo()
            // then
            assertThat(result).isIn(0, 1, 2)
        }
    }

    @Test
    fun `get random integer between min and max`(): Unit {
        // given
        val min = 10
        val max = 200

        // when
        for (i in 1..100) {
            val result = 0.randomBetween(min, max)
            // then
            assertThat(result).isBetween(min, max)
        }
    }

    @Test
    fun `get random integer of length`(): Unit {
        // given
        val minLength = 1
        val maxLength = Int.MAX_VALUE.toString().length

        // when
        for (i in 1..100) {
            val ofMin = 0.randomOfLength(minLength)
            val ofMax = 0.randomOfLength(maxLength)
            // then
            assertThat(ofMin.toString().length).isEqualTo(minLength)
            assertThat(ofMax.toString().length).isEqualTo(maxLength)
        }
    }

    @Test
    fun `when invalid length for randomOfLength throws IllegalArgumentException`(): Unit {
        // given
        val invalidLength = Int.MAX_VALUE.toString().length + 1
        val invalidLength2 = 0

        // when then
        org.junit.jupiter.api.assertThrows<IllegalArgumentException> {
            0.randomOfLength(invalidLength)
        }
        org.junit.jupiter.api.assertThrows<IllegalArgumentException> {
            0.randomOfLength(invalidLength2)
        }
    }

}