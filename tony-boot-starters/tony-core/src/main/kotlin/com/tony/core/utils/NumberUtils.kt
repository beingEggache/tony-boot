@file:Suppress("unused", "FunctionName")
@file:JvmName("NumberUtils")
/**
 *
 * @author tangli
 * @since 2020-12-14 13:49
 */
package com.tony.core.utils

import com.tony.core.exception.ApiException
import java.math.BigDecimal
import java.math.RoundingMode
import java.security.SecureRandom
import java.text.NumberFormat
import kotlin.math.pow

@JvmOverloads
fun Number?.toBigDecimal(decimal: Int = 2): BigDecimal {
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

private fun formatToPercent(number: Number?, digit: Int, roundingMode: RoundingMode = RoundingMode.DOWN): String {
    return NumberFormat.getPercentInstance().apply {
        maximumFractionDigits = digit
        this.roundingMode = roundingMode
    }.format(number ?: 0)
}

@JvmOverloads
fun Number?.truncToBigDecimal(digit: Int = 2, decimal: Int = digit) =
    toBigDecimal(decimal).div(10.toBigDecimal(decimal).pow(digit))

@JvmOverloads
fun Number?.truncToString(digit: Int = 2, decimal: Int = digit) =
    truncToBigDecimal(digit, decimal).toString()

@JvmOverloads
fun Float?.formatToPercent(digit: Int = 2, roundingMode: RoundingMode = RoundingMode.DOWN): String =
    formatToPercent(this, digit, roundingMode)

@JvmOverloads
fun Double?.formatToPercent(digit: Int = 2, roundingMode: RoundingMode = RoundingMode.DOWN): String =
    formatToPercent(this, digit, roundingMode)

@JvmOverloads
fun BigDecimal?.formatToPercent(digit: Int = 2, roundingMode: RoundingMode = RoundingMode.DOWN): String =
    formatToPercent(this, digit, roundingMode)

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
