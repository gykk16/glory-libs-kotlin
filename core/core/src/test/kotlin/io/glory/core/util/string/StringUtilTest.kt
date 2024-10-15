package io.glory.core.util.string

import io.glory.core.util.string.StringUtil.removeAllSpaces
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

class StringUtilTest {

    @Test
    fun `remove all spaces from string`(): Unit {
        // given
        val input = "  a b  c    "
        val expected = "abc"

        // when
        val actual = input.removeAllSpaces()

        // then
        assertThat(actual).isEqualTo(expected)
    }

}