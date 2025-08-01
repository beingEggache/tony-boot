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

package tony.test.redis.protostuff

import tony.redis.RedisKeys
import tony.redis.RedisManager
import tony.test.redis.TestRedisApp
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInfo
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigInteger

/**
 * RedisListTests
 *
 * @author tangli
 * @date 2023/06/14 19:04
 */
@Suppress("SpringBootApplicationProperties")
@SpringBootTest(
    properties = ["redis.serializerMode=PROTOSTUFF"],
    classes = [TestRedisApp::class],
    webEnvironment = SpringBootTest.WebEnvironment.NONE
)
class ProtostuffRedisListTests {

    private val logger = LoggerFactory.getLogger(ProtostuffRedisListTests::class.java)


    @Execution(ExecutionMode.CONCURRENT)
    @Test
    fun testRedisListsNumber(testInfo: TestInfo) {
        val keyPrefix = testInfo.testMethod.get().name
        number(keyPrefix, arrayOf(1.toByte(), 2.toByte(), 3.toByte()))
        number(keyPrefix, arrayOf(1.toShort(), 2.toShort(), 3.toShort()))
        number(keyPrefix, arrayOf(1, 2, 3))
        number(keyPrefix, arrayOf(1L, 2L, 3L))
        number(keyPrefix, arrayOf(BigInteger.valueOf(1L), BigInteger.valueOf(2L), BigInteger.valueOf(3L)))
        number(keyPrefix, arrayOf(1.0f, 2.0f, 3.0f))
        number(keyPrefix, arrayOf(1.0, 2.0, 3.0))
        number(keyPrefix, arrayOf(1.0.toBigDecimal(), 2.0.toBigDecimal(), 3.0.toBigDecimal()))

        RedisManager.deleteByKeyPatterns("$keyPrefix:*")
    }

    private inline fun <reified T : Number> number(keyPrefix: String?, array: Array<T>) {
        val testListNumberKey = RedisKeys.genKey("$keyPrefix:list:number:${T::class.java.simpleName.lowercase()}")

        array.forEach {
            RedisManager.lists.leftPush(testListNumberKey, it)
        }

        array.forEachIndexed { index, _ ->
            val indexInt0 = RedisManager.lists.index<T>(testListNumberKey, index.toLong())
            logger.info("$testListNumberKey index $indexInt0")
        }
        repeat(array.count()) {
            val leftPopInt1 = RedisManager.lists.leftPop<T>(testListNumberKey)
            logger.info("$testListNumberKey pop $leftPopInt1")
        }
    }

    @Execution(ExecutionMode.CONCURRENT)
    @Test
    fun testRedisListsBoolean(testInfo: TestInfo) {
        val keyPrefix = testInfo.testMethod.get().name
        val testListBooleanKey = RedisKeys.genKey("$keyPrefix:list:boolean")

        RedisManager.lists.leftPush(testListBooleanKey, true)
        RedisManager.lists.leftPush(testListBooleanKey, false)
        RedisManager.lists.leftPush(testListBooleanKey, false)

        val indexBoolean0 = RedisManager.lists.index<Boolean>(testListBooleanKey, 0)
        logger.info("$testListBooleanKey pop $indexBoolean0")
        val indexBoolean1 = RedisManager.lists.index<Boolean>(testListBooleanKey, 1)
        logger.info("$testListBooleanKey pop $indexBoolean1")
        val indexBoolean2 = RedisManager.lists.index<Boolean>(testListBooleanKey, 2)
        logger.info("$testListBooleanKey pop $indexBoolean2")

        val leftPopBoolean1 = RedisManager.lists.leftPop<Boolean>(testListBooleanKey)
        logger.info("$testListBooleanKey pop $leftPopBoolean1")
        val leftPopBoolean2 = RedisManager.lists.leftPop<Boolean>(testListBooleanKey)
        logger.info("$testListBooleanKey pop $leftPopBoolean2")
        val leftPopBoolean3 = RedisManager.lists.leftPop<Boolean>(testListBooleanKey)
        logger.info("$testListBooleanKey pop $leftPopBoolean3")

        RedisManager.deleteByKeyPatterns("$keyPrefix:*")
    }
}

