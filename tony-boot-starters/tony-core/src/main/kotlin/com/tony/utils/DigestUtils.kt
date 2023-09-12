@file:JvmName("DigestUtils")

package com.tony.utils

/**
 * 摘要工具类
 *
 * @author Tang Li
 * @date 2022/9/29 10:20
 */
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * 获取摘要算法
 * @param [algorithm] 算法
 * @return [MessageDigest]
 * @author Tang Li
 * @date 2023/09/12 10:34
 * @since 1.0.0
 */
public fun getDigest(algorithm: String): MessageDigest =
    try {
        MessageDigest.getInstance(algorithm)
    } catch (e: NoSuchAlgorithmException) {
        throw IllegalArgumentException(e)
    }

/**
 * md5摘要算法
 * @return [MessageDigest]
 * @author Tang Li
 * @date 2023/09/12 10:34
 * @since 1.0.0
 */
public fun md5Digest(): MessageDigest = getDigest(MD5)

/**
 * sha1摘要算法
 * @return [MessageDigest]
 * @author Tang Li
 * @date 2023/09/12 10:34
 * @since 1.0.0
 */
public fun sha1Digest(): MessageDigest = getDigest(SHA1)

/**
 * sha256摘要算法
 * @return [MessageDigest]
 * @author Tang Li
 * @date 2023/09/12 10:35
 * @since 1.0.0
 */
public fun sha256Digest(): MessageDigest = getDigest(SHA256)

public const val MD5: String = "MD5"

public const val SHA1: String = "SHA-1"

public const val SHA256: String = "SHA-256"
