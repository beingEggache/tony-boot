package com.tony.test.redis.fury.serializer

import com.tony.utils.isNumberTypes
import io.fury.Fury
import io.fury.config.Language
import org.slf4j.LoggerFactory
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.data.redis.serializer.SerializationException
import java.text.NumberFormat

/**
 * FurySerializer is
 * @author tangli
 * @date 2024/01/25 09:54
 * @since 1.0.0
 */
class FurySerializer : RedisSerializer<Any?> {

    private val logger = LoggerFactory.getLogger(FurySerializer::class.java)

    @Throws(SerializationException::class)
    override fun serialize(t: Any?): ByteArray {
        if (t == null) {
            return ByteArray(0)
        }
        fury.register(t::class.java)
        return fury.serialize(t)
    }

    override fun deserialize(bytes: ByteArray?): Any? {
        if (bytes?.isNotEmpty() == true) {
            val result = try {
                fury.deserialize(bytes)
            } catch (e: RuntimeException) {
                logger.warn(e.message)
                null
            }

            if (result != null) {
                return result
            }
            val string = String(bytes)
            string.toIntOrNull() ?: return string
            return NumberFormat
                .getInstance()
                .parse(string)
        }
        return null
    }

    override fun canSerialize(type: Class<*>): Boolean =
        !type.isNumberTypes()

    internal companion object {
        @get:JvmStatic
        val fury: Fury by lazy {
            Fury
                .builder()
                .requireClassRegistration(false)
                .suppressClassRegistrationWarnings(true)
                .withLanguage(Language.JAVA)
                .build()
        }
    }
}
