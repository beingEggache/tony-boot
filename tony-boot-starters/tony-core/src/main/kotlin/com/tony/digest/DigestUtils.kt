@file:JvmName("DigestUtils")

package com.tony.digest

/**
 * 摘要工具类
 *
 * @author Tang Li
 * @date 2022/9/29 10:20
 */
import com.tony.digest.enums.DigestAlgorithm

/**
 * 字符串转为MD5
 */
public fun String.md5(): String =
    DigestAlgorithm.MD5.digest(this)

/**
 * 字符串转为 sha1
 */
public fun String.sha1(): String =
    DigestAlgorithm.SHA1.digest(this)

/**
 * 字符串转为 sha256
 */
public fun String.sha256(): String =
    DigestAlgorithm.SHA256.digest(this)
