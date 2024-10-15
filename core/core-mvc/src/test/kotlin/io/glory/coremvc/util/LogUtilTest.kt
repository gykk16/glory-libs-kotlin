package io.glory.coremvc.util

import LogUtil
import org.assertj.core.api.Assertions
import kotlin.test.Test

class LogUtilTest {

    @Test
    fun `log title`(): Unit {
        // given
        val title = "title"
        val expected =
            "# ============================== title                               =============================="

        // when
        val result = LogUtil.title(title)
        println("result = $result")

        // then
        Assertions.assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `log subtitle`(): Unit {
        // given
        val subtitle = "subtitle"
        val expected = "# ==================== subtitle"

        // when
        val result = LogUtil.subtitle(subtitle)
        println("result = $result")

        // then
        Assertions.assertThat(result).isEqualTo(expected)
    }
}