package com.tony.cache.aspect.script

import com.tony.cache.RedisManager
import com.tony.cache.test.TestCacheApp
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest

/**
 * RedisScriptTest is
 * @author tangli
 * @since 2023/05/31 11:00
 */
@SpringBootTest(classes = [TestCacheApp::class], webEnvironment = SpringBootTest.WebEnvironment.NONE)
class RedisScriptTest {

    private val logger = LoggerFactory.getLogger(RedisScriptTest::class.java)

    @Test
    fun deleteByScriptTest() {
        val keyPrefix = "test_script"
        RedisManager.values.set("$keyPrefix:int1", 1)
        RedisManager.values.set("$keyPrefix:int2", 2)
        RedisManager.values.set("$keyPrefix:int3", 3)
        RedisManager.values.set("$keyPrefix:int4", 4)

        val keyPrefix2 = "test_script2"
        RedisManager.values.set("$keyPrefix2:int1", 1)
        RedisManager.values.set("$keyPrefix2:int2", 2)
        RedisManager.values.set("$keyPrefix2:int3", 3)
        RedisManager.values.set("$keyPrefix2:int4", 4)
        RedisManager.values.set("$keyPrefix2:int5", 5)

        val deletedCount = RedisManager.deleteByKeyPatterns("$keyPrefix:*", "$keyPrefix2:*")
        logger.info("deletedCount:$deletedCount")
    }

    @Test
    fun scriptTest() {
    }

}
