@file:JvmName("ByteArrayUtils")

package com.tony.utils

import java.util.Base64

/**
 * ByteUtils is
 * @author tangli
 * @since 2023/05/25 17:16
 */

/**
 * 转为 base64表示
 * @return
 */
public fun ByteArray.encodeToBase64(): ByteArray =
    if (this.isEmpty()) {
        this
    } else {
        Base64.getEncoder().encode(this)
    }

/**
 * 转为 base64表示
 * @return
 */
public fun ByteArray.encodeToBase64UrlSafe(): ByteArray =
    if (this.isEmpty()) {
        this
    } else {
        Base64.getUrlEncoder().encode(this)
    }

/**
 * decodeToBase64
 * @return
 */
public fun ByteArray.decodeBase64(): ByteArray =
    if (this.isEmpty()) {
        this
    } else {
        Base64.getDecoder().decode(this)
    }

/**
 * decodeToBase64
 * @return
 */
public fun ByteArray.decodeBase64UrlSafe(): ByteArray =
    if (this.isEmpty()) {
        this
    } else {
        Base64.getUrlDecoder().decode(this)
    }

/**
 * 0123456789abcdef
 */
internal val DIGITS_LOWER = "0123456789abcdef".toCharArray().map { it.code.toByte() }.toByteArray()

/**
 * 0123456789ABCDEF
 */
internal val DIGITS_UPPER = "0123456789ABCDEF".toCharArray().map { it.code.toByte() }.toByteArray()

/**
 * encode Hex
 *
 * @param lowerCase default true
 * @return hex bytes
 */
@JvmOverloads
public fun ByteArray.encodeToHex(lowerCase: Boolean = true): ByteArray =
    encodeToHex(if (lowerCase) DIGITS_LOWER else DIGITS_UPPER)

/**
 * encode Hex
 *
 * @receiver Data to Hex
 * @return bytes as a hex string
 */
public fun ByteArray.encodeToHex(digits: ByteArray): ByteArray {
    val out = ByteArray(size shl 1)
    var i = 0
    var j = 0
    while (i < size) {
        out[j++] = digits[0xF0 and this[i].toInt() ushr 4]
        out[j++] = digits[0xF and this[i].toInt()]
        i++
    }
    return out
}

/**
 * decode Hex
 *
 * @receiver data Hex data
 * @return decode hex to bytes
 */
public fun ByteArray.decodeHex(): ByteArray {
    val len = size
    require(len and 0x01 == 0) { "hexBinary needs to be even-length: $len" }
    val out = ByteArray(len shr 1)
    var i = 0
    var j = 0
    while (j < len) {
        var f = Character.digit(this[j].toInt(), 16) shl 4
        j++
        f = f or Character.digit(this[j].toInt(), 16)
        j++
        out[i] = (f and 0xFF).toByte()
        i++
    }
    return out
}
