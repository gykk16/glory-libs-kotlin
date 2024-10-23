package io.glory.core.util.extensions

import org.assertj.core.api.Assertions.assertThat
import java.time.LocalDate
import kotlin.test.Test

class LocalDateExtensionsTest {

    @Test
    fun `is today`(): Unit {
        // given
        val today = LocalDate.now()

        // when
        val result = today.isToday()

        // then
        assertThat(result).isTrue()
    }

    @Test
    fun `is yesterday`(): Unit {
        // given
        val yesterday = LocalDate.now().minusDays(1)

        // when
        val result = yesterday.isYesterday()

        // then
        assertThat(result).isTrue()
    }

    @Test
    fun `is tomorrow`(): Unit {
        // given
        val tomorrow = LocalDate.now().plusDays(1)

        // when
        val result = tomorrow.isTomorrow()

        // then
        assertThat(result).isTrue()
    }

    @Test
    fun `is past`(): Unit {
        // given
        val past = LocalDate.now().minusDays(1)

        // when
        val result = past.isPast()

        // then
        assertThat(result).isTrue()
    }

    @Test
    fun `is future`(): Unit {
        // given
        val future = LocalDate.now().plusDays(1)

        // when
        val result = future.isFuture()

        // then
        assertThat(result).isTrue()
    }

}