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

package tony.test.core

import tony.utils.atEndOfDay
import tony.utils.getLogger
import tony.utils.overlap
import tony.utils.toDate
import tony.utils.toLocalDate
import tony.utils.toLocalDateTime
import tony.utils.toString
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date

/**
 * DateTest is
 * @author tangli
 * @date 2023/12/08 19:50
 * @since 1.0.0
 */
object DatesTest {

    private val logger = getLogger()
    val pattern_yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss"
    val pattern_yyyyMMdd = "yyyy-MM-dd"
    val pattern_yyyyMM = "yyyy-MM"
    val pattern_HHmmss = "HH:mm:ss"
    val padLength = 35

    @Test
    fun testDate(){
        val date = Date(89, 2, 15, 12, 12, 12)
        logger.info("${"date".padEnd(padLength)}:{}", date)

        val dateToLocalDate = date.toLocalDate()
        logger.info("${"dateToLocalDate".padEnd(padLength)}:{}", dateToLocalDate)

        val dateToLocalDateTime = date.toLocalDateTime()
        logger.info("${"dateToLocalDateTime".padEnd(padLength)}:{}", dateToLocalDateTime)
    }

    @Test
    fun testLocalDate(){
        val localDate = LocalDate.of(1989, 3, 15)
        logger.info("${"localDate".padEnd(padLength)}:{}", localDate)

        val localDateAtStartOfDay = localDate.atStartOfDay()
        logger.info("${"localDateAtStartOfDay".padEnd(padLength)}:{}", localDateAtStartOfDay)

        val localDateAtEndOfDay = localDate.atEndOfDay()
        logger.info("${"localDateAtEndOfDay".padEnd(padLength)}:{}", localDateAtEndOfDay)

        val localDateToDate = localDate.toDate()
        logger.info("${"localDateToDate".padEnd(padLength)}:{}", localDateToDate)
    }

    @Test
    fun testLocalDateTime(){
        val localDateTime = LocalDateTime.of(1989, 3, 15, 12, 12, 12)
        logger.info("${"localDateTime".padEnd(padLength)}:{}", localDateTime)

        val localDateTimeToLocalDate = localDateTime.toLocalDate()
        logger.info("${"localDateTimeToLocalDate".padEnd(padLength)}:{}", localDateTimeToLocalDate)

        val localDateTimeToDate = localDateTime.toDate()
        logger.info("${"localDateTimeToDate".padEnd(padLength)}:{}", localDateTimeToDate)
    }

    @Test
    fun testFormatTo(){
        val stringDate = "1989-03-15 12:12:12"
        val stringDateToDate = stringDate.toDate(pattern_yyyyMMddHHmmss)
        logger.info("${"stringDateToDate".padEnd(padLength)}:{}", stringDateToDate)

        val stringLocalDate = "1989-03-15"
        val stringDateToLocalDate = stringLocalDate.toLocalDate(pattern_yyyyMMdd)
        logger.info("${"stringDateToLocalDate".padEnd(padLength)}:{}", stringDateToLocalDate)

        val stringLocalDateTime = "1989-03-15 12:12:12"
        val stringLocalDateTimeToLocalDatetime = stringLocalDateTime.toDate(pattern_yyyyMMddHHmmss)
        logger.info("${"stringLocalDateTimeToLocalDatetime".padEnd(padLength)}:{}", stringLocalDateTimeToLocalDatetime)
    }
    @Test
    fun testToString(){
        val date = Date(89, 2, 15, 12, 12, 12)
        val localDate = LocalDate.of(1989, 3, 15)
        val localDateTime = LocalDateTime.of(1989, 3, 15, 12, 12, 12)

        val date_yyyyMMddHHmmssStr = date.toString(pattern_yyyyMMddHHmmss)
        logger.info("${"date_yyyyMMddHHmmssStr".padEnd(padLength)}:{}", date_yyyyMMddHHmmssStr)
        val date_yyyyMMddStr = date.toString(pattern_yyyyMMdd)
        logger.info("${"date_yyyyMMddStr".padEnd(padLength)}:{}", date_yyyyMMddStr)
        val date_yyyyMMStr = date.toString(pattern_yyyyMM)
        logger.info("${"date_yyyyMMStr".padEnd(padLength)}:{}", date_yyyyMMStr)
        val date_HHmmssStr = date.toString(pattern_HHmmss)
        logger.info("${"date_HHmmssStr".padEnd(padLength)}:{}", date_HHmmssStr)

        val localDate_yyyyMMddStr = localDate.toString(pattern_yyyyMMdd)
        logger.info("${"localDate_yyyyMMddStr".padEnd(padLength)}:{}", localDate_yyyyMMddStr)
        val localDate_yyyyMMStr = localDate.toString(pattern_yyyyMM)
        logger.info("${"localDate_yyyyMMStr".padEnd(padLength)}:{}", localDate_yyyyMMStr)

        val localDateTime_yyyyMMddHHmmssStr = localDateTime.toString(pattern_yyyyMMddHHmmss)
        logger.info("${"localDateTime_yyyyMMddHHmmssStr".padEnd(padLength)}:{}", localDateTime_yyyyMMddHHmmssStr)
        val localDateTime_yyyyMMddStr = localDateTime.toString(pattern_yyyyMMdd)
        logger.info("${"localDateTime_yyyyMMddStr".padEnd(padLength)}:{}", localDateTime_yyyyMMddStr)
        val localDateTime_yyyyMMStr = localDateTime.toString(pattern_yyyyMM)
        logger.info("${"localDateTime_yyyyMMStr".padEnd(padLength)}:{}", localDateTime_yyyyMMStr)
        val localDateTime_HHmmssStr = localDateTime.toString(pattern_HHmmss)
        logger.info("${"localDateTime_HHmmssStr".padEnd(padLength)}:{}", localDateTime_HHmmssStr)
    }

    @Test
    fun testDateOverlap() {
        val timePeriod1 = LocalDate.of(2023, 5, 1) to LocalDate.of(2023, 10, 1)
        val timePeriod2 = LocalDate.of(2023, 4, 1) to LocalDate.of(2023, 11, 1)
        val overlap1 = timePeriod1 overlap timePeriod2
        logger.info("$timePeriod1 is overlap $timePeriod2 ? {}", overlap1)

        val timePeriod3 = LocalDate.of(2023, 5, 1) to LocalDate.of(2023, 10, 1)
        val timePeriod4 = LocalDate.of(2023, 6, 1) to LocalDate.of(2023, 9, 1)
        val overlap2 = timePeriod3 overlap timePeriod4
        logger.info("$timePeriod3 is overlap $timePeriod4 ? {}", overlap2)

        val timePeriod5 = LocalDate.of(2023, 5, 1) to LocalDate.of(2023, 10, 1)
        val timePeriod6 = LocalDate.of(2023, 11, 1) to LocalDate.of(2023, 12, 1)
        val overlap3 = timePeriod5 overlap timePeriod6
        logger.info("$timePeriod5 is overlap $timePeriod6 ? {}", overlap3)
    }

}
