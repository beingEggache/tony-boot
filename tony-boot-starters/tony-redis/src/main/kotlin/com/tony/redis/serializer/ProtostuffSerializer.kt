/*
 * MIT License
 *
 * Copyright (c) 2023-present, tangli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.tony.redis.serializer

import com.tony.redis.serializer.wrapper.ProtoWrapper
import com.tony.utils.isNumberTypes
import io.protostuff.LinkedBuffer
import io.protostuff.ProtobufException
import io.protostuff.ProtostuffIOUtil
import io.protostuff.Schema
import io.protostuff.runtime.RuntimeSchema
import java.text.NumberFormat
import org.slf4j.LoggerFactory
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.data.redis.serializer.SerializationException

/**
 * Protostuff redis 序列化.
 *
 * 生成的码流比jdk序列化小，速度更快.
 *
 * @author Tang Li
 * @date 2023/6/5 13:52
 */
internal class ProtostuffSerializer : RedisSerializer<Any?> {
    @Throws(SerializationException::class)
    override fun serialize(t: Any?): ByteArray {
        val buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE)
        if (t == null) {
            return emptyByteArray
        }
        // 数字类型直接字符串. 否则incre等命令会出问题.
        if (!canSerialize(t::class.java)) {
            return t
                .toString()
                .toByteArray()
        }
        return try {
            ProtostuffIOUtil.toByteArray(ProtoWrapper(t), schema, buffer)
        } finally {
            buffer.clear()
        }
    }

    @Throws(SerializationException::class)
    override fun deserialize(bytes: ByteArray?): Any? {
        if (bytes == null || bytes.isEmpty()) {
            return null
        }
        val newMessage = schema.newMessage()
        try {
            ProtostuffIOUtil.mergeFrom(bytes, newMessage, schema)
        } catch (e: RuntimeException) {
            if (e.cause is ProtobufException) {
                // increment or get enum will cause this.
                logger.warn(e.message)
                val string = String(bytes)
                string.toIntOrNull() ?: return string
                return NumberFormat
                    .getInstance()
                    .parse(string)
            } else {
                throw e
            }
        }
        return newMessage.data
    }

    override fun canSerialize(type: Class<*>): Boolean =
        !type.isNumberTypes()

    private companion object {
        @JvmStatic
        private val emptyByteArray =
            ByteArray(0)

        @JvmStatic
        private val schema: Schema<ProtoWrapper> =
            RuntimeSchema.getSchema(ProtoWrapper::class.java)

        @JvmStatic
        private val logger =
            LoggerFactory.getLogger(ProtostuffSerializer::class.java)
    }
}
