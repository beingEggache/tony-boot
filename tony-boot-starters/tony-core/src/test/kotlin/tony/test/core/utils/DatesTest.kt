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

package tony.test.core.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.slf4j.LoggerFactory
import tony.core.utils.atEndOfDay
import tony.core.utils.dateOverlap
import tony.core.utils.isBetween
import tony.core.utils.overlap
import tony.core.utils.secondOfTodayRest
import tony.core.utils.toDate
import tony.core.utils.toInstant
import tony.core.utils.toLocalDate
import tony.core.utils.toLocalDateTime
import tony.core.utils.toString
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Date

/**
 * 日期工具类单元测试
 *
 * @author tangli
 * @date 2025/06/27 17:00
 */
@DisplayName("Dates工具类全量测试")
class DatesTest {
    private val logger = LoggerFactory.getLogger(DatesTest::class.java)

    @Nested
    @DisplayName("格式化与解析相关")
    inner class FormatParse {
        @Test
        @DisplayName("TemporalAccessor.toString(pattern)")
        fun testTemporalAccessorToString() {
            logger.info("测试TemporalAccessor.toString(pattern)方法")

            val ld = LocalDate.of(2024, 6, 9)
            val ldt = LocalDateTime.of(2024, 6, 9, 14, 30, 45)

            val dateStr = ld.toString("yyyy-MM-dd")
            val dateTimeStr = ldt.toString("yyyy-MM-dd HH:mm:ss")

            logger.info("LocalDate格式化: {} -> '{}'", ld, dateStr)
            logger.info("LocalDateTime格式化: {} -> '{}'", ldt, dateTimeStr)

            assertEquals("2024-06-09", dateStr)
            assertEquals("2024-06-09 14:30:45", dateTimeStr)
        }
        @Test
        @DisplayName("Date.toString(pattern)")
        fun testDateToString() {
            logger.info("测试Date.toString(pattern)方法")

            val date = Date.from(LocalDateTime.of(2024, 6, 9, 14, 30, 45).toInstant())
            val dateStr = date.toString("yyyy-MM-dd HH:mm:ss")

            logger.info("Date格式化: {} -> '{}'", date, dateStr)
            assertEquals("2024-06-09 14:30:45", dateStr)

            // 测试不同格式
            val dateStr2 = date.toString("dd/MM/yyyy")
            logger.info("Date格式化(不同格式): {} -> '{}'", date, dateStr2)
            assertEquals("09/06/2024", dateStr2)
        }
        @Test
        @DisplayName("CharSequence.toDate(pattern)")
        fun testCharSequenceToDate() {
            logger.info("测试CharSequence.toDate(pattern)方法")

            val dateStr = "2024-06-09 14:30:45"
            val date = dateStr.toDate("yyyy-MM-dd HH:mm:ss")

            logger.info("字符串转Date: '{}' -> {}", dateStr, date)
            assertNotNull(date)

            // 验证转换结果
            val ldt = date.toLocalDateTime()
            assertEquals(2024, ldt.year)
            assertEquals(6, ldt.monthValue)
            assertEquals(9, ldt.dayOfMonth)
            assertEquals(14, ldt.hour)
            assertEquals(30, ldt.minute)
            assertEquals(45, ldt.second)
        }
        @Test
        @DisplayName("CharSequence.toLocalDate(pattern)")
        fun testCharSequenceToLocalDate() {
            logger.info("测试CharSequence.toLocalDate(pattern)方法")

            val dateStr = "2024-06-09"
            val localDate = dateStr.toLocalDate("yyyy-MM-dd")

            logger.info("字符串转LocalDate: '{}' -> {}", dateStr, localDate)
            assertEquals(LocalDate.of(2024, 6, 9), localDate)

            // 测试不同格式
            val dateStr2 = "09/06/2024"
            val localDate2 = dateStr2.toLocalDate("dd/MM/yyyy")
            logger.info("字符串转LocalDate(不同格式): '{}' -> {}", dateStr2, localDate2)
            assertEquals(LocalDate.of(2024, 6, 9), localDate2)
        }
        @Test
        @DisplayName("CharSequence.toLocalDateTime(pattern)")
        fun testCharSequenceToLocalDateTime() {
            logger.info("测试CharSequence.toLocalDateTime(pattern)方法")

            val dateTimeStr = "2024-06-09 14:30:45"
            val localDateTime = dateTimeStr.toLocalDateTime("yyyy-MM-dd HH:mm:ss")

            logger.info("字符串转LocalDateTime: '{}' -> {}", dateTimeStr, localDateTime)
            assertEquals(LocalDateTime.of(2024, 6, 9, 14, 30, 45), localDateTime)

            // 测试不同格式
            val dateTimeStr2 = "09/06/2024 14:30"
            val localDateTime2 = dateTimeStr2.toLocalDateTime("dd/MM/yyyy HH:mm")
            logger.info("字符串转LocalDateTime(不同格式): '{}' -> {}", dateTimeStr2, localDateTime2)
            assertEquals(LocalDateTime.of(2024, 6, 9, 14, 30), localDateTime2)
        }
    }

    @Nested
    @DisplayName("类型转换相关")
    inner class Convert {
        @Test
        @DisplayName("LocalDateTime <-> Instant <-> Date")
        fun testLocalDateTimeToInstantAndDate() {
            logger.info("测试LocalDateTime.toInstant()方法")

            val ldt = LocalDateTime.of(2024, 6, 9, 14, 30, 45)
            val instant = ldt.toInstant()

            logger.info("LocalDateTime转Instant: {} -> {}", ldt, instant)
            assertNotNull(instant)

            // 验证转换结果
            val convertedBack = LocalDateTime.ofInstant(instant, java.time.ZoneId.systemDefault())
            assertEquals(ldt, convertedBack)
        }
        @Test
        @DisplayName("LocalDate <-> Instant <-> Date")
        fun testLocalDateToInstantAndDate() {
            logger.info("测试LocalDate.toInstant()方法")

            val ld = LocalDate.of(2024, 6, 9)
            val instant = ld.toInstant()

            logger.info("LocalDate转Instant: {} -> {}", ld, instant)
            assertNotNull(instant)

            // 验证转换结果
            val convertedBack = LocalDate.ofInstant(instant, java.time.ZoneId.systemDefault())
            assertEquals(ld, convertedBack)
        }
        @Test
        @DisplayName("Date <-> LocalDate/LocalDateTime")
        fun testDateToLocalDateAndLocalDateTime() {
            logger.info("测试Date.toLocalDate()方法")

            val ldt = LocalDateTime.of(2024, 6, 9, 14, 30, 45)
            val date = ldt.toDate()
            val convertedBack = date.toLocalDate()

            logger.info("Date转LocalDate: {} -> {}", date, convertedBack)
            assertEquals(ldt.year, convertedBack.year)
            assertEquals(ldt.monthValue, convertedBack.monthValue)
            assertEquals(ldt.dayOfMonth, convertedBack.dayOfMonth)
        }
    }

    @Nested
    @DisplayName("区间与边界相关")
    inner class RangeBoundary {
        @Test
        @DisplayName("LocalDateTime.isBetween() & LocalDate.isBetween()")
        fun testIsBetween() {
            logger.info("测试LocalDateTime.isBetween()方法")

            val start = LocalDateTime.of(2024, 6, 9, 10, 0, 0)
            val middle = LocalDateTime.of(2024, 6, 9, 14, 30, 0)
            val end = LocalDateTime.of(2024, 6, 9, 18, 0, 0)

            val isBetween = middle.isBetween(start, end)
            logger.info("时间是否在范围内: {} 在 {} 和 {} 之间: {}", middle, start, end, isBetween)
            assertTrue(isBetween)

            // 测试边界情况
            val beforeStart = LocalDateTime.of(2024, 6, 9, 9, 0, 0)
            val afterEnd = LocalDateTime.of(2024, 6, 9, 19, 0, 0)

            val isBeforeStart = beforeStart.isBetween(start, end)
            logger.info("时间是否在范围内: {} 在 {} 和 {} 之间: {}", beforeStart, start, end, isBeforeStart)
            assertFalse(isBeforeStart)

            val isAfterEnd = afterEnd.isBetween(start, end)
            logger.info("时间是否在范围内: {} 在 {} 和 {} 之间: {}", afterEnd, start, end, isAfterEnd)
            assertFalse(isAfterEnd)
        }
        @Test
        @DisplayName("LocalDateTime.isBetween()异常 & LocalDate.isBetween()异常")
        fun testIsBetweenException() {
            logger.info("测试LocalDateTime.isBetween()异常情况")

            val middle = LocalDateTime.now()

            // 测试null参数
            assertThrows(Exception::class.java) {
                middle.isBetween(null, LocalDateTime.now())
            }
            logger.info("start为null时正确抛出异常")

            assertThrows(Exception::class.java) {
                middle.isBetween(LocalDateTime.now(), null)
            }
            logger.info("end为null时正确抛出异常")
        }
        @Test
        @DisplayName("LocalDate.atEndOfDay()")
        fun testAtEndOfDay() {
            logger.info("测试LocalDate.atEndOfDay()方法")

            val ld = LocalDate.of(2024, 6, 9)
            val endOfDay = ld.atEndOfDay()

            logger.info("LocalDate获取当天结束时间: {} -> {}", ld, endOfDay)

            assertEquals(ld, endOfDay.toLocalDate())
            assertEquals(LocalTime.MAX, endOfDay.toLocalTime())
            assertEquals(23, endOfDay.hour)
            assertEquals(59, endOfDay.minute)
            assertEquals(59, endOfDay.second)
            assertEquals(999999999, endOfDay.nano)
        }
    }

    @Nested
    @DisplayName("时间段重叠相关")
    inner class Overlap {
        @Test
        @DisplayName("Pair<LocalDateTime, LocalDateTime>.overlap()")
        fun testPairOverlap() {
            logger.info("测试Pair.overlap()方法")

            val p1 = Pair(
                LocalDateTime.of(2024, 6, 9, 10, 0, 0),
                LocalDateTime.of(2024, 6, 9, 14, 0, 0)
            )
            val p2 = Pair(
                LocalDateTime.of(2024, 6, 9, 12, 0, 0),
                LocalDateTime.of(2024, 6, 9, 16, 0, 0)
            )
            val p3 = Pair(
                LocalDateTime.of(2024, 6, 9, 15, 0, 0),
                LocalDateTime.of(2024, 6, 9, 18, 0, 0)
            )

            val overlap1 = p1 overlap p2
            val overlap2 = p1 overlap p3

            logger.info("时间段1: {} - {}", p1.first, p1.second)
            logger.info("时间段2: {} - {}", p2.first, p2.second)
            logger.info("时间段3: {} - {}", p3.first, p3.second)
            logger.info("时间段1与时间段2是否重叠: {}", overlap1)
            logger.info("时间段1与时间段3是否重叠: {}", overlap2)

            assertTrue(overlap1) // 有重叠
            assertFalse(overlap2) // 无重叠
        }
        @Test
        @DisplayName("Pair<Date, Date>.dateOverlap()")
        fun testPairDateOverlap() {
            logger.info("测试Pair.dateOverlap()方法")

            val d1 = LocalDate.of(2024, 6, 9).toDate()
            val d2 = LocalDate.of(2024, 6, 15).toDate()
            val d3 = LocalDate.of(2024, 6, 20).toDate()
            val d4 = LocalDate.of(2024, 6, 25).toDate()

            val p1 = Pair(d1, d2)
            val p2 = Pair(d3, d4)
            val p3 = Pair(d2, d3) // 边界重合

            val overlap1 = p1 dateOverlap p2
            val overlap2 = p1 dateOverlap p3

            logger.info("日期段1: {} - {}", p1.first, p1.second)
            logger.info("日期段2: {} - {}", p2.first, p2.second)
            logger.info("日期段3: {} - {}", p3.first, p3.second)
            logger.info("日期段1与日期段2是否重叠: {}", overlap1)
            logger.info("日期段1与日期段3是否重叠: {} (边界重合)", overlap2)

            assertFalse(overlap1) // 无重叠
            assertFalse(overlap2) // 边界重合不算重叠
        }
    }

    @Nested
    @DisplayName("今日剩余秒数相关")
    inner class TodayRest {
        @Test
        @DisplayName("secondOfTodayRest()")
        fun testSecondOfTodayRest() {
            logger.info("测试secondOfTodayRest()方法")

            val restSeconds = secondOfTodayRest()

            logger.info("今天剩余秒数: {}", restSeconds)
            assertTrue(restSeconds > 0)
            assertTrue(restSeconds <= 86400) // 一天最多86400秒

            // 验证计算逻辑
            val now = LocalDateTime.now()
            val endOfDay = now.with(LocalTime.MAX)
            val expectedSeconds = java.time.Duration.between(now, endOfDay).seconds

            logger.info("当前时间: {}", now)
            logger.info("今天结束时间: {}", endOfDay)
            logger.info("预期剩余秒数: {}", expectedSeconds)
            logger.info("实际剩余秒数: {}", restSeconds)

            // 由于测试执行时间，允许1秒的误差
            assertTrue(kotlin.math.abs(restSeconds - expectedSeconds) <= 1)
        }
    }

    @Nested
    @DisplayName("异常与边界情况")
    inner class ExceptionBoundary {
        @Test
        @DisplayName("空字符串与无效格式")
        fun testInvalidString() {
            logger.info("测试边界情况")

            // 测试空字符串
            val emptyStr = ""
            assertThrows(Exception::class.java) {
                emptyStr.toLocalDate("yyyy-MM-dd")
            }
            logger.info("空字符串转换正确抛出异常")

            // 测试无效格式
            val invalidStr = "invalid-date"
            assertThrows(Exception::class.java) {
                invalidStr.toLocalDate("yyyy-MM-dd")
            }
            logger.info("无效格式字符串转换正确抛出异常")

            // 测试时间重叠的边界情况
            val start = LocalDateTime.of(2024, 6, 9, 10, 0, 0)
            val end = LocalDateTime.of(2024, 6, 9, 14, 0, 0)
            val boundary = LocalDateTime.of(2024, 6, 9, 14, 0, 0)

            val isBetween = boundary.isBetween(start, end)
            logger.info("边界时间是否在范围内: {} 在 {} 和 {} 之间: {}", boundary, start, end, isBetween)
            assertFalse(isBetween) // 边界时间不算在范围内
        }
        @Test
        @DisplayName("isBetween边界不包含")
        fun testIsBetweenBoundary() {
            val start = LocalDateTime.of(2024, 6, 9, 10, 0, 0)
            val end = LocalDateTime.of(2024, 6, 9, 14, 0, 0)
            val boundary = LocalDateTime.of(2024, 6, 9, 14, 0, 0)
            assertFalse(boundary.isBetween(start, end))
        }
    }

    // ==================== 性能测试 ====================

    @Test
    @DisplayName("性能测试")
    fun testPerformance() {
        logger.info("测试日期工具类性能")

        val iterations = 1000
        val testDateTime = LocalDateTime.of(2024, 6, 9, 14, 30, 45)

        // 测试格式化性能
        val formatStartTime = System.currentTimeMillis()
        repeat(iterations) {
            testDateTime.toString("yyyy-MM-dd HH:mm:ss")
        }
        val formatEndTime = System.currentTimeMillis()
        val formatDuration = formatEndTime - formatStartTime

        logger.info("格式化性能测试: {}次迭代耗时 {}ms", iterations, formatDuration)
        assertTrue(formatDuration < 1000) // 应该在1秒内完成

        // 测试转换性能
        val convertStartTime = System.currentTimeMillis()
        repeat(iterations) {
            testDateTime.toDate()
        }
        val convertEndTime = System.currentTimeMillis()
        val convertDuration = convertEndTime - convertStartTime

        logger.info("转换性能测试: {}次迭代耗时 {}ms", iterations, convertDuration)
        assertTrue(convertDuration < 1000) // 应该在1秒内完成
    }
}
