package io.glory.core.util.datetime

import java.time.LocalDate
import java.time.Period
import java.time.temporal.ChronoUnit

private val DEFAULT_SEARCH_PERIOD: Period = Period.ofDays(1)
private val DEFAULT_MAX_SEARCH_PERIOD: Period = Period.ofMonths(1)

data class SearchDates @JvmOverloads constructor(
    var startDate: LocalDate = LocalDate.now().minus(DEFAULT_SEARCH_PERIOD),
    var endDate: LocalDate = LocalDate.now(),
    val strict: Boolean = false,
    val searchPeriod: Period = DEFAULT_SEARCH_PERIOD,
    val maxSearchPeriod: Period = DEFAULT_MAX_SEARCH_PERIOD
) {

    init {
        if (startDate.isAfter(endDate)) {
            require(!strict) { "startDate must be before endDate" }
            startDate = endDate.minus(searchPeriod)
        }

        val maxSearchDays = ChronoUnit.DAYS.between(
            LocalDate.of(0, 1, 1),
            LocalDate.of(0, 1, 1).plus(maxSearchPeriod)
        )
        val searchDays = ChronoUnit.DAYS.between(startDate, endDate)
        if (searchDays > maxSearchDays) {
            require(!strict) { "Search period must not exceed $maxSearchPeriod" }
            startDate = endDate.minus(maxSearchPeriod)
        }
    }

}