@file:JvmName("DateUtils")

package com.tony.utils

/**
 * 日期工具类
 *
 * @author tangli
 * @since 2022/9/29 10:20
 */
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
import java.util.WeakHashMap

/**
 * 系统默认时区(ZoneOffset)
 * 如果以加或减{+|-}开头, 则创建ZoneOffset实例
 * 如果以{GMT|UTC|UT}开头, 则创建ZoneRegion实例
 * ZoneId.of(“UT+8”) 得到 ZoneRegion
 * ZoneId.of("+8") 得到ZoneOffset
 * ZoneId.of("+08:00") 得到ZoneOffset
 * ZoneId.of(“Asia/Shanghai”) 得到ZoneRegion
 */
@JvmField
public val defaultZoneOffset: ZoneOffset =
    OffsetDateTime.now(ZoneId.systemDefault()).offset

@JvmField
@JvmSynthetic
internal val dateTimeFormatterMap: WeakHashMap<String, DateTimeFormatter> = WeakHashMap()

@JvmSynthetic
internal fun dateTimeFormatterWithDefaultOptions(pattern: String) =
    DateTimeFormatter
        .ofPattern(pattern)
        .withLocale(Locale.getDefault())
        .withZone(defaultZoneOffset)

/**
 * 日期转为字符串表示
 * @param pattern 字符串格式
 * @see DateTimeFormatter
 */
public fun TemporalAccessor.toString(pattern: String): String =
    dateTimeFormatterMap.getOrPut(pattern) {
        dateTimeFormatterWithDefaultOptions(pattern)
    }.format(this)

/**
 * 字符串转 Date
 * @param pattern 字符串格式
 * @see DateTimeFormatter
 */
public fun String.toDate(pattern: String): Date =
    dateTimeFormatterMap.getOrPut(pattern) {
        dateTimeFormatterWithDefaultOptions(pattern)
    }.parse(this).toDate()

/**
 * 字符串转 LocalDate
 * @param pattern 字符串格式
 * @see DateTimeFormatter
 */
public fun String.toLocalDate(pattern: String): LocalDate =
    dateTimeFormatterMap.getOrPut(pattern) {
        dateTimeFormatterWithDefaultOptions(pattern)
    }.parse(this).run {
        LocalDate.from(this)
    }

/**
 * 字符串转 LocalDateTime
 * @param pattern 字符串格式
 * @see DateTimeFormatter
 */
public fun String.toLocalDateTime(pattern: String): LocalDateTime =
    dateTimeFormatterMap.getOrPut(pattern) {
        dateTimeFormatterWithDefaultOptions(pattern)
    }.parse(this).run {
        LocalDateTime.from(this)
    }

/**
 * LocalDate 或 LocalDatetime 转 Date
 */
internal fun TemporalAccessor.toDate(): Date = LocalDateTime.from(this).toDate()

/**
 * Date 转 LocalDate
 */
public fun Date.toLocalDate(): LocalDate =
    toInstant()
        .atOffset(defaultZoneOffset)
        .toLocalDate()

/**
 * Date 转 LocalDateTime
 */
public fun Date.toLocalDateTime(): LocalDateTime =
    toInstant()
        .atOffset(defaultZoneOffset)
        .toLocalDateTime()

/**
 * LocalDate 转 Date
 */
public fun LocalDate.toDate(): Date = Date.from(atStartOfDay(defaultZoneOffset).toInstant())

/**
 * LocalDateTime 转 Date
 */
public fun LocalDateTime.toDate(): Date = Date.from(atOffset(defaultZoneOffset).toInstant())

/**
 * LocalDateTime 转 Instant
 */
public fun LocalDateTime.toInstant(): Instant = toInstant(defaultZoneOffset)

/**
 * 获取今天所剩秒数
 */
public fun secondOfTodayRest(): Long =
    ChronoUnit.SECONDS.between(
        LocalDateTime.now(),
        LocalDateTime.now().with(LocalTime.MAX)
    )

/**
 * 判断 一个 LocalDateTime 是否在 start 和 end 之间
 * @param start
 * @param end
 */
public fun LocalDateTime.isBetween(
    start: LocalDateTime?,
    end: LocalDateTime?,
): Boolean = if (start == null || end == null) {
    throw ApiException("start or end is null,start:$start end:$end")
} else {
    isAfter(start) && isBefore(end)
}

/**
 * 判断 一个 LocalDate 是否在 start 和 end 之间
 * @param start
 * @param end
 */
public fun LocalDate.isBetween(
    start: LocalDate?,
    end: LocalDate?,
): Boolean = if (start == null || end == null) {
    throw ApiException("start or end is null,start:$start end:$end")
} else {
    isAfter(start) && isBefore(end)
}

/**
 * 获取某天的23:59:59
 */
public fun LocalDate.atEndOfDay(): LocalDateTime = LocalDateTime.of(this, LocalTime.MAX)

//TODO needTest
public class TimePeriod {

    private val start: Any
    private val end: Any

    public constructor(start: LocalDate, end: LocalDate) {
        this.start = start
        this.end = end
    }

    public constructor(start: LocalDateTime, end: LocalDateTime) {
        this.start = start
        this.end = end
    }

    public constructor(start: Date, end: Date) {
        this.start = start
        this.end = end
    }

    public constructor(start: LocalDate, end: LocalDateTime) {
        this.start = start
        this.end = end
    }

    public constructor(start: LocalDate, end: Date) {
        this.start = start
        this.end = end
    }

    public constructor(start: LocalDateTime, end: LocalDate) {
        this.start = start
        this.end = end
    }

    public constructor(start: LocalDateTime, end: Date) {
        this.start = start
        this.end = end
    }

    private fun startSeconds(time: LocalTime = LocalTime.MIN) = seconds(start, time)
    private fun endSeconds(time: LocalTime = LocalTime.MIN) = seconds(start, time)

    public fun overlapWith(another: TimePeriod): Boolean {
        val anotherStartSeconds = another.startSeconds()
        val endSeconds = if (another.start is LocalDate) endSeconds(LocalTime.MAX) else endSeconds()
        val startSeconds = startSeconds()
        val anotherEndSeconds = if (start is LocalDate) another.endSeconds(LocalTime.MAX) else another.endSeconds()

        return !(endSeconds <= anotherStartSeconds || startSeconds >= anotherEndSeconds)
    }

    private fun seconds(dateTimeObject: Any, time: LocalTime = LocalTime.MIN) = when (dateTimeObject) {
        is LocalDate -> dateTimeObject.toEpochSecond(time, defaultZoneOffset)
        is LocalDateTime -> dateTimeObject.toEpochSecond(defaultZoneOffset)
        is Date -> dateTimeObject.toLocalDateTime().toEpochSecond(defaultZoneOffset)
        else -> error("Ain't gonna happen.")
    }
}
