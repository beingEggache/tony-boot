package com.tony.redis.test

import com.tony.redis.RedisKeys
import com.tony.redis.RedisManager
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInfo
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest

/**
 * RedisListTests
 *
 * @author tangli
 * @since 2023/6/14 18:04
 */
@SpringBootTest(classes = [TestRedisApp::class], webEnvironment = SpringBootTest.WebEnvironment.NONE)
class RedisListTests {

    private val logger = LoggerFactory.getLogger(RedisManagerTests::class.java)


    @Execution(ExecutionMode.CONCURRENT)
    @Test
    fun testRedisLists(testInfo: TestInfo) {
        val keyPrefix = testInfo.testMethod.get().name
        val testListBooleanKey = RedisKeys.genKey("$keyPrefix:list:boolean")

        RedisManager.lists.leftPush(testListBooleanKey, true)
        RedisManager.lists.leftPush(testListBooleanKey, false)
        RedisManager.lists.leftPush(testListBooleanKey, false)

        val leftPopBoolean1 = RedisManager.lists.leftPop<Boolean>(testListBooleanKey)
        logger.info("$testListBooleanKey pop $leftPopBoolean1")
        val leftPopBoolean2 = RedisManager.lists.leftPop<Boolean>(testListBooleanKey)
        logger.info("$testListBooleanKey pop $leftPopBoolean2")
        val leftPopBoolean3 = RedisManager.lists.leftPop<Boolean>(testListBooleanKey)
        logger.info("$testListBooleanKey pop $leftPopBoolean3")

        RedisManager.deleteByKeyPatterns("$keyPrefix:*")
    }
}

