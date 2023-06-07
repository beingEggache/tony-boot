package com.tony.redis.serializer

import com.tony.redis.serializer.wrapper.ProtoWrapper
import com.tony.utils.isNumberTypes
import io.protostuff.LinkedBuffer
import io.protostuff.ProtobufException
import io.protostuff.ProtostuffIOUtil
import io.protostuff.Schema
import io.protostuff.runtime.RuntimeSchema
import org.slf4j.LoggerFactory
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.data.redis.serializer.SerializationException
import java.text.NumberFormat

/**
 * Protostuff redis 序列化.
 *
 * 生成的码流比jdk序列化小，速度更快.
 *
 * @author tangli
 * @since 2023/6/5 13:52
 */
public class ProtostuffSerializer : RedisSerializer<Any?> {

    @Throws(SerializationException::class)
    override fun serialize(t: Any?): ByteArray {
        val buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE)
        if (t == null) {
            return emptyByteArray
        }
        // 数字类型直接字符串. 否则incre等命令会出问题.
        if (!canSerialize(t::class.java)) {
            return t.toString().toByteArray()
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
                // only incr command will cause this.
                logger.warn("Maybe source is a number literal string.")
                return NumberFormat.getInstance().parse(String(bytes))
            } else {
                throw e
            }
        }
        return newMessage.data
    }

    override fun canSerialize(type: Class<*>): Boolean =
        type.isNumberTypes()
}

private val emptyByteArray = ByteArray(0)

private val schema: Schema<ProtoWrapper> = RuntimeSchema.getSchema(ProtoWrapper::class.java)

private val logger = LoggerFactory.getLogger(RedisSerializer::class.java)
