@file:Suppress("unused", "FunctionName")
@file:JvmName("DateUtils")

package com.tony.core.utils

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
internal val dateTimeFormatterMap: WeakHashMap<String, DateTimeFormatter> =
    WeakHashMap()

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

fun TemporalAccessor.toDate(): Date = LocalDateTime.from(this).toDate()

fun Date.toLocalDate(): LocalDate = toInstant().atZone(defaultZoneId).toLocalDate()
fun Date.toLocalDateTime(): LocalDateTime = toInstant().atZone(defaultZoneId).toLocalDateTime()

fun LocalDate.toDate(): Date = Date.from(atStartOfDay(defaultZoneId).toInstant())
fun LocalDateTime.toDate(): Date = Date.from(atZone(defaultZoneId).toInstant())

fun secondOfTodayRest() =
    ChronoUnit.SECONDS.between(
        LocalDateTime.now(),
        LocalDateTime.now().with(LocalTime.MAX))