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

@file:JvmName("Nums")

package com.tony.utils

/**
 * 数字工具类
 * @author Tang Li
 * @date 2020-12-14 13:49
 */
import com.tony.exception.ApiException
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode
import java.security.SecureRandom
import java.text.NumberFormat
import kotlin.math.pow

/**
 * 数字类型转为 [BigDecimal]
 * @receiver [Number]?
 * @param [decimal] 小数点后保留几位
 * @return [BigDecimal]
 * @author Tang Li
 * @date 2023/09/13 10:23
 * @since 1.0.0
 */
@JvmOverloads
public fun Number?.toBigDecimal(decimal: Int = 2): BigDecimal {
    require(decimal >= 0) { "decimal must >= 0" }
    return when (this) {
        null -> "0".toBigDecimal(decimal)
        is Int -> "$this".toBigDecimal(decimal)
        is Long -> "$this".toBigDecimal(decimal)
        is Double -> "$this".toBigDecimal(decimal)
        is BigDecimal -> "$this".toBigDecimal(decimal)
        else -> throw ApiException("Not support ${this::class.simpleName}")
    }
}

/**
 * 数值类型互转, 只支持常规数字类型.
 * 线程安全数值类型不支持.
 * @receiver [T]
 * @param [T] 自身数值类型
 * @param [R] 返回数值类型
 * @param [numberType] 目标类型
 * @return [R]
 * @author Tang Li
 * @date 2023/09/13 10:24
 * @since 1.0.0
 */
public fun <T : Number, R : Number> T.toNumber(numberType: Class<in R>): R =
    when (numberType) {
        Long::class.javaObjectType, Long::class.javaPrimitiveType -> this.toLong()
        Int::class.javaObjectType, Int::class.javaPrimitiveType -> this.toInt()
        Double::class.javaObjectType, Double::class.javaPrimitiveType -> this.toDouble()
        Byte::class.javaObjectType, Byte::class.javaPrimitiveType -> this.toByte()
        Short::class.javaObjectType, Short::class.javaPrimitiveType -> this.toShort()
        Float::class.javaObjectType, Float::class.javaPrimitiveType -> this.toFloat()
        BigInteger::class.java -> BigInteger.valueOf(this.toLong())
        BigDecimal::class.java -> BigDecimal(this.toString())
        else -> throw IllegalArgumentException("Not support input type: $numberType")
    }.asToNotNull()

/**
 * 将字符串类型或数值类型转换成数值.
 * @receiver [Any]? 字符串或数值
 * @param [E] 返回数值类型
 * @param [numberType] 数值类型
 * @return [E]
 * @author Tang Li
 * @date 2023/09/13 10:24
 * @since 1.0.0
 */
public fun <E : Number> Any?.toNumber(numberType: Class<in E>): E =
    when (this) {
        is Number -> this.toNumber(numberType)
        is CharSequence -> this.toNumber(numberType)
        else -> error("${this?.javaClass} can't transform to number.")
    }

/**
 * 数字截断并转为  [BigDecimal]
 * @receiver [Number]?
 * @param [digit] 截断位数
 * @param [decimal] 保留几位小数
 * @return [BigDecimal]
 * @author Tang Li
 * @date 2023/09/13 10:24
 * @since 1.0.0
 */
@JvmOverloads
public fun Number?.truncToBigDecimal(
    digit: Int = 2,
    decimal: Int = digit,
): BigDecimal =
    toBigDecimal(decimal).div(10.toBigDecimal(decimal).pow(digit))

/**
 * 数字截断并转为 [String]
 * @receiver [Number]?
 * @param [digit] 截断位数
 * @param [decimal] 保留几位小数
 * @return [String]
 * @author Tang Li
 * @date 2023/09/13 10:24
 * @since 1.0.0
 */
@JvmOverloads
public fun Number?.truncToString(
    digit: Int = 2,
    decimal: Int = digit,
): String =
    truncToBigDecimal(digit, decimal).toString()

/**
 * 格式化为百分比
 * @receiver [Float]?
 * @param [decimal] 保留几位小数
 * @param [roundingMode] see [RoundingMode]
 * @return [String]
 * @author Tang Li
 * @date 2023/09/13 10:24
 * @since 1.0.0
 */
@JvmOverloads
public fun Float?.formatToPercent(
    decimal: Int = 2,
    roundingMode: RoundingMode = RoundingMode.DOWN,
): String =
    formatToPercent(this, decimal, roundingMode)

/**
 * 格式化为百分比
 * @receiver [Double]?
 * @param [decimal] 保留几位小数
 * @param [roundingMode] see [RoundingMode]
 * @return [String]
 * @author Tang Li
 * @date 2023/09/13 10:25
 * @since 1.0.0
 */
@JvmOverloads
public fun Double?.formatToPercent(
    decimal: Int = 2,
    roundingMode: RoundingMode = RoundingMode.DOWN,
): String =
    formatToPercent(this, decimal, roundingMode)

/**
 * 格式化为百分比
 * @receiver [BigDecimal]?
 * @param [decimal] 保留几位小数
 * @param [roundingMode] see [RoundingMode]
 * @return [String]
 * @author Tang Li
 * @date 2023/09/13 10:25
 * @since 1.0.0
 */
@JvmOverloads
public fun BigDecimal?.formatToPercent(
    decimal: Int = 2,
    roundingMode: RoundingMode = RoundingMode.DOWN,
): String =
    formatToPercent(this, decimal, roundingMode)

public val secureRandom: SecureRandom = SecureRandom()

/**
 * 生成指定位数随机数
 * @param [digit] 位数
 * @return [Int]
 * @author Tang Li
 * @date 2023/09/13 10:25
 * @since 1.0.0
 */
public fun genRandomNumber(digit: Int): Int {
    if (digit < 1) throw ApiException("随机数至少是一位数")
    if (digit == 1) {
        return secureRandom.nextInt(10)
    }
    val base =
        9 * (
            (10)
                .toDouble()
                .pow((digit - 1).toDouble())
                .toInt()
        )
    val fix =
        10
            .toDouble()
            .pow((digit - 1).toDouble())
            .toInt()
    return secureRandom.nextInt(base) + fix
}

private fun String?.toBigDecimal(decimal: Int = 2) =
    BigDecimal(this ?: "0")
        .setScale(decimal, RoundingMode.DOWN)

private fun formatToPercent(
    number: Number?,
    digit: Int,
    roundingMode: RoundingMode = RoundingMode.DOWN,
): String =
    NumberFormat
        .getPercentInstance()
        .apply {
            maximumFractionDigits = digit
            this.roundingMode = roundingMode
        }.format(number ?: 0)
