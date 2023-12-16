package com.tony.test.redis.jackson

import com.tony.redis.RedisKeys
import com.tony.redis.RedisManager
import com.tony.test.redis.TestRedisApp
import com.tony.test.redis.jackson.service.JacksonRedisCacheAspectService
import com.tony.test.redis.jackson.service.JacksonRedisCacheAspectService.Companion.cacheKeyTemplate
import com.tony.utils.getLogger
import com.tony.utils.toJsonString
import jakarta.annotation.Resource
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal
import java.math.BigInteger
import java.util.Locale


@SpringBootTest(
    properties = [
        "redis.serializerMode=JACKSON",
    ],
    classes = [TestRedisApp::class],
    webEnvironment = SpringBootTest.WebEnvironment.NONE
)
class JacksonRedisCacheAspectTests {

    private val logger = getLogger()

    @Resource
    private lateinit var jacksonRedisCacheAspectService: JacksonRedisCacheAspectService

    @Test
    fun testBoolean() {
        testInternal<Boolean> { key ->
            jacksonRedisCacheAspectService.testBoolean(key, null as Boolean?)
        }
        testInternal<Boolean> { key ->
            jacksonRedisCacheAspectService.testBoolean(key, true)
        }
        testInternal<Boolean> { key ->
            jacksonRedisCacheAspectService.testBoolean(key, false)
        }
    }

    @Test
    fun testNumber() {
        testInternal<Int> { key ->
            jacksonRedisCacheAspectService.testNumber(key, null as Int?)
        }
        testInternal { key ->
            jacksonRedisCacheAspectService.testNumber(key, Byte.MAX_VALUE)
        }
        testInternal { key ->
            jacksonRedisCacheAspectService.testNumber(key, Short.MAX_VALUE)
        }
        testInternal { key ->
            jacksonRedisCacheAspectService.testNumber(key, Int.MAX_VALUE)
        }
        testInternal { key ->
            jacksonRedisCacheAspectService.testNumber(key, Long.MAX_VALUE)
        }
        testInternal { key ->
            jacksonRedisCacheAspectService.testNumber(key, Float.MAX_VALUE)
        }
        testInternal { key ->
            jacksonRedisCacheAspectService.testNumber(key, Double.MAX_VALUE)
        }
        testInternal { key ->
            jacksonRedisCacheAspectService.testNumber(key, BigDecimal.ONE)
        }
        testInternal { key ->
            jacksonRedisCacheAspectService.testNumber(key, BigInteger.ONE)
        }

    }

    @Test
    fun testString() {
        testInternal { key ->
            jacksonRedisCacheAspectService.testString(key, null)
        }
        testInternal { key ->
            jacksonRedisCacheAspectService.testString(key, "")
        }
        testInternal { key ->
            jacksonRedisCacheAspectService.testString(key, "hello")
        }
        testInternal { key ->
            jacksonRedisCacheAspectService.testString(key, "aloha")
        }
    }

    @Test
    fun testArray() {
        testInternal { key ->
            jacksonRedisCacheAspectService.testArray(key, null)
        }
        testInternal { key ->
            jacksonRedisCacheAspectService.testArray(key, arrayOf(Byte.MAX_VALUE))
        }
        testInternal { key ->
            jacksonRedisCacheAspectService.testArray(key, arrayOf(Short.MAX_VALUE))
        }
        testInternal { key ->
            jacksonRedisCacheAspectService.testArray(key, arrayOf(Int.MAX_VALUE))
        }
        testInternal { key ->
            jacksonRedisCacheAspectService.testArray(key, arrayOf(Long.MAX_VALUE))
        }
        testInternal { key ->
            jacksonRedisCacheAspectService.testArray(key, arrayOf(Float.MAX_VALUE))
        }
        testInternal { key ->
            jacksonRedisCacheAspectService.testArray(key, arrayOf(Double.MAX_VALUE))
        }
        testInternal { key ->
            jacksonRedisCacheAspectService.testArray(key, arrayOf(BigDecimal.ONE))
        }
        testInternal { key ->
            jacksonRedisCacheAspectService.testArray(key, arrayOf(BigInteger.ONE))
        }
    }

    private inline fun <reified T : Any> testInternal(crossinline func: (String) -> T?) {
        val key = T::class.java.simpleName.replaceFirstChar { it.lowercase(Locale.getDefault()) }
        val value = func(key)
        val cacheKey = RedisKeys.genKey(cacheKeyTemplate, key, value.toJsonString())
        logger.info("$key key: $cacheKey")
        func(key)
        logger.info("$key value:${value.toJsonString()}")
        check(value == RedisManager.values.get<T>(cacheKey))
        RedisManager.delete(key)
    }
}
