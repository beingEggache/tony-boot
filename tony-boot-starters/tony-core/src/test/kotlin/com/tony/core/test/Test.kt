package com.tony.core.test

import com.tony.utils.println
import com.tony.utils.toMd5UppercaseString
import java.security.MessageDigest

/**
 *
 * @author tangli
 * @since 2021-05-21 12:48
 */
class Test {

    fun testIfNullOrEmpty() {

    }
}

fun main() {
    "123456".toMd5UppercaseString().println()
    MessageDigest.getInstance("md5").digest()
}
