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

package com.tony.test.redis

import com.tony.exception.BizException
import com.tony.redis.RedisManager
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.RepetitionInfo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInfo
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest

/**
 *
 * @author Tang Li
 * @date 2021-05-19 15:22
 */

@SpringBootTest(classes = [TestRedisApp::class], webEnvironment = SpringBootTest.WebEnvironment.NONE)
class RedisManagerTests {

    private val logger = LoggerFactory.getLogger(RedisManagerTests::class.java)

    @Execution(ExecutionMode.CONCURRENT)
    @Test
    fun testListener(testInfo: TestInfo) {
        val keyPrefix = testInfo.testMethod.get().name

        RedisManager.values.set("$keyPrefix:testExpire0", "year")
        RedisManager.values.set("$keyPrefix:testExpire1", "year", 1)
        RedisManager.values.set("$keyPrefix:testExpire2", "year", 2)
        RedisManager.values.set("$keyPrefix:testExpire3", "year", 3)
        RedisManager.values.set("$keyPrefix:testExpire4", "year", 4)

        Thread.sleep(10 * 1000)

        RedisManager.deleteByKeyPatterns("$keyPrefix:*")
    }

    @Execution(ExecutionMode.CONCURRENT)
    @RepeatedTest(100)
    fun testMulti(testInfo: TestInfo, repetitionInfo: RepetitionInfo) {
        val keyPrefix = testInfo.testMethod.get().name + repetitionInfo.currentRepetition
        Assertions.assertThrows(BizException::class.java) {
            val result = RedisManager.doInTransaction {
                RedisManager.values.set("$keyPrefix:Multi a", "a")
                RedisManager.values.set("$keyPrefix:Multi b", "b")
                RedisManager.values.set("$keyPrefix:Multi c", "c")
                RedisManager.deleteByKeyPatterns("$keyPrefix:*")
            }
            logger.info(result.toString())

            RedisManager.doInTransaction {
                RedisManager.values.set("$keyPrefix:2 Multi a", "a")
                RedisManager.values.set("$keyPrefix:2 Multi b", "b")
                throw BizException("")
            }
        }
    }
}
