/**
 * tony-dependencies
 * DigestUtils
 *
 * @author tangli
 * @since 2022/9/29 10:20
 */
package com.tony.utils

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

private fun get(algorithm: String): MessageDigest = try {
    MessageDigest.getInstance(algorithm)
} catch (e: NoSuchAlgorithmException) {
    throw IllegalArgumentException(e)
}

fun md5Digest(): MessageDigest = get(MD5)

fun sha1Digest(): MessageDigest = get(SHA1)

const val MD5 = "MD5"

const val SHA1 = "SHA-1"
