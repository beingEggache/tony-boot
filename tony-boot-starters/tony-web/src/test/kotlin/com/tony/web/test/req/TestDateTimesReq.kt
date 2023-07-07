package com.tony.web.test.req

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date

/**
 * TestDateTimesReq is
 * @author tangli
 * @since 2023/07/07 11:46
 */
data class TestDateTimesReq(
    val date: Date = Date(),
    val localDateTime: LocalDateTime = LocalDateTime.now(),
    val localDate: LocalDate = LocalDate.now(),
)
