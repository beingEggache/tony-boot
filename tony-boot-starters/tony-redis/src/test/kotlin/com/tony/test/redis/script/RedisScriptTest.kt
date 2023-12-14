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

package com.tony.test.redis.script

import com.tony.redis.RedisManager
import com.tony.redis.RedisManager.toRedisScript
import com.tony.test.redis.TestRedisApp
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInfo
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest

/**
 * RedisScriptTest is
 * @author Tang Li
 * @date 2023/05/31 19:00
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
            RedisManager.executeScript<String>(script.toRedisScript(), listOf("hello", """"aloha""""), listOf("go", "fuck", "yourself"))
        logger.info(result.toString())
    }

}
