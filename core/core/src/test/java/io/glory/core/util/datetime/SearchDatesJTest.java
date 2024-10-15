package io.glory.core.util.datetime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.Period;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SearchDatesJTest {

    private final LocalDate startDate       = LocalDate.now().minusDays(10);
    private final LocalDate endDate         = LocalDate.now().minusDays(5);
    private final Period    searchPeriod    = Period.ofDays(1);
    private final Period    maxSearchPeriod = Period.ofMonths(1);

    @DisplayName("init default SearchDatesKt")
    @Test
    void initDefaultSearchDatesKt() {
        // given

        // when
        SearchDates searchDates = new SearchDates();

        // then
        System.out.println("==> searchDates = " + searchDates);
        assertThat(searchDates).isNotNull()
                .extracting(
                        SearchDates::getStartDate,
                        SearchDates::getEndDate,
                        SearchDates::getStrict,
                        SearchDates::getSearchPeriod,
                        SearchDates::getMaxSearchPeriod
                )
                .containsExactly(
                        LocalDate.now().minusDays(1),
                        LocalDate.now(),
                        false,
                        Period.ofDays(1),
                        Period.ofMonths(1)
                );
    }

    @DisplayName("init SearchDatesKt with parameters")
    @Test
    void initSearchDatesKtWithParameters() {
        // given

        // when
        SearchDates searchDates = successSearchDatesStrictMode(endDate);

        // then
        assertThat(searchDates).isNotNull()
                .extracting(
                        SearchDates::getStartDate,
                        SearchDates::getEndDate,
                        SearchDates::getStrict,
                        SearchDates::getSearchPeriod,
                        SearchDates::getMaxSearchPeriod
                )
                .containsExactly(startDate, endDate, true, searchPeriod, maxSearchPeriod);
    }

    @DisplayName("when endDate is before startDate throws IllegalArgumentException")
    @Test
    void whenEndDateIsBeforeStartDate_ThrowsIllegalArgumentException() {
        // given
        LocalDate invalidEndDate = startDate.minusDays(1);

        // when
        assertThatThrownBy(() -> successSearchDatesStrictMode(invalidEndDate))
                // then
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("startDate must be before endDate");
    }

    @DisplayName("when searchPeriod is greater than maxSearchPeriod throws IllegalArgumentException")
    @Test
    void whenSearchPeriodExceedsMaxSearchPeriod_ThrowsIllegalArgumentException() {
        // given
        LocalDate invalidEndDate = startDate.plus(maxSearchPeriod).plusDays(2);

        // when
        assertThatThrownBy(() -> successSearchDatesStrictMode(invalidEndDate))
                // then
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Search period must not exceed " + maxSearchPeriod);
    }

    private SearchDates successSearchDatesStrictMode(LocalDate _endDate) {
        return new SearchDates(
                startDate,
                _endDate,
                true,
                searchPeriod,
                maxSearchPeriod
        );
    }

}
