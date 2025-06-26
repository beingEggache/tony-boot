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

package tony.test.utils

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import tony.utils.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Date

/**
 * 日期工具类单元测试
 * @author AI
 * @date 2024/06/09
 * @since 1.0.0
 */
object DatesTest {

    private val logger = LoggerFactory.getLogger(DatesTest::class.java)

    // ==================== TemporalAccessor.toString() 方法测试 ====================

    @Test
    @DisplayName("TemporalAccessor.toString(pattern)方法测试")
    fun testTemporalAccessorToString() {
        logger.info("测试TemporalAccessor.toString(pattern)方法")
        
        val localDate = LocalDate.of(2024, 6, 9)
        val localDateTime = LocalDateTime.of(2024, 6, 9, 14, 30, 45)
        
        val dateStr = localDate.toString("yyyy-MM-dd")
        val dateTimeStr = localDateTime.toString("yyyy-MM-dd HH:mm:ss")
        
        logger.info("LocalDate格式化: {} -> '{}'", localDate, dateStr)
        logger.info("LocalDateTime格式化: {} -> '{}'", localDateTime, dateTimeStr)
        
        assertEquals("2024-06-09", dateStr)
        assertEquals("2024-06-09 14:30:45", dateTimeStr)
    }

    // ==================== CharSequence.toDate() 方法测试 ====================

    @Test
    @DisplayName("CharSequence.toDate(pattern)方法测试")
    fun testCharSequenceToDate() {
        logger.info("测试CharSequence.toDate(pattern)方法")
        
        val dateStr = "2024-06-09 14:30:45"
        val date = dateStr.toDate("yyyy-MM-dd HH:mm:ss")
        
        logger.info("字符串转Date: '{}' -> {}", dateStr, date)
        assertNotNull(date)
        
        // 验证转换结果
        val localDateTime = date.toLocalDateTime()
        assertEquals(2024, localDateTime.year)
        assertEquals(6, localDateTime.monthValue)
        assertEquals(9, localDateTime.dayOfMonth)
        assertEquals(14, localDateTime.hour)
        assertEquals(30, localDateTime.minute)
        assertEquals(45, localDateTime.second)
    }

    // ==================== CharSequence.toLocalDate() 方法测试 ====================

    @Test
    @DisplayName("CharSequence.toLocalDate(pattern)方法测试")
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

    // ==================== CharSequence.toLocalDateTime() 方法测试 ====================

    @Test
    @DisplayName("CharSequence.toLocalDateTime(pattern)方法测试")
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

    // ==================== LocalDateTime.toInstant() 方法测试 ====================

    @Test
    @DisplayName("LocalDateTime.toInstant()方法测试")
    fun testLocalDateTimeToInstant() {
        logger.info("测试LocalDateTime.toInstant()方法")
        
        val localDateTime = LocalDateTime.of(2024, 6, 9, 14, 30, 45)
        val instant = localDateTime.toInstant()
        
        logger.info("LocalDateTime转Instant: {} -> {}", localDateTime, instant)
        assertNotNull(instant)
        
        // 验证转换结果
        val convertedBack = LocalDateTime.ofInstant(instant, java.time.ZoneId.systemDefault())
        assertEquals(localDateTime, convertedBack)
    }

    // ==================== LocalDateTime.toDate() 方法测试 ====================

    @Test
    @DisplayName("LocalDateTime.toDate()方法测试")
    fun testLocalDateTimeToDate() {
        logger.info("测试LocalDateTime.toDate()方法")
        
        val localDateTime = LocalDateTime.of(2024, 6, 9, 14, 30, 45)
        val date = localDateTime.toDate()
        
        logger.info("LocalDateTime转Date: {} -> {}", localDateTime, date)
        assertNotNull(date)
        
        // 验证转换结果
        val convertedBack = date.toLocalDateTime()
        assertEquals(localDateTime.year, convertedBack.year)
        assertEquals(localDateTime.monthValue, convertedBack.monthValue)
        assertEquals(localDateTime.dayOfMonth, convertedBack.dayOfMonth)
        assertEquals(localDateTime.hour, convertedBack.hour)
        assertEquals(localDateTime.minute, convertedBack.minute)
        assertEquals(localDateTime.second, convertedBack.second)
    }

    // ==================== LocalDateTime.isBetween() 方法测试 ====================

    @Test
    @DisplayName("LocalDateTime.isBetween()方法测试")
    fun testLocalDateTimeIsBetween() {
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
    @DisplayName("LocalDateTime.isBetween()异常测试")
    fun testLocalDateTimeIsBetweenException() {
        logger.info("测试LocalDateTime.isBetween()异常情况")
        
        val middle = LocalDateTime.of(2024, 6, 9, 14, 30, 0)
        
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

    // ==================== LocalDate.toInstant() 方法测试 ====================

    @Test
    @DisplayName("LocalDate.toInstant()方法测试")
    fun testLocalDateToInstant() {
        logger.info("测试LocalDate.toInstant()方法")

        val localDate = LocalDate.of(2024, 6, 9)
        val instant = localDate.toInstant()
        
        logger.info("LocalDate转Instant: {} -> {}", localDate, instant)
        assertNotNull(instant)
        
        // 验证转换结果
        val convertedBack = LocalDate.ofInstant(instant, java.time.ZoneId.systemDefault())
        assertEquals(localDate, convertedBack)
    }

    // ==================== LocalDate.toDate() 方法测试 ====================

    @Test
    @DisplayName("LocalDate.toDate()方法测试")
    fun testLocalDateToDate() {
        logger.info("测试LocalDate.toDate()方法")

        val localDate = LocalDate.of(2024, 6, 9)
        val date = localDate.toDate()
        
        logger.info("LocalDate转Date: {} -> {}", localDate, date)
        assertNotNull(date)
        
        // 验证转换结果
        val convertedBack = date.toLocalDate()
        assertEquals(localDate, convertedBack)
    }

    // ==================== LocalDate.isBetween() 方法测试 ====================

    @Test
    @DisplayName("LocalDate.isBetween()方法测试")
    fun testLocalDateIsBetween() {
        logger.info("测试LocalDate.isBetween()方法")

        val start = LocalDate.of(2024, 6, 1)
        val middle = LocalDate.of(2024, 6, 15)
        val end = LocalDate.of(2024, 6, 30)
        
        val isBetween = middle.isBetween(start, end)
        logger.info("日期是否在范围内: {} 在 {} 和 {} 之间: {}", middle, start, end, isBetween)
        assertTrue(isBetween)
        
        // 测试边界情况
        val beforeStart = LocalDate.of(2024, 5, 31)
        val afterEnd = LocalDate.of(2024, 7, 1)
        
        val isBeforeStart = beforeStart.isBetween(start, end)
        logger.info("日期是否在范围内: {} 在 {} 和 {} 之间: {}", beforeStart, start, end, isBeforeStart)
        assertFalse(isBeforeStart)
        
        val isAfterEnd = afterEnd.isBetween(start, end)
        logger.info("日期是否在范围内: {} 在 {} 和 {} 之间: {}", afterEnd, start, end, isAfterEnd)
        assertFalse(isAfterEnd)
    }

    @Test
    @DisplayName("LocalDate.isBetween()异常测试")
    fun testLocalDateIsBetweenException() {
        logger.info("测试LocalDate.isBetween()异常情况")
        
        val middle = LocalDate.of(2024, 6, 15)
        
        // 测试null参数
        assertThrows(Exception::class.java) {
            middle.isBetween(null, LocalDate.now())
        }
        logger.info("start为null时正确抛出异常")
        
        assertThrows(Exception::class.java) {
            middle.isBetween(LocalDate.now(), null)
        }
        logger.info("end为null时正确抛出异常")
    }

    // ==================== LocalDate.atEndOfDay() 方法测试 ====================

    @Test
    @DisplayName("LocalDate.atEndOfDay()方法测试")
    fun testLocalDateAtEndOfDay() {
        logger.info("测试LocalDate.atEndOfDay()方法")
        
        val localDate = LocalDate.of(2024, 6, 9)
        val endOfDay = localDate.atEndOfDay()
        
        logger.info("LocalDate获取当天结束时间: {} -> {}", localDate, endOfDay)
        
        assertEquals(localDate, endOfDay.toLocalDate())
        assertEquals(LocalTime.MAX, endOfDay.toLocalTime())
        assertEquals(23, endOfDay.hour)
        assertEquals(59, endOfDay.minute)
        assertEquals(59, endOfDay.second)
        assertEquals(999999999, endOfDay.nano)
    }

    // ==================== Date.toString() 方法测试 ====================

    @Test
    @DisplayName("Date.toString(pattern)方法测试")
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

    // ==================== Date.toLocalDate() 方法测试 ====================

    @Test
    @DisplayName("Date.toLocalDate()方法测试")
    fun testDateToLocalDate() {
        logger.info("测试Date.toLocalDate()方法")
        
        val localDate = LocalDate.of(2024, 6, 9)
        val date = localDate.toDate()
        val convertedBack = date.toLocalDate()
        
        logger.info("Date转LocalDate: {} -> {}", date, convertedBack)
        assertEquals(localDate, convertedBack)
    }

    // ==================== Date.toLocalDateTime() 方法测试 ====================

    @Test
    @DisplayName("Date.toLocalDateTime()方法测试")
    fun testDateToLocalDateTime() {
        logger.info("测试Date.toLocalDateTime()方法")

        val localDateTime = LocalDateTime.of(2024, 6, 9, 14, 30, 45)
        val date = localDateTime.toDate()
        val convertedBack = date.toLocalDateTime()
        
        logger.info("Date转LocalDateTime: {} -> {}", date, convertedBack)
        assertEquals(localDateTime.year, convertedBack.year)
        assertEquals(localDateTime.monthValue, convertedBack.monthValue)
        assertEquals(localDateTime.dayOfMonth, convertedBack.dayOfMonth)
        assertEquals(localDateTime.hour, convertedBack.hour)
        assertEquals(localDateTime.minute, convertedBack.minute)
        assertEquals(localDateTime.second, convertedBack.second)
    }

    // ==================== secondOfTodayRest() 方法测试 ====================

    @Test
    @DisplayName("secondOfTodayRest()方法测试")
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

    // ==================== Pair.overlap() 方法测试 ====================

    @Test
    @DisplayName("Pair.overlap()方法测试")
    fun testPairOverlap() {
        logger.info("测试Pair.overlap()方法")

        val period1 = Pair(
            LocalDateTime.of(2024, 6, 9, 10, 0, 0),
            LocalDateTime.of(2024, 6, 9, 14, 0, 0)
        )
        val period2 = Pair(
            LocalDateTime.of(2024, 6, 9, 12, 0, 0),
            LocalDateTime.of(2024, 6, 9, 16, 0, 0)
        )
        val period3 = Pair(
            LocalDateTime.of(2024, 6, 9, 15, 0, 0),
            LocalDateTime.of(2024, 6, 9, 18, 0, 0)
        )
        
        val overlap1 = period1 overlap period2
        val overlap2 = period1 overlap period3
        
        logger.info("时间段1: {} - {}", period1.first, period1.second)
        logger.info("时间段2: {} - {}", period2.first, period2.second)
        logger.info("时间段3: {} - {}", period3.first, period3.second)
        logger.info("时间段1与时间段2是否重叠: {}", overlap1)
        logger.info("时间段1与时间段3是否重叠: {}", overlap2)
        
        assertTrue(overlap1) // 有重叠
        assertFalse(overlap2) // 无重叠
    }

    // ==================== Pair.dateOverlap() 方法测试 ====================

    @Test
    @DisplayName("Pair.dateOverlap()方法测试")
    fun testPairDateOverlap() {
        logger.info("测试Pair.dateOverlap()方法")
        
        val date1 = LocalDate.of(2024, 6, 9).toDate()
        val date2 = LocalDate.of(2024, 6, 15).toDate()
        val date3 = LocalDate.of(2024, 6, 20).toDate()
        val date4 = LocalDate.of(2024, 6, 25).toDate()
        
        val period1 = Pair(date1, date2)
        val period2 = Pair(date3, date4)
        val period3 = Pair(date2, date3) // 边界重合
        
        val overlap1 = period1 dateOverlap period2
        val overlap2 = period1 dateOverlap period3
        
        logger.info("日期段1: {} - {}", period1.first, period1.second)
        logger.info("日期段2: {} - {}", period2.first, period2.second)
        logger.info("日期段3: {} - {}", period3.first, period3.second)
        logger.info("日期段1与日期段2是否重叠: {}", overlap1)
        logger.info("日期段1与日期段3是否重叠: {} (边界重合)", overlap2)
        
        assertFalse(overlap1) // 无重叠
        assertFalse(overlap2) // 边界重合不算重叠
    }

    // ==================== 边界情况测试 ====================

    @Test
    @DisplayName("边界情况测试")
    fun testBoundaryCases() {
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

    // ==================== 性能测试 ====================

    @Test
    @DisplayName("性能测试")
    fun testPerformance() {
        logger.info("测试日期工具类性能")

        val iterations = 1000
        val testDate = LocalDate.of(2024, 6, 9)
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
