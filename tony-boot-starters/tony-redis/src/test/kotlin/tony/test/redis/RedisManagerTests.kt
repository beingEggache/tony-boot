package tony.test.redis

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull
import tony.redis.RedisManager
import tony.redis.RedisManager.toRedisScript

/**
 * RedisManager 测试
 *
 * @author tony
 * @since 2025-07-01
 */
@DisplayName("RedisManager 高级功能测试")
class RedisManagerTests : BaseRedisTest() {

    @Nested
    @DisplayName("事务操作")
    inner class TransactionTests {
        @Test
        @DisplayName("doInTransaction 正常提交与回滚")
        fun testDoInTransactionCommitAndRollback() {
            val key1 = generateTestKey("tx-commit")
            val key2 = generateTestKey("tx-rollback")
            // 正常提交
            val result = RedisManager.doInTransaction {
                RedisManager.values.set(key1, 100)
                RedisManager.values.set(key2, 200)
            }
            assertEquals(2, result.size)
            assertEquals(100, RedisManager.values.get<Int>(key1))
            assertEquals(200, RedisManager.values.get<Int>(key2))
            // 回滚
            val key3 = generateTestKey("tx-ex")
            assertThrows(Exception::class.java) {
                RedisManager.doInTransaction {
                    RedisManager.values.set(key3, 300)
                    throw RuntimeException("tx error")
                }
            }
            assertNull(RedisManager.values.get<Int>(key3))
        }
    }

    @Nested
    @DisplayName("分布式锁")
    inner class LockTests {
        @Test
        @DisplayName("lockKey 正常加锁与超时")
        fun testLockKeyBasic() {
            val key = generateTestKey("lock-basic")
            val locked = RedisManager.lockKey(key, 2)
            assertTrue(locked)
            // 再次加锁应失败
            val lockedAgain = RedisManager.lockKey(key, 2)
            assertFalse(lockedAgain)
            Thread.sleep(2100)
            // 过期后可再次加锁
            val lockedAfterExpire = RedisManager.lockKey(key, 2)
            assertTrue(lockedAfterExpire)
        }

        @Test
        @DisplayName("lockKey 带自旋等待与超时")
        fun testLockKeySpinWait() {
            val key = generateTestKey("lock-spin")
            assertTrue(RedisManager.lockKey(key, 2))
            val start = System.currentTimeMillis()
            val locked = RedisManager.lockKey(key, 2, 500)
            val duration = System.currentTimeMillis() - start
            assertFalse(locked)
            assertTrue(duration >= 500) // 自旋等待
        }

        @Test
        @DisplayName("lockKey 非法参数异常")
        fun testLockKeyInvalidParam() {
            val key = generateTestKey("lock-invalid")
            assertThrows(Exception::class.java) {
                RedisManager.lockKey(key, 0)
            }
        }
    }

    @Nested
    @DisplayName("脚本执行")
    inner class ScriptTests {
        @Test
        @DisplayName("executeScript 正常执行 Lua 脚本")
        fun testExecuteScript() {
            val key = generateTestKey("script")
            RedisManager.values.set(key, 123)
            val script = "return redis.call('GET', KEYS[1])".toRedisScript<Long>()
            val result = RedisManager.executeScript(script, listOf(key), listOf())
            assertEquals(123L, result)
        }

        @Test
        @DisplayName("toRedisScript 泛型类型推断")
        fun testToRedisScriptType() {
            // 整型返回
            val longScript = "return 42".toRedisScript<Long>()
            val longResult = RedisManager.executeScript(longScript, emptyList(), emptyList())
            assertEquals(42, longResult)

            // 字符串返回
            val strScript = "return 'hello'".toRedisScript<String>()
            val strResult = RedisManager.executeScript(strScript, emptyList(), emptyList())
            assertEquals("hello", strResult)

            // 布尔值返回（Lua true 实际为 1）
            val boolScript = "return 1 == 1".toRedisScript<Boolean>()
            val boolResult = RedisManager.executeScript(boolScript, emptyList(), emptyList())
            assertTrue(boolResult == true)

            // 列表返回
            val listScript = "return {1,2,3}".toRedisScript<List<Int>>()
            val listResult = RedisManager.executeScript(listScript, emptyList(), emptyList())
            assertTrue(listResult == listOf(1,2,3) || listResult == listOf(1L,2L,3L))

            // 带参数脚本
            val key = generateTestKey("script-param")
            RedisManager.values.set(key, "abc")
            val paramScript = "return redis.call('GET', KEYS[1])".toRedisScript<String>()
            val paramResult = RedisManager.executeScript(paramScript, listOf(key), listOf())
            assertEquals("abc", paramResult)

            // 返回 null
            val nullScript = "return nil".toRedisScript<String>()
            val nullResult = RedisManager.executeScript(nullScript, emptyList(), emptyList())
            assertNull(nullResult)

            // 类型不匹配异常
            val mismatchScript = "return 123".toRedisScript<String>()
            assertThrows(Exception::class.java) {
                RedisManager.executeScript(mismatchScript, emptyList(), emptyList())
            }
        }
    }

    @Nested
    @DisplayName("批量删除与边界")
    inner class BatchDeleteTests {
        @Test
        @DisplayName("deleteByKeyPatterns 正常批量删除")
        fun testDeleteByKeyPatterns() {
            val keys = generateTestKeys(3, "del-batch")
            keys.forEach { RedisManager.values.set(it, 1) }
            val delCount = RedisManager.deleteByKeyPatterns(*keys.toTypedArray())
            assertEquals(3, delCount)
            keys.forEach { assertNull(RedisManager.values.get<Int>(it)) }
        }

        @Test
        @DisplayName("deleteByKeyPatterns 空参数/异常")
        fun testDeleteByKeyPatternsEdge() {
            assertThrows(Exception::class.java) {
                RedisManager.deleteByKeyPatterns()
            }
            assertThrows(Exception::class.java) {
                RedisManager.deleteByKeyPatterns("")
            }
        }
    }

    @Nested
    @DisplayName("清空数据库 flushdb")
    inner class FlushDbTests {
        @Test
        @DisplayName("flushdb 清空所有数据")
        fun testFlushDb() {
            val key = generateTestKey("flushdb")
            RedisManager.values.set(key, "v")
            assertTrue(RedisManager.hasKey(key))
            RedisManager.flushdb()
            assertFalse(RedisManager.hasKey(key))
        }
    }

    @Nested
    @DisplayName("Key/生命周期/删除基础操作")
    inner class KeyAndExpireTests {
        @Test
        @DisplayName("hasKey/keys 基本用法")
        fun testHasKeyAndKeys() {
            val key1 = generateTestKey("mgr-key1")
            val key2 = generateTestKey("mgr-key2")
            RedisManager.values.set(key1, "v1")
            RedisManager.values.set(key2, "v2")
            assertTrue(RedisManager.hasKey(key1))
            assertTrue(RedisManager.hasKey(key2))
            val keys = RedisManager.keys("mgr-key*")
            assertTrue(keys.any { it == key1 })
            assertTrue(keys.any { it == key2 })
        }

        @Test
        @DisplayName("delete 单个与批量")
        fun testDelete() {
            val key1 = generateTestKey("mgr-del1")
            val key2 = generateTestKey("mgr-del2")
            RedisManager.values.set(key1, 1)
            RedisManager.values.set(key2, 2)
            assertEquals(1, RedisManager.delete(key1))
            assertNull(RedisManager.values.get<Int>(key1))
            assertEquals(1, RedisManager.delete(listOf(key2)))
            assertNull(RedisManager.values.get<Int>(key2))
        }

        @Test
        @DisplayName("expire/expireAt/getExpire 生命周期")
        fun testExpireAndGetExpire() {
            val key = generateTestKey("mgr-expire")
            RedisManager.values.set(key, "v")
            assertTrue(RedisManager.expire(key, 1))
            val ttl = RedisManager.getExpire(key)
            assertTrue(ttl in 0..1)
            Thread.sleep(1100)
            assertFalse(RedisManager.hasKey(key))

            // expireAt
            val key2 = generateTestKey("mgr-expireAt")
            RedisManager.values.set(key2, "v2")
            val expireAt = java.util.Date(System.currentTimeMillis() + 1000)
            assertTrue(RedisManager.expireAt(key2, expireAt))
            val ttl2 = RedisManager.getExpire(key2)
            assertTrue(ttl2 in 0..1)
            Thread.sleep(1100)
            assertFalse(RedisManager.hasKey(key2))
        }

        @Test
        @DisplayName("getExpire 非法key/未设置过期")
        fun testGetExpireEdge() {
            val key = generateTestKey("mgr-expire-edge")
            assertEquals(-2, RedisManager.getExpire(key)) // key不存在
            RedisManager.values.set(key, "v")
            assertEquals(-1, RedisManager.getExpire(key)) // 未设置过期
        }
    }
}
