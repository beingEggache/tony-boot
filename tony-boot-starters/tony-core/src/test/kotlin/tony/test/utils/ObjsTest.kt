package tony.test.utils

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import tony.utils.*

/**
 * Objs工具类单元测试
 */
@DisplayName("Objs测试")
class ObjsTest {
    data class User(val name: String, val age: Int)

    @Nested
    @DisplayName("Objs.asTo()测试")
    inner class AsToTest {
        @ParameterizedTest
        @ValueSource(strings = ["hello", "world", "test"])
        @DisplayName("Objs.asTo():字符串类型转换")
        fun testAsToWithString(value: String) {
            val result: String? = value.asTo()
            assertEquals(value, result)
        }
        @ParameterizedTest
        @ValueSource(ints = [123, 456, 789])
        @DisplayName("Objs.asTo():数字类型转换")
        fun testAsToWithNumber(value: Int) {
            val result: Int? = value.asTo()
            assertEquals(value, result)
        }
        @Test
        @DisplayName("Objs.asTo():类型转换异常")
        fun testAsToWithTypeMismatch() {
            val str: Any = "hello"
            assertThrows(ClassCastException::class.java) {
                @Suppress("UnusedVariable", "unused")
                val asTo = str.asTo<Int>()
            }
        }
    }

    @Nested
    @DisplayName("Objs.asToDefault()测试")
    inner class AsToDefaultTest {
        @ParameterizedTest
        @ValueSource(strings = ["hello", "world", "test"])
        @DisplayName("Objs.asToDefault():字符串默认值")
        fun testAsToDefaultWithString(value: String) {
            val result: String = value.asToDefault("default")
            assertEquals(value, result)
        }
        @ParameterizedTest
        @ValueSource(ints = [123, 456, 789])
        @DisplayName("Objs.asToDefault():数字默认值")
        fun testAsToDefaultWithNumber(value: Int) {
            val result: Int = value.asToDefault(999)
            assertEquals(value, result)
        }
        @Test
        @DisplayName("Objs.asToDefault():类型转换异常")
        fun testAsToDefaultWithTypeMismatch() {
            val str: Any = "hello"
            assertThrows(ClassCastException::class.java) {
                @Suppress("UnusedVariable", "unused")
                val asToDefault = str.asToDefault(123)
            }
        }
        @Test
        @DisplayName("Objs.asToDefault():null值")
        fun testAsToDefaultWithNull() {
            val nullObj: Any? = null
            val defaultStr: String = nullObj.asToDefault("default")
            assertEquals("default", defaultStr)
        }
    }

    @Nested
    @DisplayName("Objs.asToNotNull()测试")
    inner class AsToNotNullTest {
        @ParameterizedTest
        @ValueSource(strings = ["hello", "world", "test"])
        @DisplayName("Objs.asToNotNull():字符串非空转换")
        fun testAsToNotNullWithString(value: String) {
            val result: String = value.asToNotNull()
            assertEquals(value, result)
        }
        @ParameterizedTest
        @ValueSource(ints = [123, 456, 789])
        @DisplayName("Objs.asToNotNull():数字非空转换")
        fun testAsToNotNullWithNumber(value: Int) {
            val result: Int = value.asToNotNull()
            assertEquals(value, result)
        }
        @Test
        @DisplayName("Objs.asToNotNull():类型转换异常")
        fun testAsToNotNullWithTypeMismatch() {
            assertThrows(ClassCastException::class.java) {
                @Suppress("UnusedVariable", "unused")
                val asToNotNull = (123).asToNotNull<String>()
            }
        }
    }

    @Nested
    @DisplayName("Objs.notBlank()测试")
    inner class NotBlankTest {
        @ParameterizedTest
        @ValueSource(strings = ["abc", "hello", "world"])
        @DisplayName("Objs.notBlank():非空字符串")
        fun testNotBlankWithNonEmptyString(value: String) {
            assertTrue(value.notBlank())
        }
        @ParameterizedTest
        @ValueSource(ints = [123, 456, 789])
        @DisplayName("Objs.notBlank():数字")
        fun testNotBlankWithNumber(value: Int) {
            assertTrue(value.notBlank())
        }
        @Test
        @DisplayName("Objs.notBlank():null值")
        fun testNotBlankWithNull() {
            assertFalse(null.notBlank())
        }
        @ParameterizedTest
        @ValueSource(strings = ["", "   ", "\t", "\n"])
        @DisplayName("Objs.notBlank():空字符串")
        fun testNotBlankWithEmptyString(value: String) {
            assertFalse(value.notBlank())
        }
    }

    @Nested
    @DisplayName("Objs.copyTo()测试")
    inner class CopyToTest {
        @Test
        @DisplayName("Objs.copyTo():Map转对象")
        fun testCopyToFromMap() {
            val user = User("tony", 18)
            val map = mapOf("name" to "tony", "age" to 18)
            val user2 = map.copyTo<User>()
            assertEquals(user, user2)
        }
        @Test
        @DisplayName("Objs.copyTo():Map转Class")
        fun testCopyToFromClass() {
            val user = User("tony", 18)
            val map = mapOf("name" to "tony", "age" to 18)
            val user3 = map.copyTo(User::class.java)
            assertEquals(user, user3)
        }
    }
}
