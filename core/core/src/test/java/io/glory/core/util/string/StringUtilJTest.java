package io.glory.core.util.string;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

 class StringUtilJTest {

    @DisplayName("remove all spaces from string")
    @Test
    void removeAllSpaces() throws Exception {
        // given
        String input = "  a b c  ";
        String expected = "abc";

        // when
        String actual = StringUtil.removeAllSpaces(input);

        // then
        assertThat(actual).isEqualTo(expected);
    }

}
