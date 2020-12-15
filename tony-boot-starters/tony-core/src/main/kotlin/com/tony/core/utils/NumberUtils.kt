@file:Suppress("unused", "FunctionName")
@file:JvmName("NumberUtils")

package com.tony.core.utils

import com.tony.core.exception.ApiException
import java.math.BigDecimal
import java.math.RoundingMode
import java.security.SecureRandom
import java.text.NumberFormat
import kotlin.math.pow

/**
 *
 * @author tangli
 * @since 2020-12-14 13:49
 */

@JvmOverloads
fun Number?.truncToBigDecimal(decimal: Int = 2): BigDecimal {
    if (decimal < 0) throw IllegalArgumentException("decimal must >= 0")
    return when (this) {
        null -> "0".toBigDecimal(decimal)
        is Int -> "$this".toBigDecimal(decimal)
        is Long -> "$this".toBigDecimal(decimal)
        is Double -> "$this".toBigDecimal(decimal)
        is BigDecimal -> "$this".toBigDecimal(decimal)
        else -> throw ApiException("Not support ${this::class.simpleName}")
    }
}

private fun String?.toBigDecimal(decimal: Int = 2) = BigDecimal(this ?: "0").setScale(decimal, RoundingMode.DOWN)

private fun formatToPercent(number: Number?, digit: Int): String {
    return NumberFormat.getPercentInstance().apply {
        maximumFractionDigits = digit
    }.format(number ?: 0)
}

@JvmOverloads
fun Number?.truncToString(digit: Int = 2, decimal: Int = digit) =
    truncToBigDecimal(decimal).div(10.truncToBigDecimal(decimal).pow(digit)).toString()

@JvmOverloads
fun Float?.formatToPercent(digit: Int = 2): String = formatToPercent(this, digit)

@JvmOverloads
fun Double?.formatToPercent(digit: Int = 2): String = formatToPercent(this, digit)

@JvmOverloads
fun BigDecimal?.formatToPercent(digit: Int = 2): String = formatToPercent(this, digit)

private val secureRandom = SecureRandom()

/**
 * 生成指定位数随机数
 * @param digit 位数
 */
fun genRandomNumber(digit: Int): Int {
    if (digit < 1) throw ApiException("随机数至少是一位数")
    if (digit == 1) {
        return secureRandom.nextInt(10)
    }
    val bound1 = (9 * 10).toDouble().pow((digit - 1).toDouble()).toInt()
    val bound2 = 10.toDouble().pow((digit - 1).toDouble()).toInt()

    return secureRandom.nextInt(bound1) + bound2
}
