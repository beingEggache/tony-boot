package com.tony.redis.test.script

import com.tony.redis.RedisManager
import com.tony.redis.RedisManager.toRedisScript
import com.tony.redis.test.TestRedisApp
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInfo
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest

/**
 * RedisScriptTest is
 * @author tangli
 * @since 2023/05/31 11:00
 */
@SpringBootTest(classes = [TestRedisApp::class], webEnvironment = SpringBootTest.WebEnvironment.NONE)
class RedisScriptTest {

    private val logger = LoggerFactory.getLogger(RedisScriptTest::class.java)

    @ParameterizedTest
    @ValueSource(ints = [100])
    fun deleteByScriptTest(count: Int, testInfo: TestInfo) {
        val methodName = testInfo.testMethod.get().name
        (0 until count).forEach {
            RedisManager.values.set("$methodName:$it", it)
        }
        (0 until count).forEach {
            RedisManager.values.set("${methodName}2:$it", it)
        }

        val deletedCount = RedisManager.deleteByKeyPatterns("$methodName:*", "${methodName}2:*")
        logger.info("deletedCount:$deletedCount")
    }

    @Test
    fun scriptTest() {
        val script = """
            local keys = KEYS
            return keys
        """.trimIndent()
        val result =
            RedisManager.executeScript<String>(script.toRedisScript(), listOf("hello", "aloha"), listOf("go", "fuck", "yourself"))
        logger.info(result.toString())
    }

}
