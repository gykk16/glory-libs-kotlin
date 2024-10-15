package io.glory.core.util.datetime;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DateFormatterJTest {

    @DisplayName("convert string to LocalTime")
    @Test
    void convertToLocalTime() throws Exception {
        // given
        String target = "12:34:56";
        var expected = LocalTime.of(12, 34, 56);

        // when
        var actual = DateFormatter.toTime(target);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("convert string to LocalTime with format")
    @Test
    void convertToLocalTimeWithFormat() throws Exception {
        // given
        String format = "HHmmss";
        String target = "123456";
        var expected = LocalTime.of(12, 34, 56);

        // when
        var actual = DateFormatter.toTime(target, format);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("convert LocalTime to string")
    @Test
    void convertToStringFromLocalTime() throws Exception {
        // given
        var target = LocalTime.of(12, 34, 56);
        String expected = "12:34:56";

        // when
        String actual = DateFormatter.toStr(target);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("convert LocalTime to string with format")
    @Test
    void convertToStringFromLocalTimeWithFormat() throws Exception {
        // given
        String format = "HH:mm:ss";
        var target = LocalTime.of(12, 34, 56);
        String expected = "12:34:56";

        // when
        String actual = DateFormatter.toStr(target, format);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("convert string to LocalDate")
    @Test
    void convertToLocalDate() throws Exception {
        // given
        String target = "2024-01-01";
        var expected = LocalDate.of(2024, 1, 1);

        // when
        var actual = DateFormatter.toDate(target);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("convert string to LocalDate with format")
    @Test
    void convertToLocalDateWithFormat() throws Exception {
        // given
        String format = "yyyyMMdd";
        String target = "20240101";
        var expected = LocalDate.of(2024, 1, 1);

        // when
        var actual = DateFormatter.toDate(target, format);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("convert LocalDate to string")
    @Test
    void convertToStringFromLocalDate() throws Exception {
        // given
        var target = LocalDate.of(2024, 1, 1);
        String expected = "2024-01-01";

        // when
        String actual = DateFormatter.toStr(target);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("convert LocalDate to string with format")
    @Test
    void convertToStringFromLocalDateWithFormat() throws Exception {
        // given
        String format = "yyyyMMdd";
        var target = LocalDate.of(2024, 1, 1);
        String expected = "20240101";

        // when
        String actual = DateFormatter.toStr(target, format);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("convert string to LocalDateTime")
    @Test
    void convertToLocalDateTime() throws Exception {
        // given
        String target = "2024-01-01 12:34:56";
        var expected = LocalDateTime.of(2024, 1, 1, 12, 34, 56);

        // when
        var actual = DateFormatter.toDateTime(target);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("convert string to LocalDateTime with format")
    @Test
    void convertToLocalDateTimeWithFormat() throws Exception {
        // given
        String format = "yyyyMMddHHmmss";
        String target = "20240101123456";
        var expected = LocalDateTime.of(2024, 1, 1, 12, 34, 56);

        // when
        var actual = DateFormatter.toDateTime(target, format);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("convert LocalDateTime to string")
    @Test
    void convertToStringFromLocalDateTime() throws Exception {
        // given
        var target = LocalDateTime.of(2024, 1, 1, 12, 34, 56);
        String expected = "2024-01-01 12:34:56";

        // when
        String actual = DateFormatter.toStr(target);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("convert LocalDateTime to string with format")
    @Test
    void convertToStringFromLocalDateTimeWithFormat() throws Exception {
        // given
        String format = "yyyyMMddHHmmss";
        var target = LocalDateTime.of(2024, 1, 1, 12, 34, 56);
        String expected = "20240101123456";

        // when
        String actual = DateFormatter.toStr(target, format);

        // then
        assertThat(actual).isEqualTo(expected);
    }

}
