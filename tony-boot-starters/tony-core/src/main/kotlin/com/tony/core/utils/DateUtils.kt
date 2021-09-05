@file:Suppress("unused", "FunctionName")
@file:JvmName("DateUtils")

package com.tony.core.utils

import com.tony.core.exception.ApiException
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAccessor
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.WeakHashMap

@JvmField
val defaultZoneId: ZoneId = TimeZone.getDefault().toZoneId()

@JvmField
val defaultZoneOffset: ZoneOffset = OffsetDateTime.now(defaultZoneId).offset

@JvmField
@JvmSynthetic
internal val dateTimeFormatterMap: WeakHashMap<String, DateTimeFormatter> = WeakHashMap()

@JvmSynthetic
internal fun dateTimeFormatterWithDefaultOptions(pattern: String) =
    DateTimeFormatter
        .ofPattern(pattern)
        .withLocale(Locale.getDefault())
        .withZone(defaultZoneId)

fun TemporalAccessor.toString(pattern: String): String =
    dateTimeFormatterMap.getOrPut(pattern) {
        dateTimeFormatterWithDefaultOptions(pattern)
    }.format(this)

fun String.toDate(pattern: String): Date =
    dateTimeFormatterMap.getOrPut(pattern) {
        dateTimeFormatterWithDefaultOptions(pattern)
    }.parse(this).toDate()

fun String.toLocalDate(pattern: String): LocalDate =
    dateTimeFormatterMap.getOrPut(pattern) {
        dateTimeFormatterWithDefaultOptions(pattern)
    }.parse(this).run {
        LocalDate.from(this)
    }

fun String.toLocalDateTime(pattern: String): LocalDateTime =
    dateTimeFormatterMap.getOrPut(pattern) {
        dateTimeFormatterWithDefaultOptions(pattern)
    }.parse(this).run {
        LocalDateTime.from(this)
    }

fun TemporalAccessor.toDate(): Date = LocalDateTime.from(this).toDate()

fun Date.toLocalDate(): LocalDate = toInstant().atZone(defaultZoneId).toLocalDate()

fun Date.toLocalDateTime(): LocalDateTime = toInstant().atZone(defaultZoneId).toLocalDateTime()

fun LocalDate.toDate(): Date = Date.from(atStartOfDay(defaultZoneId).toInstant())

fun LocalDateTime.toDate(): Date = Date.from(atZone(defaultZoneId).toInstant())

fun LocalDateTime.toInstant(): Instant = toInstant(defaultZoneOffset)

fun secondOfTodayRest() =
    ChronoUnit.SECONDS.between(
        LocalDateTime.now(),
        LocalDateTime.now().with(LocalTime.MAX)
    )

fun LocalDateTime.isBetween(
    start: LocalDateTime?,
    end: LocalDateTime?
) = if (start == null || end == null) {
    throw ApiException("start or end is null,start:$start end:$end")
} else isAfter(start) && isBefore(end)

fun LocalDate.isBetween(
    start: LocalDate?,
    end: LocalDate?
) = if (start == null || end == null) {
    throw ApiException("start or end is null,start:$start end:$end")
} else isAfter(start) && isBefore(end)

fun LocalDate.atEndOfDay(): LocalDateTime = LocalDateTime.of(this, LocalTime.MAX)
