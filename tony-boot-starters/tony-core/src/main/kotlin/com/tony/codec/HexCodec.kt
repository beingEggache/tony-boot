package com.tony.codec

import com.tony.utils.string
import java.util.HexFormat

/**
 * 十六进制编解码器
 */
public data object HexCodec : Codec {

    @JvmStatic
    private val DIGITS_LOWER = "0123456789abcdef".toCharArray().map { it.code.toByte() }.toByteArray()

    override fun encodeToString(src: ByteArray?): String {
        if (src?.isNotEmpty() != true) {
            return ""
        }
        return HexFormat.of().formatHex(src)
    }

    override fun encodeToString(src: String?): String {
        if (src?.isNotEmpty() != true) {
            return ""
        }
        return HexFormat.of().formatHex(src.toByteArray())
    }

    override fun decodeToByteArray(src: String?): ByteArray {
        if (src?.isNotEmpty() != true) {
            return ByteArray(0)
        }
        return HexFormat.of().parseHex(src)
    }

    override fun decodeToString(src: String?): String {
        return decodeToByteArray(src).string()
    }

    override fun decodeToString(src: ByteArray?): String {
        return HexFormat.of().parseHex(src?.string()).string()
    }

    override fun encodeToByteArray(src: ByteArray?): ByteArray {
        if (src?.isNotEmpty() != true) {
            return ByteArray(0)
        }
        val out = ByteArray(src.size shl 1)
        var i = 0
        var j = 0
        while (i < src.size) {
            out[j++] = DIGITS_LOWER[0xF0 and src[i].toInt() ushr 4]
            out[j++] = DIGITS_LOWER[0xF and src[i].toInt()]
            i++
        }
        return out
    }

    override fun decodeToByteArray(src: ByteArray?): ByteArray {
        if (src?.isNotEmpty() != true) {
            return ByteArray(0)
        }
        val len = src.size
        require(len and 0x01 == 0) { "hexBinary needs to be even-length: $len" }
        val out = ByteArray(len shr 1)
        var i = 0
        var j = 0
        while (j < len) {
            var f = Character.digit(src[j].toInt(), 16) shl 4
            j++
            f = f or Character.digit(src[j].toInt(), 16)
            j++
            out[i] = (f and 0xFF).toByte()
            i++
        }
        return out
    }
}
