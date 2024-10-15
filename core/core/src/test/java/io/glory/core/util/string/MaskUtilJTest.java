package io.glory.core.util.string;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MaskUtilJTest {

    @DisplayName("mask string")
    @Test
    void mask() throws Exception {
        // given
        String value4 = "1234";
        String value6 = "123456";
        String value8 = "12345678";
        String value10 = "1234567890";
        String value12 = "123456789012";
        String value16 = "1234567890123456";

        // when
        String mask4 = MaskUtil.mask(value4);
        String mask6 = MaskUtil.mask(value6);
        String mask8 = MaskUtil.mask(value8);
        String mask10 = MaskUtil.mask(value10);
        String mask12 = MaskUtil.mask(value12);
        String mask16 = MaskUtil.mask(value16);

        // then
        assertThat(mask4).isEqualTo("****");
        assertThat(mask6).isEqualTo("****56");
        assertThat(mask8).isEqualTo("****5678");
        assertThat(mask10).isEqualTo("12****7890");
        assertThat(mask12).isEqualTo("1234****9012");
        assertThat(mask16).isEqualTo("1234********3456");
    }

    @DisplayName("when string is empty for mask in strict mode returns stars")
    @Test
    void maskEmptyString() throws Exception {
        // given
        String empty = "";

        // when
        String mask = MaskUtil.mask(empty);

        // then
        assertThat(mask).isEqualTo("****");
    }

    @DisplayName("when string is empty for mask in strict mode throws IllegalArgumentException")
    @Test
    void whenMaskEmptyInStrictMode_ThrowIllegalArgumentException() throws Exception {
        // given
        String empty = "";

        // when
        assertThatThrownBy(() -> MaskUtil.mask(empty, true))
                // then
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Input string cannot be blank");
    }

}
