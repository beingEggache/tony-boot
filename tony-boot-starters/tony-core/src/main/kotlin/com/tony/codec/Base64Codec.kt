package com.tony.codec

import java.util.Base64

/**
 * base64编解码器
 */
public data object Base64Codec : Codec {

    override fun encodeToByteArray(src: ByteArray): ByteArray {
        return Base64.getUrlEncoder().encode(src)
    }

    override fun decodeToByteArray(src: ByteArray): ByteArray {
        return Base64.getUrlDecoder().decode(src)
    }
}
