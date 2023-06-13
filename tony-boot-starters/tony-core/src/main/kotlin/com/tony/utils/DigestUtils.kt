@file:JvmName("DigestUtils")

package com.tony.utils

/**
 * 摘要工具类
 *
 * @author tangli
 * @since 2022/9/29 10:20
 */
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

private fun get(algorithm: String): MessageDigest =
    try {
        MessageDigest.getInstance(algorithm)
    } catch (e: NoSuchAlgorithmException) {
        throw IllegalArgumentException(e)
    }

public fun md5Digest(): MessageDigest = get(MD5)

public fun sha1Digest(): MessageDigest = get(SHA1)

public const val MD5: String = "MD5"

public const val SHA1: String = "SHA-1"
