package com.tony.cache.test.script

import com.tony.cache.RedisManager
import com.tony.cache.test.TestCacheApp
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.script.DefaultRedisScript
import java.util.Collections

/**
 * RedisScriptTest is
 * @author tangli
 * @since 2023/05/31 11:00
 */
@SpringBootTest(classes = [TestCacheApp::class], webEnvironment = SpringBootTest.WebEnvironment.NONE)
class RedisScriptTest {

    // TODO
    val script = """|for i,key_pattern in KEYS do
                    |    local keys = redis.call('keys', key_pattern)
                    |    for i,key in ipairs(keys) do
                    |        local res = redis.call('del', key)
                    |    end
                    |end""".trimMargin()

    @Test
    fun scriptTest(){
        RedisManager.redisTemplate.execute(DefaultRedisScript<Long>(script), listOf("*1","*2"))
    }
}
