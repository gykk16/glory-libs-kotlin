package io.glory.coremvc

const val X_TRACE_KEY = "X-Trace-Id"

val FILTER_EXCLUDE_PATH = arrayOf("/css/", "/js/", "/img/", "/images/", "/error/", "/download/", ".ico")

val INTERCEPTOR_EXCLUDE_PATH = listOf(
    "/css/**", "/js/**", "/img/**", "/images/**", "/error/**", "/download/**", "/common/file**", "/*.ico"
)
