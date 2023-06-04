package com.tony.redis.serializer

import com.tony.utils.getLogger
import io.protostuff.LinkedBuffer
import io.protostuff.ProtobufException
import io.protostuff.ProtostuffIOUtil
import io.protostuff.Schema
import io.protostuff.runtime.RuntimeSchema
import org.springframework.cache.interceptor.SimpleKey
import org.springframework.core.convert.ConversionService
import org.springframework.core.convert.support.DefaultConversionService
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.data.redis.serializer.SerializationException
import java.math.BigDecimal
import java.math.BigInteger
import java.text.NumberFormat
import java.util.Objects


/**
 * redis key value 序列化反序列化.
 *
 * @author tangli
 * @since 2023/5/24 18:12
 */
internal class ProtoWrapper @JvmOverloads constructor(var data: Any? = null)

/**
 * Created by zkk on 2019/3/14
 * 增加protostuff序列化方式，生成的码流比jdk序列化小，速度更快
 * 解决devtool热加载在jdk序列化下类型转换报错的情况
 */
class ProtostuffSerializer : RedisSerializer<Any?> {

    private val logger = getLogger()
    private val buffer: LinkedBuffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE)

    @Throws(SerializationException::class)
    override fun serialize(t: Any?): ByteArray {
        if (t == null) {
            return emptyByteArray
        }
        // 数字类型直接字符串吧. 否则incre等命令会出问题.
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

    override fun canSerialize(type: Class<*>): Boolean {
        return arrayOf(
            Byte::class.java,
            java.lang.Byte::class.java,
            Short::class.java,
            java.lang.Short::class.java,
            Int::class.java,
            java.lang.Integer::class.java,
            Long::class.java,
            java.lang.Long::class.java,
            BigInteger::class.java,
            Float::class.java,
            java.lang.Float::class.java,
            Double::class.java,
            java.lang.Double::class.java,
            BigDecimal::class.java,
        ).none {
            it.isAssignableFrom(type)
        }
    }

    private companion object {
        @JvmStatic
        private val schema: Schema<ProtoWrapper> = RuntimeSchema.getSchema(ProtoWrapper::class.java)

        @JvmStatic
        private val emptyByteArray = ByteArray(0)
    }
}


/**
 * 将redis key序列化为字符串
 *
 *
 *
 * spring cache中的简单基本类型直接使用 StringRedisSerializer 会有问题
 *
 *
 */
public class RedisKeySerializer :
    RedisSerializer<Any?> {
    private val converter: ConversionService = DefaultConversionService.getSharedInstance()

    override fun deserialize(bytes: ByteArray?): Any? {
        return bytes?.let { String(it, Charsets.UTF_8) }
    }

    override fun serialize(obj: Any?): ByteArray? {
        Objects.requireNonNull(obj, "redis key is null")
        val key = when (obj) {
            is SimpleKey -> ""
            is String -> obj
            else -> converter.convert(obj, String::class.java)
        }
        return Objects.requireNonNull(key)?.toByteArray()
    }
}


