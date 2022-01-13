@file:Suppress("unused", "FunctionName")
@file:JvmName("NumberUtils")
/**
 *
 * @author tangli
 * @since 2020-12-14 13:49
 */
package com.tony.utils

import com.tony.exception.ApiException
import java.math.BigDecimal
import java.math.RoundingMode
import java.security.SecureRandom
import java.text.NumberFormat
import kotlin.math.pow

/**
 * 数字类型转为 [BigDecimal]
 * @param decimal 小数点后保留几位
 */
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

/**
 * 数字截断并转为 [BigDecimal]
 * @param digit 截断位数
 * @param decimal 保留几位小数
 */
@JvmOverloads
fun Number?.truncToBigDecimal(digit: Int = 2, decimal: Int = digit) =
    toBigDecimal(decimal).div(10.toBigDecimal(decimal).pow(digit))

/**
 * 数字截断并转为 [String]
 * @param digit 截断位数
 * @param decimal 保留几位小数
 */
@JvmOverloads
fun Number?.truncToString(digit: Int = 2, decimal: Int = digit) =
    truncToBigDecimal(digit, decimal).toString()

/**
 * 百分比表示
 * @param decimal 保留几位小数
 * @param roundingMode see [RoundingMode]
 */
@JvmOverloads
fun Float?.formatToPercent(decimal: Int = 2, roundingMode: RoundingMode = RoundingMode.DOWN): String =
    formatToPercent(this, decimal, roundingMode)

/**
 * 百分比表示
 * @param decimal 保留几位小数
 * @param roundingMode see [RoundingMode]
 */
@JvmOverloads
fun Double?.formatToPercent(decimal: Int = 2, roundingMode: RoundingMode = RoundingMode.DOWN): String =
    formatToPercent(this, decimal, roundingMode)

/**
 * 百分比表示
 * @param decimal 保留几位小数
 * @param roundingMode see [RoundingMode]
 */
@JvmOverloads
fun BigDecimal?.formatToPercent(decimal: Int = 2, roundingMode: RoundingMode = RoundingMode.DOWN): String =
    formatToPercent(this, decimal, roundingMode)

val secureRandom = SecureRandom()

/**
 * 生成指定位数随机数
 * @param digit 位数
 */
fun genRandomNumber(digit: Int): Int {
    if (digit < 1) throw ApiException("随机数至少是一位数")
    if (digit == 1) {
        return secureRandom.nextInt(10)
    }
    val base = 9 * ((10).toDouble().pow((digit - 1).toDouble()).toInt())
    val fix = 10.toDouble().pow((digit - 1).toDouble()).toInt()
    return secureRandom.nextInt(base) + fix
}
