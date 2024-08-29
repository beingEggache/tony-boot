/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine.assist

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.util.function.Function

/**
 * 流数据帮助类
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public object StreamUtils {
    @JvmStatic
    public fun getResourceAsStream(name: String): InputStream {
        val classLoader = Thread.currentThread().contextClassLoader
        var stream = classLoader.getResourceAsStream(name)
        if (null == stream) {
            stream = StreamUtils::class.java.classLoader.getResourceAsStream(name)
        }
        if (stream == null) {
            throw Assert.throwable("resource $name does not exist")
        }
        return stream
    }

    @JvmStatic
    public fun <T> readBytes(
        `in`: InputStream,
        function: Function<String, T>,
    ): T {
        Assert.isNull(`in`)
        try {
            return function.apply(readBytes(`in`))
        } catch (e: Exception) {
            throw Assert.throwable(e.message, e)
        }
    }

    @JvmStatic
    @Throws(IOException::class)
    public fun readBytes(`in`: InputStream): String {
        ByteArrayOutputStream().use { out ->
            val buffer = ByteArray(4096)
            var count: Int
            while ((`in`.read(buffer).also { count = it }) != -1) {
                out.write(buffer, 0, count)
            }
            return out.toString(StandardCharsets.UTF_8.toString())
        }
    }
}
