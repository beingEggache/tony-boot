@file:Suppress("unused")
@file:JvmName("DateUtils")

package com.tony.utils

import com.tony.exception.ApiException
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
public val defaultZoneId: ZoneId = TimeZone.getDefault().toZoneId()

@JvmField
public val defaultZoneOffset: ZoneOffset = OffsetDateTime.now(defaultZoneId).offset

@JvmField
@JvmSynthetic
internal val dateTimeFormatterMap: WeakHashMap<String, DateTimeFormatter> = WeakHashMap()

@JvmSynthetic
internal fun dateTimeFormatterWithDefaultOptions(pattern: String) =
    DateTimeFormatter
        .ofPattern(pattern)
        .withLocale(Locale.getDefault())
        .withZone(defaultZoneId)

public fun TemporalAccessor.toString(pattern: String): String =
    dateTimeFormatterMap.getOrPut(pattern) {
        dateTimeFormatterWithDefaultOptions(pattern)
    }.format(this)

public fun String.toDate(pattern: String): Date =
    dateTimeFormatterMap.getOrPut(pattern) {
        dateTimeFormatterWithDefaultOptions(pattern)
    }.parse(this).toDate()

public fun String.toLocalDate(pattern: String): LocalDate =
    dateTimeFormatterMap.getOrPut(pattern) {
        dateTimeFormatterWithDefaultOptions(pattern)
    }.parse(this).run {
        LocalDate.from(this)
    }

public fun String.toLocalDateTime(pattern: String): LocalDateTime =
    dateTimeFormatterMap.getOrPut(pattern) {
        dateTimeFormatterWithDefaultOptions(pattern)
    }.parse(this).run {
        LocalDateTime.from(this)
    }

public fun TemporalAccessor.toDate(): Date = LocalDateTime.from(this).toDate()

public fun Date.toLocalDate(): LocalDate = toInstant().atZone(defaultZoneId).toLocalDate()

public fun Date.toLocalDateTime(): LocalDateTime = toInstant().atZone(defaultZoneId).toLocalDateTime()

public fun LocalDate.toDate(): Date = Date.from(atStartOfDay(defaultZoneId).toInstant())

public fun LocalDateTime.toDate(): Date = Date.from(atZone(defaultZoneId).toInstant())

public fun LocalDateTime.toInstant(): Instant = toInstant(defaultZoneOffset)

public fun secondOfTodayRest(): Long =
    ChronoUnit.SECONDS.between(
        LocalDateTime.now(),
        LocalDateTime.now().with(LocalTime.MAX),
    )

public fun LocalDateTime.isBetween(
    start: LocalDateTime?,
    end: LocalDateTime?,
): Boolean = if (start == null || end == null) {
    throw ApiException("start or end is null,start:$start end:$end")
} else {
    isAfter(start) && isBefore(end)
}

public fun LocalDate.isBetween(
    start: LocalDate?,
    end: LocalDate?,
): Boolean = if (start == null || end == null) {
    throw ApiException("start or end is null,start:$start end:$end")
} else {
    isAfter(start) && isBefore(end)
}

public fun LocalDate.atEndOfDay(): LocalDateTime = LocalDateTime.of(this, LocalTime.MAX)
