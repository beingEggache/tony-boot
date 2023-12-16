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

/**
 * RedisCacheAnnoTests
 *
 * TODO
 *
 * @author Tang Li
 * @date 2022/04/19 19:41
 */
package com.tony.test.redis

import com.tony.utils.println
import jakarta.annotation.Resource
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [TestRedisApp::class], webEnvironment = SpringBootTest.WebEnvironment.NONE)
class RedisCacheAspectTests {

    @Resource
    private lateinit var testRedisCacheAnnoService: TestRedisCacheAnnoService

    @Test
    fun testCacheAnno() {
        testRedisCacheAnnoService.testCache1()
        testRedisCacheAnnoService.testCache2()
    }

    @Test
    fun testRCacheAnno() {
        testRedisCacheAnnoService.rTestCache()
    }

    @Test
    fun testCacheNameAnnoKeyWithoutEmpty() {
        testRedisCacheAnnoService.testCacheNameBoolean("boolean").println()
        testRedisCacheAnnoService.testCacheNameBooleanOrNull("Boolean").println()
        testRedisCacheAnnoService.testCacheNameByte("byte").println()
        testRedisCacheAnnoService.testCacheNameByteOrNull("Byte").println()
        testRedisCacheAnnoService.testCacheNameShort("short").println()
        testRedisCacheAnnoService.testCacheNameShortOrNull("Short").println()
        testRedisCacheAnnoService.testCacheNameInt("int").println()
        testRedisCacheAnnoService.testCacheNameInteger("Integer").println()
        testRedisCacheAnnoService.testCacheNameLong("long").println()
        testRedisCacheAnnoService.testCacheNameLongOrNull("Long").println()
        testRedisCacheAnnoService.testCacheNameFloat("float").println()
        testRedisCacheAnnoService.testCacheNameFloatOrNull("Float").println()
        testRedisCacheAnnoService.testCacheNameDouble("double").println()
        testRedisCacheAnnoService.testCacheNameDoubleOrNull("Double").println()
        testRedisCacheAnnoService.testCacheNameBigDecimal("BigDecimal").println()
        testRedisCacheAnnoService.testCacheNameBigInteger("BigInteger").println()
        testRedisCacheAnnoService.testCacheNameMap("Map").println()
        testRedisCacheAnnoService.testCacheMapObj("mapobj").println()
        testRedisCacheAnnoService.testCacheNameObj("Obj").println()
        testRedisCacheAnnoService.testCacheList("list").println()
        testRedisCacheAnnoService.testCacheListObj("listobj").println()
    }

    @Test
    fun testCacheNameAnnoKeyWithObj() {
        testRedisCacheAnnoService.testCacheAnnoObj(
            TestCacheObj("aloha"),
            TestCacheObj("he he"),
            TestCacheObj("haha"),
        )
    }

}
