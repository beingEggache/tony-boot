package com.tony.redis.test

import com.tony.redis.RedisManager
import com.tony.redis.test.model.ObjWithList
import com.tony.redis.test.model.ObjWithMap
import com.tony.redis.test.model.ObjWithNumberTypes
import com.tony.redis.test.model.ObjWithObjList
import com.tony.redis.test.model.ObjWithObjMap
import com.tony.redis.test.model.SimpleObj
import com.tony.utils.toJsonString
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
//        Assertions.assertThrows(ApiException::class.java) {
//            testRedisObj<EnumValue<*>>(MyIntEnum.ONE, MyStringEnum.YES)
//        }
        testRedisObj(MyIntEnum.ZERO, MyIntEnum.ONE)
        testRedisObj(MyStringEnum.YES, MyStringEnum.NO)
    }

    @Execution(ExecutionMode.CONCURRENT)
    @Test
    fun testObj() {
        val simpleObj1 = SimpleObj("a da", 18)
        val simpleObj2 = SimpleObj("a er", 20)
        testRedisObj<SimpleObj>(simpleObj1, simpleObj2)
        testRedisObj<ObjWithNumberTypes>(
            ObjWithNumberTypes(
                Byte.MIN_VALUE,
                Short.MIN_VALUE,
                Int.MIN_VALUE,
                Long.MIN_VALUE,
                BigInteger.ONE,
                Float.MIN_VALUE,
                Double.MIN_VALUE,
                "123.2334554174212354".toBigDecimal(),
            ),
            ObjWithNumberTypes(
                Byte.MAX_VALUE,
                Short.MAX_VALUE,
                Int.MAX_VALUE,
                Long.MAX_VALUE,
                BigInteger.TEN,
                Float.MAX_VALUE,
                Double.MAX_VALUE,
                "123.2334554174212354".toBigDecimal(),
            )
        )
        testRedisObj<ObjWithList>(
            ObjWithList("simple list1", listOf("a", "b", "c")),
            ObjWithList("simple list2", listOf("d", "e", "f")),
        )
        testRedisObj<ObjWithMap>(
            ObjWithMap("simple map1", mapOf("a" to simpleObj1, "b" to simpleObj2)),
            ObjWithMap("simple map2", mapOf("c" to simpleObj1, "d" to simpleObj2)),
        )
        val objList1 = ObjWithObjList("obj list1", listOf(simpleObj1, simpleObj2))
        val objList2 = ObjWithObjList("obj list2", listOf(simpleObj1, simpleObj2))
        testRedisObj(
            objList1,
            objList2,
        )
        testRedisObj(
            ObjWithObjMap("obj map1", mapOf("a" to objList1, "b" to objList2)),
            ObjWithObjMap("obj map2", mapOf("c" to objList1, "d" to objList2)),
        )
    }

    private fun <T : Number> setAndGetNumber(type: Class<T>, oper: RedisOper, timeout: Long = 0L) {
        val value = getSampleByType(type)
        val key = genTestNumberKey(type, oper)

        logger.info("${type.name} $oper test : start.")
        RedisManager.values.set(key, value, timeout)
        logger.info("${type.name} $oper test : $value.")
        val getValue = RedisManager.values.get(key, type)
        logger.info("${type.name} $oper test : $getValue was get.")
        RedisManager.delete(key)
        logger.info("${type.name} $oper test : $getValue was delete.")
        logger.info("${type.name} $oper test : end.")
    }

    private fun <T : Number> increAndGetNumber(type: Class<T>, oper: RedisOper) {

        val key = genTestNumberKey(type, oper)
        logger.info("${type.name} $oper test : start.")
        val incrementResult = if (type == Double::class.java) {
            RedisManager.values.increment(key, 2.20, if (oper == RedisOper.INCRE_WITH_INIT) 1.1 else null)
            RedisManager.values.increment(key, 3.3)
        } else {
            RedisManager.values.increment(key, 2L, if (oper == RedisOper.INCRE_WITH_INIT) 1L else null)
            RedisManager.values.increment(key, 3L)
        }
        logger.info("${type.name} $oper test : increment result is $incrementResult.")
        val getValue = RedisManager.values.get(key, type)
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
        return "test:number:${type.simpleName}:$oper"
    }

    private fun genTestObjKey(type: Class<*>, oper: RedisOper): String {
        return "test:${type.simpleName}:$oper"
    }

    private inline fun <reified T : Any> testRedisObj(value1: T, value2: T) {
        val key1 = genTestObjKey(T::class.java, RedisOper.SET)
        val timeout1 = 0L

        logger.info("$key1 : start.")
        RedisManager.values.set(key1, value1, timeout1)
        logger.info("$key1 : $value1.")
        val getValue1 = RedisManager.values.get<T>(key1)
        logger.info("$key1 : $getValue1 was get.")
        logger.info("$key1 : json: ${getValue1.toJsonString()}.")
        logger.info("$key1 : end.")

        val key2 = genTestObjKey(T::class.java, RedisOper.SETNX)
        val timeout2 = 1L

        logger.info("$key2 : start.")
        RedisManager.values.set(key2, value2, timeout2)
        logger.info("$key2 : $value2.")
        val getValue2 = RedisManager.values.get<T>(key2)
        logger.info("$key2 : $getValue2 was get.")
        logger.info("$key2 : json: ${getValue2.toJsonString()}.")
        logger.info("$key2 : end.")

        RedisManager.delete(key1, key2)
        logger.info(
            "${
                genTestObjKey(
                    T::class.java,
                    RedisOper.DELETE
                )
            } : $getValue1 was delete. $key2 : $getValue2 was delete."
        )
    }

}

enum class RedisOper {
    SET, SETNX, INCRE, INCRE_WITH_INIT, DELETE;
}
