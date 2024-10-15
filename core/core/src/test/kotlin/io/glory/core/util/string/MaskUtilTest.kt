package io.glory.core.util.string

import io.glory.core.util.string.MaskUtil.mask
import io.glory.core.util.string.MaskUtil.maskKorName
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class MaskUtilTest {

    @Test
    fun `mask string`() {
        // given
        val input4 = "1234"
        val input5 = "12345"
        val input6 = "123456"
        val input7 = "1234567"
        val input8 = "12345678"
        val input9 = "123456789"
        val input10 = "1234567890"
        val input11 = "12345678901"
        val input12 = "123456789012"
        val input16 = "1234567890123456"

        // when
        val mask4 = input4.mask()
        val mask5 = input5.mask()
        val mask6 = input6.mask()
        val mask7 = input7.mask()
        val mask8 = input8.mask()
        val mask9 = input9.mask()
        val mask10 = input10.mask()
        val mask11 = input11.mask()
        val mask12 = input12.mask()
        val mask16 = input16.mask()

        // then
        assertThat(mask4).isEqualTo("****")
        assertThat(mask5).isEqualTo("****5")
        assertThat(mask6).isEqualTo("****56")
        assertThat(mask7).isEqualTo("****567")
        assertThat(mask8).isEqualTo("****5678")
        assertThat(mask9).isEqualTo("1****6789")
        assertThat(mask10).isEqualTo("12****7890")
        assertThat(mask11).isEqualTo("123****8901")
        assertThat(mask12).isEqualTo("1234****9012")
        assertThat(mask16).isEqualTo("1234********3456")
    }

    @Test
    fun `when string is empty for mask in strict mode throws IllegalArgumentException`(): Unit {
        // given
        val empty = ""

        // when then
        assertThrows<IllegalArgumentException> {
            empty.mask(true)
        }.message?.contains("Input string cannot be empty")
    }

    @Test
    fun `when string is empty for mask in non strict mode returns stars`(): Unit {
        // given
        val empty = ""

        // when then
        assertThat(empty.mask()).isEqualTo("****")
    }

    @Test
    fun `mask korean name`() {
        // given
        val input2 = "홍길"
        val input3 = "홍길동"
        val input4 = "홍길길동"

        // when
        val mask2 = input2.maskKorName()
        val mask3 = input3.maskKorName()
        val mask4 = input4.maskKorName()

        // then
        assertThat(mask2).isEqualTo("홍*")
        assertThat(mask3).isEqualTo("홍*동")
        assertThat(mask4).isEqualTo("홍**동")
    }

    @Test
    fun `when string is empty for maskKorName in strict mode throws IllegalArgumentException`(): Unit {
        // given
        val empty = ""

        // when then
        assertThrows<IllegalArgumentException> {
            empty.maskKorName(true)
        }.message?.contains("Input string cannot be empty")
    }

    @Test
    fun `when string is empty for maskKorName in non strict mode returns stars`(): Unit {
        // given
        val empty = ""

        // when then
        assertThat(empty.maskKorName()).isEqualTo("***")
    }

}