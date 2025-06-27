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

package tony.test.utils

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.TypeFactory
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import tony.utils.*
import java.io.ByteArrayInputStream
import java.io.IOException
import com.fasterxml.jackson.databind.RuntimeJsonMappingException
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonMappingException
import kotlin.jvm.java

/**
 * JSON工具类单元测试
 * @author AI
 * @date 2024/06/09
 * @since 1.0.0
 */
@DisplayName("Jsons测试")
class JsonsTest {

    // 测试数据类
    data class TestUser(
        val id: Int,
        val name: String,
        val email: String,
        val age: Int? = null
    )

    data class TestResponse<T>(
        val code: Int,
        val message: String,
        val data: T?
    )

    @Nested
    @DisplayName("Jsons.createObjectMapper()测试")
    inner class CreateObjectMapperTest {
        @Test
        @DisplayName("Jsons.createObjectMapper():创建ObjectMapper")
        fun testCreateObjectMapper() {
            val objectMapper = createObjectMapper()
            assertNotNull(objectMapper)
            assertTrue(objectMapper is ObjectMapper)
        }
    }

    @Nested
    @DisplayName("Jsons.CharSequence.jsonToObj()测试")
    inner class CharSequenceJsonToObjTest {
        @ParameterizedTest
        @ValueSource(strings = [
            """{"id": 1, "name": "Tony", "email": "tony@example.com", "age": 30}""",
            """{"id": 2, "name": "Alice", "email": "alice@example.com", "age": 25}""",
            """{"id": 3, "name": "Bob", "email": "bob@example.com"}"""
        ])
        @DisplayName("Jsons.CharSequence.jsonToObj():泛型转换")
        fun testCharSequenceJsonToObj(value: String) {
            val user = value.jsonToObj<TestUser>()
            assertNotNull(user)
            assertTrue(user.id > 0)
            assertTrue(user.name.isNotEmpty())
            assertTrue(user.email.contains("@"))
        }

        @Test
        @DisplayName("Jsons.CharSequence.jsonToObj():Class转换")
        fun testCharSequenceJsonToObjWithClass() {
            val jsonString = """{"id": 2, "name": "Alice", "email": "alice@example.com"}"""
            val user = jsonString.jsonToObj(TestUser::class.java)
            assertNotNull(user)
            assertEquals(2, user.id)
            assertEquals("Alice", user.name)
            assertEquals("alice@example.com", user.email)
            assertNull(user.age)
        }

        @Test
        @DisplayName("Jsons.CharSequence.jsonToObj():TypeReference转换")
        fun testCharSequenceJsonToObjWithTypeReference() {
            val jsonString = """{"code": 200, "message": "success", "data": {"id": 3, "name": "Bob", "email": "bob@example.com"}}"""
            val typeRef = object : TypeReference<TestResponse<TestUser>>() {}
            val response = jsonString.jsonToObj(typeRef)
            assertNotNull(response)
            assertEquals(200, response.code)
            assertEquals("success", response.message)
            assertNotNull(response.data)
            assertEquals(3, response.data!!.id)
            assertEquals("Bob", response.data!!.name)
        }

        @Test
        @DisplayName("Jsons.CharSequence.jsonToObj():JavaType转换")
        fun testCharSequenceJsonToObjWithJavaType() {
            val jsonString = """{"id": 4, "name": "Charlie", "email": "charlie@example.com", "age": 25}"""
            val javaType = TypeFactory.defaultInstance().constructType(TestUser::class.java)
            val user = jsonString.jsonToObj<TestUser>(javaType)
            assertNotNull(user)
            assertEquals(4, user.id)
            assertEquals("Charlie", user.name)
            assertEquals("charlie@example.com", user.email)
            assertEquals(25, user.age)
        }

        @Test
        @DisplayName("Jsons.CharSequence.jsonToObj():无效JSON")
        fun testCharSequenceJsonToObjWithInvalidJson() {
            val invalidJson = """{"id": 1, "name": "Tony", "email": "tony@example.com""" // 缺少闭合括号
            assertThrows(JsonProcessingException::class.java) {
                invalidJson.jsonToObj<TestUser>()
            }
        }
    }

    @Nested
    @DisplayName("Jsons.JsonNode.convertTo()测试")
    inner class JsonNodeConvertToTest {
        @Test
        @DisplayName("Jsons.JsonNode.convertTo():泛型转换")
        fun testJsonNodeConvertTo() {
            val jsonString = """{"id": 5, "name": "David", "email": "david@example.com"}"""
            val jsonNode = jsonString.jsonNode()
            val user = jsonNode.convertTo<TestUser>()
            assertNotNull(user)
            assertEquals(5, user.id)
            assertEquals("David", user.name)
            assertEquals("david@example.com", user.email)
        }

        @Test
        @DisplayName("Jsons.JsonNode.convertTo():Class转换")
        fun testJsonNodeConvertToWithClass() {
            val jsonString = """{"id": 6, "name": "Eve", "email": "eve@example.com", "age": 28}"""
            val jsonNode = jsonString.jsonNode()
            val user = jsonNode.convertTo(TestUser::class.java)
            assertNotNull(user)
            assertEquals(6, user.id)
            assertEquals("Eve", user.name)
            assertEquals("eve@example.com", user.email)
            assertEquals(28, user.age)
        }

        @Test
        @DisplayName("Jsons.JsonNode.convertTo():TypeReference转换")
        fun testJsonNodeConvertToWithTypeReference() {
            val jsonString = """{"code": 200, "message": "success", "data": {"id": 7, "name": "Frank", "email": "frank@example.com"}}"""
            val jsonNode = jsonString.jsonNode()
            val typeRef = object : TypeReference<TestResponse<TestUser>>() {}
            val response = jsonNode.convertTo(typeRef)
            assertNotNull(response)
            assertEquals(200, response.code)
            assertEquals("success", response.message)
            assertNotNull(response.data)
            assertEquals(7, response.data!!.id)
            assertEquals("Frank", response.data!!.name)
        }
    }

    @Nested
    @DisplayName("Jsons.CharSequence.jsonNode()测试")
    inner class CharSequenceJsonNodeTest {
        @ParameterizedTest
        @ValueSource(strings = [
            """{"id": 1, "name": "Tony"}""",
            """{"code": 200, "message": "success"}""",
            """{"data": [1, 2, 3]}"""
        ])
        @DisplayName("Jsons.CharSequence.jsonNode():有效JSON")
        fun testCharSequenceJsonNodeWithValidJson(value: String) {
            val jsonNode = value.jsonNode()
            assertNotNull(jsonNode)
            assertFalse(jsonNode.isEmpty)
        }

        @Test
        @DisplayName("Jsons.CharSequence.jsonNode():无效JSON")
        fun testCharSequenceJsonNodeWithInvalidJson() {
            val invalidJson = """{"id": 1, "name": "Tony""" // 缺少闭合括号
            assertThrows(JsonParseException::class.java) {
                invalidJson.jsonNode()
            }
        }
    }

    @Nested
    @DisplayName("Jsons.Any.toJsonString()测试")
    inner class AnyToJsonStringTest {
        @Test
        @DisplayName("Jsons.Any.toJsonString():对象转JSON字符串")
        fun testAnyToJsonString() {
            val user = TestUser(1, "Tony", "tony@example.com", 30)
            val jsonString = user.toJsonString()
            assertNotNull(jsonString)
            assertTrue(jsonString.contains("\"id\":1"))
            assertTrue(jsonString.contains("\"name\":\"Tony\""))
            assertTrue(jsonString.contains("\"email\":\"tony@example.com\""))
            assertTrue(jsonString.contains("\"age\":30"))
        }

        @Test
        @DisplayName("Jsons.Any.toJsonString():null值")
        fun testAnyToJsonStringWithNull() {
            val user = TestUser(1, "Tony", "tony@example.com", null)
            val jsonString = user.toJsonString()
            assertNotNull(jsonString)
            assertTrue(jsonString.contains("\"age\":null"))
        }

        @Test
        @DisplayName("Jsons.Any.toJsonString():复杂对象")
        fun testAnyToJsonStringWithComplexObject() {
            val response = TestResponse(200, "success", TestUser(1, "Tony", "tony@example.com"))
            val jsonString = response.toJsonString()
            assertNotNull(jsonString)
            assertTrue(jsonString.contains("\"code\":200"))
            assertTrue(jsonString.contains("\"message\":\"success\""))
            assertTrue(jsonString.contains("\"data\""))
        }

        @Test
        @DisplayName("Jsons.Any.toJsonString():null对象")
        fun testAnyToJsonStringWithNullObject() {
            val nullUser: TestUser? = null
            val jsonString = nullUser.toJsonString()
            assertEquals("", jsonString)
        }
    }

    @Nested
    @DisplayName("Jsons.InputStream.jsonToObj()测试")
    inner class InputStreamJsonToObjTest {
        @Test
        @DisplayName("Jsons.InputStream.jsonToObj():流转对象")
        fun testInputStreamJsonToObj() {
            val jsonString = """{"id": 1, "name": "Tony", "email": "tony@example.com"}"""
            val inputStream = ByteArrayInputStream(jsonString.toByteArray())
            val user = inputStream.jsonToObj<TestUser>()
            assertNotNull(user)
            assertEquals(1, user.id)
            assertEquals("Tony", user.name)
            assertEquals("tony@example.com", user.email)
        }

        @Test
        @DisplayName("Jsons.InputStream.jsonToObj():Class转换")
        fun testInputStreamJsonToObjWithClass() {
            val jsonString = """{"id": 2, "name": "Alice", "email": "alice@example.com"}"""
            val inputStream = ByteArrayInputStream(jsonString.toByteArray())
            val user = inputStream.jsonToObj(TestUser::class.java)
            assertNotNull(user)
            assertEquals(2, user.id)
            assertEquals("Alice", user.name)
        }

        @Test
        @DisplayName("Jsons.InputStream.jsonToObj():TypeReference转换")
        fun testInputStreamJsonToObjWithTypeReference() {
            val jsonString = """{"code": 200, "message": "success", "data": {"id": 3, "name": "Bob", "email": "bob@example.com"}}"""
            val inputStream = ByteArrayInputStream(jsonString.toByteArray())
            val typeRef = object : TypeReference<TestResponse<TestUser>>() {}
            val response = inputStream.jsonToObj(typeRef)
            assertNotNull(response)
            assertEquals(200, response.code)
            assertEquals("success", response.message)
        }
    }

    @Nested
    @DisplayName("Jsons.InputStream.jsonNode()测试")
    inner class InputStreamJsonNodeTest {
        @Test
        @DisplayName("Jsons.InputStream.jsonNode():流转JsonNode")
        fun testInputStreamJsonNode() {
            val jsonString = """{"id": 1, "name": "Tony"}"""
            val inputStream = ByteArrayInputStream(jsonString.toByteArray())
            val jsonNode = inputStream.jsonNode()
            assertNotNull(jsonNode)
            assertFalse(jsonNode.isEmpty)
            assertEquals(1, jsonNode.get("id").asInt())
            assertEquals("Tony", jsonNode.get("name").asText())
        }

        @Test
        @DisplayName("Jsons.InputStream.jsonNode():无效JSON")
        fun testInputStreamJsonNodeWithInvalidJson() {
            val invalidJson = """{"id": 1, "name": "Tony""" // 缺少闭合括号
            val inputStream = ByteArrayInputStream(invalidJson.toByteArray())
            assertThrows(JsonProcessingException::class.java) {
                inputStream.jsonNode()
            }
        }
    }

    @Nested
    @DisplayName("Jsons.ByteArray.jsonToObj()测试")
    inner class ByteArrayJsonToObjTest {
        @Test
        @DisplayName("Jsons.ByteArray.jsonToObj():字节数组转对象")
        fun testByteArrayJsonToObj() {
            val jsonString = """{"id": 1, "name": "Tony", "email": "tony@example.com"}"""
            val byteArray = jsonString.toByteArray()
            val user = byteArray.jsonToObj<TestUser>()
            assertNotNull(user)
            assertEquals(1, user.id)
            assertEquals("Tony", user.name)
            assertEquals("tony@example.com", user.email)
        }

        @Test
        @DisplayName("Jsons.ByteArray.jsonToObj():Class转换")
        fun testByteArrayJsonToObjWithClass() {
            val jsonString = """{"id": 2, "name": "Alice", "email": "alice@example.com"}"""
            val byteArray = jsonString.toByteArray()
            val user = byteArray.jsonToObj(TestUser::class.java)
            assertNotNull(user)
            assertEquals(2, user.id)
            assertEquals("Alice", user.name)
        }
    }

    @Nested
    @DisplayName("Jsons.ByteArray.jsonNode()测试")
    inner class ByteArrayJsonNodeTest {
        @Test
        @DisplayName("Jsons.ByteArray.jsonNode():字节数组转JsonNode")
        fun testByteArrayJsonNode() {
            val jsonString = """{"id": 1, "name": "Tony"}"""
            val byteArray = jsonString.toByteArray()
            val jsonNode = byteArray.jsonNode()
            assertNotNull(jsonNode)
            assertFalse(jsonNode.isEmpty)
            assertEquals(1, jsonNode.get("id").asInt())
            assertEquals("Tony", jsonNode.get("name").asText())
        }
    }

    @Nested
    @DisplayName("Jsons.CharSequence.getFromRootAsString()测试")
    inner class CharSequenceGetFromRootAsStringTest {
        @Test
        @DisplayName("Jsons.CharSequence.getFromRootAsString():获取根节点字段")
        fun testCharSequenceGetFromRootAsString() {
            val jsonString = """{"id": 1, "name": "Tony", "email": "tony@example.com"}"""
            val id = jsonString.getFromRootAsString("id")
            val name = jsonString.getFromRootAsString("name")
            val email = jsonString.getFromRootAsString("email")

            assertEquals("1", id)
            assertEquals("Tony", name)
            assertEquals("tony@example.com", email)
        }

        @Test
        @DisplayName("Jsons.CharSequence.getFromRootAsString():不存在的字段")
        fun testCharSequenceGetFromRootAsStringWithNonExistentField() {
            val jsonString = """{"id": 1, "name": "Tony"}"""
            val result = jsonString.getFromRootAsString("nonexistent")
            assertNull(result)
        }

        @Test
        @DisplayName("Jsons.CharSequence.getFromRootAsString():无效JSON")
        fun testCharSequenceGetFromRootAsStringWithInvalidJson() {
            val invalidJson = """{"id": 1, "name": "Tony""" // 缺少闭合括号
            val result = invalidJson.getFromRootAsString("id")
            assertEquals("1", result)
        }
    }

    @Nested
    @DisplayName("Jsons.边界情况测试")
    inner class EdgeCasesTest {
        @Test
        @DisplayName("Jsons.空字符串JSON")
        fun testEmptyStringJson() {
            assertThrows(JsonProcessingException::class.java) {
                "".jsonToObj<TestUser>()
            }
        }

        @Test
        @DisplayName("Jsons.空白字符串JSON")
        fun testBlankStringJson() {
            assertThrows(JsonProcessingException::class.java) {
                "   ".jsonToObj<TestUser>()
            }
        }
    }
}
