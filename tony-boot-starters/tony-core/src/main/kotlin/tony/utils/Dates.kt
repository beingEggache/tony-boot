/*
 * MIT License
 *
 * Copyright (c) 2023-present, tangli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

@file:JvmName("Dates")

package tony.utils

/**
 * 日期工具类
 *
 * @author tangli
 * @date 2022/09/29 19:20
 */
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAccessor
import java.util.Date
import java.util.Locale
import org.springframework.util.ConcurrentReferenceHashMap
import tony.exception.ApiException

@get:JvmSynthetic
internal val dateTimeFormatterMap: MutableMap<String, DateTimeFormatter> = ConcurrentReferenceHashMap()

@JvmSynthetic
internal fun dateTimeFormatterWithDefaultOptions(pattern: String) =
    DateTimeFormatter
        .ofPattern(pattern)
        .withLocale(Locale.getDefault())
        .withZone(ZoneId.systemDefault())

/**
 * 日期[TemporalAccessor]转为[String]表示
 * @param [pattern] 字符串格式
 * @return [String]
 * @author tangli
 * @date 2023/12/08 19:42
 */
public fun TemporalAccessor.toString(pattern: CharSequence): String =
    dateTimeFormatterMap
        .getOrPut(pattern.toString()) {
            dateTimeFormatterWithDefaultOptions(pattern.toString())
        }.format(this)

/**
 * [LocalDate] 或 [LocalDateTime] 转 [Date]
 * @return [Date]
 * @author tangli
 * @date 2023/12/08 19:42
 */
@JvmSynthetic
internal fun TemporalAccessor.toDate(): Date =
    when (this) {
        is LocalDate -> toDate()
        is LocalDateTime -> toDate()
        else -> Date.from(Instant.from(this))
    }

/**
 * [String]转 [Date]
 * @param [pattern] 字符串格式
 * @return [Date]
 * @author tangli
 * @date 2023/12/08 19:42
 */
public fun CharSequence.toDate(pattern: CharSequence): Date =
    dateTimeFormatterMap
        .getOrPut(pattern.toString()) {
            dateTimeFormatterWithDefaultOptions(pattern.toString())
        }.parse(this)
        .toDate()

/**
 * [String]转 [LocalDate]
 * @param [pattern] 字符串格式
 * @return [LocalDate]
 * @author tangli
 * @date 2023/12/08 19:43
 */
public fun CharSequence.toLocalDate(pattern: CharSequence): LocalDate =
    dateTimeFormatterMap
        .getOrPut(pattern.toString()) {
            dateTimeFormatterWithDefaultOptions(pattern.toString())
        }.parse(this)
        .run {
            LocalDate.from(this)
        }

/**
 * [String]转 [LocalDateTime]
 * @param [pattern] 字符串格式
 * @return [LocalDateTime]
 * @author tangli
 * @date 2023/12/08 19:43
 */
public fun CharSequence.toLocalDateTime(pattern: CharSequence): LocalDateTime =
    dateTimeFormatterMap
        .getOrPut(pattern.toString()) {
            dateTimeFormatterWithDefaultOptions(pattern.toString())
        }.parse(this)
        .run {
            LocalDateTime.from(this)
        }

/**
 * [LocalDateTime] 转 [Instant]
 * @return [Instant]
 * @author tangli
 * @date 2023/12/08 19:43
 */
public fun LocalDateTime.toInstant(): Instant =
    Instant.from(atZone(ZoneId.systemDefault()))

/**
 * [LocalDateTime] 转 [Date]
 * @return [Date]
 * @author tangli
 * @date 2023/12/08 19:43
 */
public fun LocalDateTime.toDate(): Date =
    Date.from(toInstant())

/**
 * 判断 一个 [LocalDateTime] 是否在 [start] 和 [end] 之间
 * @param [start] 开始
 * @param [end] 终止
 * @return [Boolean]
 * @author tangli
 * @date 2023/12/08 19:43
 */
public fun LocalDateTime.isBetween(
    start: LocalDateTime?,
    end: LocalDateTime?,
): Boolean =
    if (start == null || end == null) {
        throw ApiException("start or end is null,start:$start end:$end")
    } else {
        isAfter(start) && isBefore(end)
    }

/**
 * [LocalDate] 转 [Instant]
 * @return [Instant]
 * @author tangli
 * @date 2023/12/08 19:43
 */
public fun LocalDate.toInstant(): Instant =
    atStartOfDay(ZoneId.systemDefault()).toInstant()

/**
 * [LocalDate] 转 [Date]
 * @return [Date]
 * @author tangli
 * @date 2023/12/08 19:43
 */
public fun LocalDate.toDate(): Date =
    Date.from(toInstant())

/**
 * 判断 一个 [LocalDate] 是否在 [start] 和 [end] 之间
 * @param [start] 开始
 * @param [end] 终止
 * @return [Boolean]
 * @author tangli
 * @date 2023/12/08 19:43
 */
public fun LocalDate.isBetween(
    start: LocalDate?,
    end: LocalDate?,
): Boolean =
    if (start == null || end == null) {
        throw ApiException("start or end is null,start:$start end:$end")
    } else {
        isAfter(start) && isBefore(end)
    }

/**
 * 获取某天的23:59:59
 * @return [LocalDateTime]
 * @author tangli
 * @date 2023/12/08 19:43
 */
public fun LocalDate.atEndOfDay(): LocalDateTime =
    LocalDateTime.of(this, LocalTime.MAX)

/**
 * [Date] 转 [String]
 * @param [pattern] 图案
 * @return [String]
 * @author tangli
 * @date 2023/12/08 19:40
 */
public fun Date.toString(pattern: CharSequence): String =
    dateTimeFormatterMap
        .getOrPut(pattern.toString()) {
            dateTimeFormatterWithDefaultOptions(pattern.toString())
        }.format(toInstant())

/**
 * [Date] 转 [LocalDate]
 * @return [LocalDate]
 * @author tangli
 * @date 2023/12/08 19:43
 */
public fun Date.toLocalDate(): LocalDate =
    LocalDate.ofInstant(toInstant(), ZoneId.systemDefault())

/**
 * [Date] 转 [LocalDateTime]
 * @return [LocalDateTime]
 * @author tangli
 * @date 2023/12/08 19:43
 */
public fun Date.toLocalDateTime(): LocalDateTime =
    LocalDateTime.ofInstant(toInstant(), ZoneId.systemDefault())

/**
 * 获取今天所剩秒数
 * @return [Long]
 * @author tangli
 * @date 2023/12/08 19:43
 */
public fun secondOfTodayRest(): Long =
    ChronoUnit.SECONDS.between(
        LocalDateTime.now(),
        LocalDateTime.now().with(LocalTime.MAX)
    )

/**
 * 是否与另一时间段重叠
 * @param [timePeriod] 时间段
 * @return [Boolean]
 * @author tangli
 * @date 2023/12/04 19:33
 */
public infix fun <T : TemporalAccessor> Pair<T, T>.overlap(timePeriod: Pair<T, T>): Boolean =
    TimePeriod(this.first, this.second).overlapWith(TimePeriod(timePeriod.first, timePeriod.second))

/**
 * 是否与另一时间段重叠
 * @param [timePeriod] 时间段
 * @return [Boolean]
 * @author tangli
 * @date 2023/12/04 19:33
 */
public infix fun <T : Date> Pair<T, T>.dateOverlap(timePeriod: Pair<T, T>): Boolean =
    TimePeriod(this.first, this.second).overlapWith(TimePeriod(timePeriod.first, timePeriod.second))

/**
 * 时间段
 * @author tangli
 * @date 2023/12/08 19:44
 */
internal class TimePeriod {
    private val start: Date
    private val end: Date

    constructor(start: TemporalAccessor, end: TemporalAccessor) {
        val startDate = start.toDate()
        val endDate = end.toDate()
        if (startDate > endDate) {
            throw ApiException("The start time must before the end time")
        }
        this.start = startDate
        this.end = endDate
    }

    constructor(start: Date, end: Date) {
        if (start > end) {
            throw ApiException("The start time must before the end time")
        }
        this.start = start
        this.end = end
    }

    fun overlapWith(another: TimePeriod): Boolean =
        end > another.start && start < another.end
}
