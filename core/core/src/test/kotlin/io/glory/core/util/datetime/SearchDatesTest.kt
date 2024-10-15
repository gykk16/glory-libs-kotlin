package io.glory.core.util.datetime

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import java.time.Period
import kotlin.test.Test

class SearchDatesTest {

    private val startDate = LocalDate.now().minusDays(10)
    private val endDate = LocalDate.now().minusDays(5)
    private val searchPeriod = Period.ofDays(1)
    private val maxSearchPeriod = Period.ofMonths(1)

    @Test
    fun `init SearchDatesKt with default parameters`(): Unit {
        // given
        val startDate = LocalDate.now().minusDays(1)
        val endDate = LocalDate.now()
        val strict = false
        val searchPeriod = Period.ofDays(1)
        val maxSearchPeriod = Period.ofMonths(1)

        // when
        val searchDates = SearchDates()

        // then
        println("searchDates = $searchDates")
        assertThat(searchDates).isNotNull()
            .extracting(
                SearchDates::startDate,
                SearchDates::endDate,
                SearchDates::strict,
                SearchDates::searchPeriod,
                SearchDates::maxSearchPeriod
            )
            .containsExactly(startDate, endDate, strict, searchPeriod, maxSearchPeriod)
    }

    @Test
    fun `init SearchDatesKt with parameters`(): Unit {
        // given

        // when
        val searchDates = successSearchDatesStrict()

        // then
        assertThat(searchDates).isNotNull()
            .extracting(
                SearchDates::startDate,
                SearchDates::endDate,
                SearchDates::strict,
                SearchDates::searchPeriod,
                SearchDates::maxSearchPeriod
            )
            .containsExactly(startDate, endDate, true, searchPeriod, maxSearchPeriod)
    }

    @Test
    fun `when endDate is before startDate throws IllegalArgumentException`(): Unit {
        // given
        val invalidEndDate = startDate.minusDays(1)

        // when then
        assertThrows<IllegalArgumentException> {
            val searchDates = successSearchDatesStrict().copy(endDate = invalidEndDate)
        }.message?.contains("startDate must be before endDate")
    }

    @Test
    fun `when search period exceeds maxSearchPeriod throws IllegalArgumentException`(): Unit {
        // given
        val invalidEndDate = startDate.plus(maxSearchPeriod).plusDays(2)

        // when then
        assertThrows<IllegalArgumentException> {
            val searchDates = successSearchDatesStrict().copy(endDate = invalidEndDate)
        }.message?.contains("Search period must not exceed $maxSearchPeriod")
    }

    @Test
    fun `when endDate is before startDate in non strict startDate is arranged`(): Unit {
        // given
        val invalidEndDate = startDate.minusDays(1)
        val expectedStartDate = invalidEndDate.minus(searchPeriod)

        // when
        val searchDates = successSearchDatesNonStrict().copy(endDate = invalidEndDate)

        // then
        println("searchDates = $searchDates")
        assertThat(searchDates).isNotNull()
            .extracting(SearchDates::startDate, SearchDates::endDate)
            .containsExactly(expectedStartDate, invalidEndDate)
    }

    @Test
    fun `when search period exceeds maxSearchPeriod in non strict startDate is arranged`(): Unit {
        // given
        val invalidEndDate = startDate.plus(maxSearchPeriod).plusDays(2)
        val expectedStartDate = invalidEndDate.minus(maxSearchPeriod)

        // when
        val searchDates = successSearchDatesNonStrict().copy(endDate = invalidEndDate)

        // then
        println("searchDates = $searchDates")
        assertThat(searchDates).isNotNull()
            .extracting(SearchDates::startDate, SearchDates::endDate)
            .containsExactly(expectedStartDate, invalidEndDate)
    }

    private fun successSearchDatesStrict(): SearchDates {
        return successSearchDatesNonStrict().copy(strict = true)
    }

    private fun successSearchDatesNonStrict(): SearchDates {
        return SearchDates(
            startDate = startDate,
            endDate = endDate,
            strict = false,
            searchPeriod = searchPeriod,
            maxSearchPeriod = maxSearchPeriod
        )
    }
}