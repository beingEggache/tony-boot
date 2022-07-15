/**
 * RedisCacheAnnoTests
 *
 * TODO
 *
 * @author tangli
 * @since 2022/4/19 17:41
 */
package com.tony.cache.test

import com.tony.utils.println
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import javax.annotation.Resource

@SpringBootTest(classes = [TestCacheApp::class], webEnvironment = SpringBootTest.WebEnvironment.NONE)
class RedisCacheAnnoTests {

    @Resource
    private lateinit var testRedisCacheAnnoService: TestRedisCacheAnnoService

    @Resource
    private lateinit var testRedisCacheAnnoWithEmptyService: TestRedisCacheAnnoWithEmptyService

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
        testRedisCacheAnnoService.testCacheNameObj("Obj").println()
    }

    @Test
    fun testCacheNameAnnoKeyWithEmpty() {
        testRedisCacheAnnoWithEmptyService.testCacheNameBoolean("boolean").println()
        testRedisCacheAnnoWithEmptyService.testCacheNameBooleanOrNull("Boolean").println()
        testRedisCacheAnnoWithEmptyService.testCacheNameByte("byte").println()
        testRedisCacheAnnoWithEmptyService.testCacheNameByteOrNull("Byte").println()
        testRedisCacheAnnoWithEmptyService.testCacheNameShort("short").println()
        testRedisCacheAnnoWithEmptyService.testCacheNameShortOrNull("Short").println()
        testRedisCacheAnnoWithEmptyService.testCacheNameInt("int").println()
        testRedisCacheAnnoWithEmptyService.testCacheNameInteger("Integer").println()
        testRedisCacheAnnoWithEmptyService.testCacheNameLong("long").println()
        testRedisCacheAnnoWithEmptyService.testCacheNameLongOrNull("Long").println()
        testRedisCacheAnnoWithEmptyService.testCacheNameFloat("float").println()
        testRedisCacheAnnoWithEmptyService.testCacheNameFloatOrNull("Float").println()
        testRedisCacheAnnoWithEmptyService.testCacheNameDouble("double").println()
        testRedisCacheAnnoWithEmptyService.testCacheNameDoubleOrNull("Double").println()
        testRedisCacheAnnoWithEmptyService.testCacheNameBigDecimal("BigDecimal").println()
        testRedisCacheAnnoWithEmptyService.testCacheNameBigInteger("BigInteger").println()
        testRedisCacheAnnoWithEmptyService.testCacheNameMap("Map").println()
        testRedisCacheAnnoWithEmptyService.testCacheNameObj("Obj").println()
    }

}
