@file:JvmName("ByteArrayUtils")

package com.tony.utils

import java.nio.charset.Charset

/**
 * [ByteArray] 转字符串
 * @param [charset] 字符集
 * @return [String]
 * @author Tang Li
 * @date 2023/09/12 10:53
 * @since 1.0.0
 */
public fun ByteArray.string(charset: Charset = Charsets.UTF_8): String = toString(charset)
