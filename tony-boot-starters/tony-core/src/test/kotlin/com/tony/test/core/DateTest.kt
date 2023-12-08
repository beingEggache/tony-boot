package com.tony.test.core

import com.tony.utils.atEndOfDay
import com.tony.utils.getLogger
import com.tony.utils.overlap
import com.tony.utils.toDate
import com.tony.utils.toLocalDate
import com.tony.utils.toLocalDateTime
import com.tony.utils.toString
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date

/**
 * DateTest is
 * @author tangli
 * @date 2023/12/08 18:50
 * @since 1.0.0
 */
object DateTest {

    private val logger = getLogger()

    @Test
    fun testDateTransform() {
        val pattern_yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss"
        val pattern_yyyyMMdd = "yyyy-MM-dd"

        val localDate = LocalDate.of(1989, 3, 15)
        val varLength = 35
        logger.info("${"localDate".padEnd(varLength)}:{}", localDate)
        val localDateString = localDate.toString(pattern_yyyyMMdd)
        logger.info("${"localDateString".padEnd(varLength)}:{}", localDateString)

        val localDateAtStartOfDay = localDate.atStartOfDay()
        logger.info("${"localDateAtStartOfDay".padEnd(varLength)}:{}", localDateAtStartOfDay)
        val localDateAtStartOfDayString = localDateAtStartOfDay.toString(pattern_yyyyMMddHHmmss)
        logger.info("${"localDateAtStartOfDayString".padEnd(varLength)}:{}", localDateAtStartOfDayString)

        val localDateAtEndOfDay = localDate.atEndOfDay()
        logger.info("${"localDateAtEndOfDay".padEnd(varLength)}:{}", localDateAtEndOfDay)
        val localDateAtEndOfDayString = localDateAtEndOfDay.toString(pattern_yyyyMMddHHmmss)
        logger.info("${"localDateAtEndOfDayString".padEnd(varLength)}:{}", localDateAtEndOfDayString)

        val localDateToDate = localDate.toDate()
        logger.info("${"localDateToDate".padEnd(varLength)}:{}", localDateToDate)
        val localDateToDateString = localDateToDate.toString(pattern_yyyyMMddHHmmss)
        logger.info("${"localDateToDateString".padEnd(varLength)}:{}", localDateToDateString)

        val localDateTime = LocalDateTime.of(1989, 3, 15, 12, 12, 12)
        logger.info("${"localDateTime".padEnd(varLength)}:{}", localDateTime)
        val localDateTimeString = localDateTime.toString(pattern_yyyyMMddHHmmss)
        logger.info("${"localDateTimeString".padEnd(varLength)}:{}", localDateTimeString)

        val localDateTimeToLocalDate = localDateTime.toLocalDate()
        logger.info("${"localDateTimeToLocalDate".padEnd(varLength)}:{}", localDateTimeToLocalDate)
        val localDateTimeToLocalDateString = localDateTimeToLocalDate.toString(pattern_yyyyMMdd)
        logger.info("${"localDateTimeToLocalDateString".padEnd(varLength)}:{}", localDateTimeToLocalDateString)

        val localDateTimeToDate = localDateTime.toDate()
        logger.info("${"localDateTimeToDate".padEnd(varLength)}:{}", localDateTimeToDate)
        val localDateTimeToDateString = localDateTimeToDate.toString(pattern_yyyyMMddHHmmss)
        logger.info("${"localDateTimeToDateString".padEnd(varLength)}:{}", localDateTimeToDateString)

        val date = Date(89, 2, 15, 12, 12, 12)
        logger.info("${"date".padEnd(varLength)}:{}", date)
        val dateString = date.toString(pattern_yyyyMMddHHmmss)
        logger.info("${"dateString".padEnd(varLength)}:{}", dateString)

        val dateToLocalDate = date.toLocalDate()
        logger.info("${"dateToLocalDate".padEnd(varLength)}:{}", dateToLocalDate)
        val dateToLocalDateString = dateToLocalDate.toString(pattern_yyyyMMdd)
        logger.info("${"dateToLocalDateString".padEnd(varLength)}:{}", dateToLocalDateString)

        val dateToLocalDateTime = date.toLocalDateTime()
        logger.info("${"dateToLocalDateTime".padEnd(varLength)}:{}", dateToLocalDateTime)
        val dateToLocalDateTimeString = dateToLocalDateTime.toString(pattern_yyyyMMddHHmmss)
        logger.info("${"dateToLocalDateTimeString".padEnd(varLength)}:{}", dateToLocalDateTimeString)

        val stringDate = "1989-03-15 12:12:12"
        val stringDateToDate = stringDate.toDate(pattern_yyyyMMddHHmmss)
        logger.info("${"stringDateToDate".padEnd(varLength)}:{}", stringDateToDate)

        val stringLocalDate = "1989-03-15"
        val stringDateToLocalDate = stringLocalDate.toLocalDate(pattern_yyyyMMdd)
        logger.info("${"stringDateToLocalDate".padEnd(varLength)}:{}", stringDateToLocalDate)

        val stringLocalDateTime = "1989-03-15 12:12:12"
        val stringLocalDateTimeToLocalDatetime = stringLocalDateTime.toDate(pattern_yyyyMMddHHmmss)
        logger.info("${"stringLocalDateTimeToLocalDatetime".padEnd(varLength)}:{}", stringLocalDateTimeToLocalDatetime)
    }

    @Test
    fun testDateOverlap() {
        val timePeriod1 = LocalDate.of(2023, 5, 1) to LocalDate.of(2023, 10, 1)
        val timePeriod2 = LocalDate.of(2023, 4, 1) to LocalDate.of(2023, 11, 1)
        val overlap1 = timePeriod1
            .overlap(
                timePeriod2
            )
        logger.info("$timePeriod1 is overlap $timePeriod2 ? {}", overlap1)

        val timePeriod3 = LocalDate.of(2023, 5, 1) to LocalDate.of(2023, 10, 1)
        val timePeriod4 = LocalDate.of(2023, 6, 1) to LocalDate.of(2023, 9, 1)
        val overlap2 = timePeriod3
            .overlap(
                timePeriod4
            )
        logger.info("$timePeriod3 is overlap $timePeriod4 ? {}", overlap2)

        val timePeriod5 = LocalDate.of(2023, 5, 1) to LocalDate.of(2023, 10, 1)
        val timePeriod6 = LocalDate.of(2023, 11, 1) to LocalDate.of(2023, 12, 1)
        val overlap3 = timePeriod5
            .overlap(
                timePeriod6
            )
        logger.info("$timePeriod5 is overlap $timePeriod6 ? {}", overlap3)
    }

}
