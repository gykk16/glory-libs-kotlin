package io.glory.core.util.datetime;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LocalDateRangeJTest {

    private LocalDate start = LocalDate.now();
    private LocalDate end   = start.plusDays(2);

    @DisplayName("init")
    @Test
    void init() throws Exception {
        // given

        // when
        LocalDateRange range = LocalDateRange.from(start, end);

        // then
        assertThat(range).isNotNull()
                .extracting("start", "end")
                .containsExactly(start, end);
    }

}
