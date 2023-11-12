package com.tony.test.web.req

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date

/**
 * TestDateTimesReq is
 * @author Tang Li
 * @date 2023/07/07 11:46
 */
data class TestDateTimesReq(
    val date: Date? = null,
    val localDateTime: LocalDateTime? = null,
    val localDate: LocalDate? = null,
)
