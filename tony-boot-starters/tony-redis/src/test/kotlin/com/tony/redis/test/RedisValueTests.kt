package com.tony.redis.test

import com.tony.enums.EnumValue
import com.tony.redis.RedisManager
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal
import java.math.BigInteger

@SpringBootTest(classes = [TestRedisApp::class], webEnvironment = SpringBootTest.WebEnvironment.NONE)
class RedisValueTests {

    private val logger = LoggerFactory.getLogger(RedisValueTests::class.java)

    @Execution(ExecutionMode.CONCURRENT)
    @Test
    fun testSetNumber() {
        setAndGetNumber(Byte::class.java, RedisOper.SET)
        setAndGetNumber(Short::class.java, RedisOper.SET)
        setAndGetNumber(Int::class.java, RedisOper.SET)
        setAndGetNumber(Long::class.java, RedisOper.SET)
        setAndGetNumber(BigInteger::class.java, RedisOper.SET)
        setAndGetNumber(Float::class.java, RedisOper.SET)
        setAndGetNumber(Double::class.java, RedisOper.SET)
        setAndGetNumber(BigDecimal::class.java, RedisOper.SET)
    }

    @Execution(ExecutionMode.CONCURRENT)
    @Test
    fun testSetNxNumber() {
        setAndGetNumber(Byte::class.java, RedisOper.SETNX, 1L)
        setAndGetNumber(Short::class.java, RedisOper.SETNX, 1L)
        setAndGetNumber(Int::class.java, RedisOper.SETNX, 1L)
        setAndGetNumber(Long::class.java, RedisOper.SETNX, 1L)
        setAndGetNumber(BigInteger::class.java, RedisOper.SETNX, 1L)
        setAndGetNumber(Float::class.java, RedisOper.SETNX, 1L)
        setAndGetNumber(Double::class.java, RedisOper.SETNX, 1L)
        setAndGetNumber(BigDecimal::class.java, RedisOper.SETNX, 1L)
    }

    @Execution(ExecutionMode.CONCURRENT)
    @Test
    fun testIncrement() {
        increAndGetNumber(Double::class.java, RedisOper.INCRE)
        increAndGetNumber(Long::class.java, RedisOper.INCRE)
    }

    @Execution(ExecutionMode.CONCURRENT)
    @Test
    fun testIncrementWithInit() {
        increAndGetNumber(Double::class.java, RedisOper.INCRE_WITH_INIT)
        increAndGetNumber(Long::class.java, RedisOper.INCRE_WITH_INIT)
    }

    @Execution(ExecutionMode.CONCURRENT)
    @Test
    fun testBoolean() {
        testRedisObj(value1 = true, value2 = false)
    }

    @Execution(ExecutionMode.CONCURRENT)
    @Test
    fun testString() {
        testRedisObj("hello aloha.", "hello oho.")
    }

    @Execution(ExecutionMode.CONCURRENT)
    @Test
    fun testEnums() {
        testRedisObj<EnumValue<*>>(MyIntEnum.ONE, MyStringEnum.YES)
        testRedisObj(MyIntEnum.ZERO, MyIntEnum.ONE)
        testRedisObj(MyStringEnum.YES, MyStringEnum.NO)
    }

    private fun <T : Number> setAndGetNumber(type: Class<T>, oper: RedisOper, timeout: Long = 0L) {
        val value = getSampleByType(type)
        val key = genTestNumberKey(type, oper)

        logger.info("${type.name} $oper test : start.")
        RedisManager.values.set(key, value, timeout)
        logger.info("${type.name} $oper test : $value.")
        val getValue = RedisManager.values.get<T>(key)
        logger.info("${type.name} $oper test : $getValue was get.")
        RedisManager.delete(key)
        logger.info("${type.name} $oper test : $getValue was delete.")
        logger.info("${type.name} $oper test : end.")
    }

    private fun <T : Number> increAndGetNumber(type: Class<T>, oper: RedisOper) {

        val key = genTestNumberKey(type, oper)
        logger.info("${type.name} $oper test : start.")
        val incrementResult = if (type == Double::class.java) {
            RedisManager.values.increment(key, 2.2, if (oper == RedisOper.INCRE_WITH_INIT) 1.1 else null)
            RedisManager.values.increment(key, 3.3)
        } else {
            RedisManager.values.increment(key, 2L, if (oper == RedisOper.INCRE_WITH_INIT) 1L else null)
            RedisManager.values.increment(key, 3L)
        }
        logger.info("${type.name} $oper test : increment result is $incrementResult.")
        val getValue = RedisManager.values.get<T>(key)
        logger.info("${type.name} $oper test : $getValue was get.")
        RedisManager.delete(key)
        logger.info("${type.name} $oper test : $getValue was delete.")
        logger.info("${type.name} $oper test : end.")
    }

    private fun <T : Number> getSampleByType(type: Class<T>, max: Boolean = true): Number {
        return when (type) {
            Byte::class.java -> if (max) Byte.MAX_VALUE else 1
            Short::class.java -> if (max) Short.MAX_VALUE else 1
            Int::class.java -> if (max) Int.MAX_VALUE else 1
            Long::class.java -> if (max) Long.MAX_VALUE else 1
            BigInteger::class.java -> if (max) BigInteger.TEN else BigInteger.valueOf(1L)

            Float::class.java -> if (max) Float.MAX_VALUE else 1f
            Double::class.java -> if (max) Double.MAX_VALUE else 1.0
            BigDecimal::class.java -> if (max) BigDecimal.TEN else BigDecimal(1)
            else -> 0
        }
    }

    private fun genTestNumberKey(type: Class<*>, oper: RedisOper): String {
        return "test:number:${type.simpleName}:$oper";
    }

    private fun genTestObjKey(type: Class<*>, oper: RedisOper): String {
        return "test:${type.simpleName}:$oper";
    }

    private inline fun <reified T : Any> testRedisObj(value1: T, value2: T) {
        val oper1 = RedisOper.SET
        val key1 = genTestObjKey(T::class.java, oper1)
        val timeout1 = 0L

        logger.info("$key1 : start.")
        RedisManager.values.set(key1, value1, timeout1)
        logger.info("$key1 : $value1.")
        val getValue1 = RedisManager.values.get<T>(key1)
        logger.info("$key1 : $getValue1 was get.")
        logger.info("$key1 : end.")

        val oper2 = RedisOper.SETNX
        val key2 = genTestObjKey(T::class.java, oper2)
        val timeout2 = 1L

        logger.info("$key2 : start.")
        RedisManager.values.set(key2, value2, timeout2)
        logger.info("$key2 : $value2.")
        val getValue2 = RedisManager.values.get<T>(key2)
        logger.info("$key2 : $getValue2 was get.")
        logger.info("$key2 : end.")

        RedisManager.delete(key1, key2)
        logger.info("$key1 : $getValue1 was delete. $key2 : $getValue2 was delete.")
    }

}

enum class RedisOper {
    SET, SETNX, INCRE, INCRE_WITH_INIT;
}
