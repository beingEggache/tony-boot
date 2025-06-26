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

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.TypeFactory
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import tony.utils.*
import java.io.ByteArrayInputStream
import java.io.IOException
import com.fasterxml.jackson.databind.RuntimeJsonMappingException
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonMappingException

/**
 * JSON工具类单元测试
 * @author AI
 * @date 2024/06/09
 * @since 1.0.0
 */
object JsonsTest {

    private val logger = LoggerFactory.getLogger(JsonsTest::class.java)

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

    // ==================== createObjectMapper() 方法测试 ====================

    @Test
    @DisplayName("createObjectMapper()方法测试")
    fun testCreateObjectMapper() {
        logger.info("测试createObjectMapper()方法")
        
        val objectMapper = createObjectMapper()
        
        logger.info("创建的ObjectMapper: {}", objectMapper)
        assertNotNull(objectMapper)
        assertTrue(objectMapper is ObjectMapper)
    }

    // ==================== CharSequence.jsonToObj() 方法测试 ====================

    @Test
    @DisplayName("CharSequence.jsonToObj()方法测试")
    fun testCharSequenceJsonToObj() {
        logger.info("测试CharSequence.jsonToObj()方法")
        
        val jsonString = """{"id": 1, "name": "Tony", "email": "tony@example.com", "age": 30}"""
        
        val user = jsonString.jsonToObj<TestUser>()
        
        logger.info("JSON转对象: '{}' -> {}", jsonString, user)
        assertNotNull(user)
        assertEquals(1, user.id)
        assertEquals("Tony", user.name)
        assertEquals("tony@example.com", user.email)
        assertEquals(30, user.age)
    }

    @Test
    @DisplayName("CharSequence.jsonToObj(Class)方法测试")
    fun testCharSequenceJsonToObjWithClass() {
        logger.info("测试CharSequence.jsonToObj(Class)方法")
        
        val jsonString = """{"id": 2, "name": "Alice", "email": "alice@example.com"}"""
        
        val user = jsonString.jsonToObj(TestUser::class.java)
        
        logger.info("JSON转对象(Class): '{}' -> {}", jsonString, user)
        assertNotNull(user)
        assertEquals(2, user.id)
        assertEquals("Alice", user.name)
        assertEquals("alice@example.com", user.email)
        assertNull(user.age)
    }

    @Test
    @DisplayName("CharSequence.jsonToObj(TypeReference)方法测试")
    fun testCharSequenceJsonToObjWithTypeReference() {
        logger.info("测试CharSequence.jsonToObj(TypeReference)方法")
        
        val jsonString = """{"code": 200, "message": "success", "data": {"id": 3, "name": "Bob", "email": "bob@example.com"}}"""
        
        val typeRef = object : TypeReference<TestResponse<TestUser>>() {}
        val response = jsonString.jsonToObj(typeRef)
        
        logger.info("JSON转对象(TypeReference): '{}' -> {}", jsonString, response)
        assertNotNull(response)
        assertEquals(200, response.code)
        assertEquals("success", response.message)
        assertNotNull(response.data)
        assertEquals(3, response.data!!.id)
        assertEquals("Bob", response.data!!.name)
    }

    @Test
    @DisplayName("CharSequence.jsonToObj(JavaType)方法测试")
    fun testCharSequenceJsonToObjWithJavaType() {
        logger.info("测试CharSequence.jsonToObj(JavaType)方法")
        
        val jsonString = """{"id": 4, "name": "Charlie", "email": "charlie@example.com", "age": 25}"""
        
        val javaType = TypeFactory.defaultInstance().constructType(TestUser::class.java)
        val user = jsonString.jsonToObj<TestUser>(javaType)
        
        logger.info("JSON转对象(JavaType): '{}' -> {}", jsonString, user)
        assertNotNull(user)
        assertEquals(4, user.id)
        assertEquals("Charlie", user.name)
        assertEquals("charlie@example.com", user.email)
        assertEquals(25, user.age)
    }

    // ==================== JsonNode.convertTo() 方法测试 ====================

    @Test
    @DisplayName("JsonNode.convertTo()方法测试")
    fun testJsonNodeConvertTo() {
        logger.info("测试JsonNode.convertTo()方法")
        
        val jsonString = """{"id": 5, "name": "David", "email": "david@example.com"}"""
        val jsonNode = jsonString.jsonNode()
        
        val user = jsonNode.convertTo<TestUser>()
        
        logger.info("JsonNode转对象: {} -> {}", jsonNode, user)
        assertNotNull(user)
        assertEquals(5, user.id)
        assertEquals("David", user.name)
        assertEquals("david@example.com", user.email)
    }

    @Test
    @DisplayName("JsonNode.convertTo(Class)方法测试")
    fun testJsonNodeConvertToWithClass() {
        logger.info("测试JsonNode.convertTo(Class)方法")
        
        val jsonString = """{"id": 6, "name": "Eve", "email": "eve@example.com", "age": 28}"""
        val jsonNode = jsonString.jsonNode()
        
        val user = jsonNode.convertTo(TestUser::class.java)
        
        logger.info("JsonNode转对象(Class): {} -> {}", jsonNode, user)
        assertNotNull(user)
        assertEquals(6, user.id)
        assertEquals("Eve", user.name)
        assertEquals("eve@example.com", user.email)
        assertEquals(28, user.age)
    }

    @Test
    @DisplayName("JsonNode.convertTo(TypeReference)方法测试")
    fun testJsonNodeConvertToWithTypeReference() {
        logger.info("测试JsonNode.convertTo(TypeReference)方法")
        
        val jsonString = """{"code": 200, "message": "success", "data": {"id": 7, "name": "Frank", "email": "frank@example.com"}}"""
        val jsonNode = jsonString.jsonNode()
        
        val typeRef = object : TypeReference<TestResponse<TestUser>>() {}
        val response = jsonNode.convertTo(typeRef)
        
        logger.info("JsonNode转对象(TypeReference): {} -> {}", jsonNode, response)
        assertNotNull(response)
        assertEquals(200, response.code)
        assertEquals("success", response.message)
        assertNotNull(response.data)
        assertEquals(7, response.data!!.id)
        assertEquals("Frank", response.data!!.name)
    }

    @Test
    @DisplayName("JsonNode.convertTo(JavaType)方法测试")
    fun testJsonNodeConvertToWithJavaType() {
        logger.info("测试JsonNode.convertTo(JavaType)方法")
        
        val jsonString = """{"id": 8, "name": "Grace", "email": "grace@example.com", "age": 32}"""
        val jsonNode = jsonString.jsonNode()
        
        val javaType = TypeFactory.defaultInstance().constructType(TestUser::class.java)
        val user = jsonNode.convertTo<TestUser>(javaType)
        
        logger.info("JsonNode转对象(JavaType): {} -> {}", jsonNode, user)
        assertNotNull(user)
        assertEquals(8, user.id)
        assertEquals("Grace", user.name)
        assertEquals("grace@example.com", user.email)
        assertEquals(32, user.age)
    }

    // ==================== CharSequence.jsonNode() 方法测试 ====================

    @Test
    @DisplayName("CharSequence.jsonNode()方法测试")
    fun testCharSequenceJsonNode() {
        logger.info("测试CharSequence.jsonNode()方法")
        
        val jsonString = """{"id": 9, "name": "Henry", "email": "henry@example.com", "age": 35}"""
        
        val jsonNode = jsonString.jsonNode()
        
        logger.info("JSON转JsonNode: '{}' -> {}", jsonString, jsonNode)
        assertNotNull(jsonNode)
        assertTrue(jsonNode.has("id"))
        assertTrue(jsonNode.has("name"))
        assertTrue(jsonNode.has("email"))
        assertTrue(jsonNode.has("age"))
        assertEquals(9, jsonNode.get("id").asInt())
        assertEquals("Henry", jsonNode.get("name").asText())
        assertEquals("henry@example.com", jsonNode.get("email").asText())
        assertEquals(35, jsonNode.get("age").asInt())
    }

    // ==================== ByteArray.jsonNode() 方法测试 ====================

    @Test
    @DisplayName("ByteArray.jsonNode()方法测试")
    fun testByteArrayJsonNode() {
        logger.info("测试ByteArray.jsonNode()方法")
        
        val jsonString = """{"id": 10, "name": "Ivy", "email": "ivy@example.com", "age": 27}"""
        val jsonBytes = jsonString.toByteArray()
        
        val jsonNode = jsonBytes.jsonNode()
        
        logger.info("字节数组转JsonNode: {} -> {}", jsonBytes.contentToString(), jsonNode)
        assertNotNull(jsonNode)
        assertTrue(jsonNode.has("id"))
        assertTrue(jsonNode.has("name"))
        assertTrue(jsonNode.has("email"))
        assertTrue(jsonNode.has("age"))
        assertEquals(10, jsonNode.get("id").asInt())
        assertEquals("Ivy", jsonNode.get("name").asText())
        assertEquals("ivy@example.com", jsonNode.get("email").asText())
        assertEquals(27, jsonNode.get("age").asInt())
    }

    // ==================== InputStream.jsonNode() 方法测试 ====================

    @Test
    @DisplayName("InputStream.jsonNode()方法测试")
    fun testInputStreamJsonNode() {
        logger.info("测试InputStream.jsonNode()方法")
        
        val jsonString = """{"id": 11, "name": "Jack", "email": "jack@example.com", "age": 29}"""
        val inputStream = ByteArrayInputStream(jsonString.toByteArray())
        
        val jsonNode = inputStream.jsonNode()
        
        logger.info("输入流转JsonNode: {} -> {}", jsonString, jsonNode)
        assertNotNull(jsonNode)
        assertTrue(jsonNode.has("id"))
        assertTrue(jsonNode.has("name"))
        assertTrue(jsonNode.has("email"))
        assertTrue(jsonNode.has("age"))
        assertEquals(11, jsonNode.get("id").asInt())
        assertEquals("Jack", jsonNode.get("name").asText())
        assertEquals("jack@example.com", jsonNode.get("email").asText())
        assertEquals(29, jsonNode.get("age").asInt())
    }

    // ==================== T.toJsonString() 方法测试 ====================

    @Test
    @DisplayName("T.toJsonString()方法测试")
    fun testToJsonString() {
        logger.info("测试T.toJsonString()方法")
        
        val user = TestUser(12, "Kate", "kate@example.com", 31)
        
        val jsonString = user.toJsonString()
        
        logger.info("对象转JSON: {} -> '{}'", user, jsonString)
        assertNotNull(jsonString)
        assertTrue(jsonString.isNotEmpty())
        assertTrue(jsonString.contains("\"id\":12"))
        assertTrue(jsonString.contains("\"name\":\"Kate\""))
        assertTrue(jsonString.contains("\"email\":\"kate@example.com\""))
        assertTrue(jsonString.contains("\"age\":31"))
    }

    @Test
    @DisplayName("T.toJsonString()null值测试")
    fun testToJsonStringWithNull() {
        logger.info("测试T.toJsonString()null值")
        
        val nullUser: TestUser? = null
        
        val jsonString = nullUser.toJsonString()
        
        logger.info("null对象转JSON: {} -> '{}'", nullUser, jsonString)
        assertEquals("", jsonString)
    }

    // ==================== CharSequence.getFromRootAsString() 方法测试 ====================

    @Test
    @DisplayName("CharSequence.getFromRootAsString()方法测试")
    fun testGetFromRootAsString() {
        logger.info("测试CharSequence.getFromRootAsString()方法")
        
        val jsonString = """{"id": 13, "name": "Liam", "email": "liam@example.com", "age": 33}"""
        
        val id = jsonString.getFromRootAsString("id")
        val name = jsonString.getFromRootAsString("name")
        val email = jsonString.getFromRootAsString("email")
        val age = jsonString.getFromRootAsString("age")
        val notExist = jsonString.getFromRootAsString("notExist")
        
        logger.info("从根节点获取字段值: id='{}', name='{}', email='{}', age='{}', notExist='{}'", id, name, email, age, notExist)
        assertEquals("13", id)
        assertEquals("Liam", name)
        assertEquals("liam@example.com", email)
        assertEquals("33", age)
        assertNull(notExist)
    }

    @Test
    @DisplayName("CharSequence.getFromRootAsString()嵌套对象测试")
    fun testGetFromRootAsStringNestedObject() {
        logger.info("测试CharSequence.getFromRootAsString()嵌套对象")
        
        val jsonString = """{"user": {"id": 14, "name": "Mia"}, "status": "active"}"""
        
        val user = jsonString.getFromRootAsString("user")
        val status = jsonString.getFromRootAsString("status")
        
        logger.info("从根节点获取嵌套字段值: user='{}', status='{}'", user, status)
        assertNull(user) // 嵌套对象不是简单值，应该返回null
        assertEquals("active", status)
    }

    // ==================== ByteArray.jsonToObj() 方法测试 ====================

    @Test
    @DisplayName("ByteArray.jsonToObj()方法测试")
    fun testByteArrayJsonToObj() {
        logger.info("测试ByteArray.jsonToObj()方法")
        
        val user = TestUser(15, "Noah", "noah@example.com", 26)
        val jsonString = user.toJsonString()
        val jsonBytes = jsonString.toByteArray()
        
        val decodedUser = jsonBytes.jsonToObj<TestUser>()
        
        logger.info("字节数组转对象: {} -> {}", jsonBytes.contentToString(), decodedUser)
        assertNotNull(decodedUser)
        assertEquals(user.id, decodedUser.id)
        assertEquals(user.name, decodedUser.name)
        assertEquals(user.email, decodedUser.email)
        assertEquals(user.age, decodedUser.age)
    }

    @Test
    @DisplayName("ByteArray.jsonToObj(Class)方法测试")
    fun testByteArrayJsonToObjWithClass() {
        logger.info("测试ByteArray.jsonToObj(Class)方法")
        
        val user = TestUser(16, "Olivia", "olivia@example.com", 34)
        val jsonString = user.toJsonString()
        val jsonBytes = jsonString.toByteArray()
        
        val decodedUser = jsonBytes.jsonToObj(TestUser::class.java)
        
        logger.info("字节数组转对象(Class): {} -> {}", jsonBytes.contentToString(), decodedUser)
        assertNotNull(decodedUser)
        assertEquals(user.id, decodedUser.id)
        assertEquals(user.name, decodedUser.name)
        assertEquals(user.email, decodedUser.email)
        assertEquals(user.age, decodedUser.age)
    }

    @Test
    @DisplayName("ByteArray.jsonToObj(TypeReference)方法测试")
    fun testByteArrayJsonToObjWithTypeReference() {
        logger.info("测试ByteArray.jsonToObj(TypeReference)方法")
        
        val response = TestResponse(200, "success", TestUser(17, "Peter", "peter@example.com", 30))
        val jsonString = response.toJsonString()
        val jsonBytes = jsonString.toByteArray()
        
        val typeRef = object : TypeReference<TestResponse<TestUser>>() {}
        val decodedResponse = jsonBytes.jsonToObj(typeRef)
        
        logger.info("字节数组转对象(TypeReference): {} -> {}", jsonBytes.contentToString(), decodedResponse)
        assertNotNull(decodedResponse)
        assertEquals(response.code, decodedResponse.code)
        assertEquals(response.message, decodedResponse.message)
        assertNotNull(decodedResponse.data)
        assertEquals(response.data!!.id, decodedResponse.data!!.id)
        assertEquals(response.data!!.name, decodedResponse.data!!.name)
    }

    @Test
    @DisplayName("ByteArray.jsonToObj(JavaType)方法测试")
    fun testByteArrayJsonToObjWithJavaType() {
        logger.info("测试ByteArray.jsonToObj(JavaType)方法")
        
        val user = TestUser(18, "Quinn", "quinn@example.com", 28)
        val jsonString = user.toJsonString()
        val jsonBytes = jsonString.toByteArray()
        
        val javaType = TypeFactory.defaultInstance().constructType(TestUser::class.java)
        val decodedUser = jsonBytes.jsonToObj<TestUser>(javaType)
        
        logger.info("字节数组转对象(JavaType): {} -> {}", jsonBytes.contentToString(), decodedUser)
        assertNotNull(decodedUser)
        assertEquals(user.id, decodedUser.id)
        assertEquals(user.name, decodedUser.name)
        assertEquals(user.email, decodedUser.email)
        assertEquals(user.age, decodedUser.age)
    }

    // ==================== InputStream.jsonToObj() 方法测试 ====================

    @Test
    @DisplayName("InputStream.jsonToObj()方法测试")
    fun testInputStreamJsonToObj() {
        logger.info("测试InputStream.jsonToObj()方法")
        
        val user = TestUser(19, "Ruby", "ruby@example.com", 25)
        val jsonString = user.toJsonString()
        val inputStream = ByteArrayInputStream(jsonString.toByteArray())
        
        val decodedUser = inputStream.jsonToObj<TestUser>()
        
        logger.info("输入流转对象: {} -> {}", jsonString, decodedUser)
        assertNotNull(decodedUser)
        assertEquals(user.id, decodedUser.id)
        assertEquals(user.name, decodedUser.name)
        assertEquals(user.email, decodedUser.email)
        assertEquals(user.age, decodedUser.age)
    }

    @Test
    @DisplayName("InputStream.jsonToObj(Class)方法测试")
    fun testInputStreamJsonToObjWithClass() {
        logger.info("测试InputStream.jsonToObj(Class)方法")
        
        val user = TestUser(20, "Sam", "sam@example.com", 36)
        val jsonString = user.toJsonString()
        val inputStream = ByteArrayInputStream(jsonString.toByteArray())
        
        val decodedUser = inputStream.jsonToObj(TestUser::class.java)
        
        logger.info("输入流转对象(Class): {} -> {}", jsonString, decodedUser)
        assertNotNull(decodedUser)
        assertEquals(user.id, decodedUser.id)
        assertEquals(user.name, decodedUser.name)
        assertEquals(user.email, decodedUser.email)
        assertEquals(user.age, decodedUser.age)
    }

    @Test
    @DisplayName("InputStream.jsonToObj(TypeReference)方法测试")
    fun testInputStreamJsonToObjWithTypeReference() {
        logger.info("测试InputStream.jsonToObj(TypeReference)方法")
        
        val response = TestResponse(200, "success", TestUser(21, "Tina", "tina@example.com", 29))
        val jsonString = response.toJsonString()
        val inputStream = ByteArrayInputStream(jsonString.toByteArray())
        
        val typeRef = object : TypeReference<TestResponse<TestUser>>() {}
        val decodedResponse = inputStream.jsonToObj(typeRef)
        
        logger.info("输入流转对象(TypeReference): {} -> {}", jsonString, decodedResponse)
        assertNotNull(decodedResponse)
        assertEquals(response.code, decodedResponse.code)
        assertEquals(response.message, decodedResponse.message)
        assertNotNull(decodedResponse.data)
        assertEquals(response.data!!.id, decodedResponse.data!!.id)
        assertEquals(response.data!!.name, decodedResponse.data!!.name)
    }

    @Test
    @DisplayName("InputStream.jsonToObj(JavaType)方法测试")
    fun testInputStreamJsonToObjWithJavaType() {
        logger.info("测试InputStream.jsonToObj(JavaType)方法")
        
        val user = TestUser(22, "Uma", "uma@example.com", 31)
        val jsonString = user.toJsonString()
        val inputStream = ByteArrayInputStream(jsonString.toByteArray())
        
        val javaType = TypeFactory.defaultInstance().constructType(TestUser::class.java)
        val decodedUser = inputStream.jsonToObj<TestUser>(javaType)
        
        logger.info("输入流转对象(JavaType): {} -> {}", jsonString, decodedUser)
        assertNotNull(decodedUser)
        assertEquals(user.id, decodedUser.id)
        assertEquals(user.name, decodedUser.name)
        assertEquals(user.email, decodedUser.email)
        assertEquals(user.age, decodedUser.age)
    }

    // ==================== 边界情况测试 ====================

    @Test
    @DisplayName("边界情况测试")
    fun testBoundaryCases() {
        logger.info("测试边界情况")
        
        // 测试空JSON对象 - 由于TestUser的name字段是非空类型，应该抛出异常
        val emptyJson = "{}"
        val ex1 = assertThrows(Throwable::class.java) {
            emptyJson.jsonToObj<TestUser>()
        }
        logger.info("空JSON转对象正确抛出异常: '{}'，异常类型: {}", emptyJson, ex1::class.qualifiedName)
        
        // 测试空JSON数组
        val emptyArrayJson = "[]"
        val emptyList = emptyArrayJson.jsonToObj<List<TestUser>>()
        logger.info("空JSON数组转对象: '{}' -> {}", emptyArrayJson, emptyList)
        assertNotNull(emptyList)
        assertTrue(emptyList.isEmpty())
        
        // 测试null值
        val nullJson = "null"
        val ex2 = assertThrows(Throwable::class.java) {
            nullJson.jsonToObj<TestUser>()
        }
        logger.info("null JSON正确抛出异常，异常类型: {}", ex2::class.qualifiedName)
        
        // 测试空字符串
        val emptyString = ""
        val ex3 = assertThrows(Throwable::class.java) {
            emptyString.jsonToObj<TestUser>()
        }
        logger.info("空字符串正确抛出异常，异常类型: {}", ex3::class.qualifiedName)
        
        // 测试只有空格
        val whitespaceString = "   "
        val ex4 = assertThrows(Throwable::class.java) {
            whitespaceString.jsonToObj<TestUser>()
        }
        logger.info("空白字符串正确抛出异常，异常类型: {}", ex4::class.qualifiedName)
    }

    // ==================== 异常情况测试 ====================

    @Test
    @DisplayName("异常情况测试")
    fun testExceptionCases() {
        logger.info("测试异常情况")
        
        // 测试无效JSON
        val invalidJson = "{invalid json}"
        assertThrows(IOException::class.java) {
            invalidJson.jsonToObj<TestUser>()
        }
        logger.info("无效JSON正确抛出异常")
        
        // 测试类型不匹配
        val typeMismatchJson = """{"id": "not_a_number", "name": "Test", "email": "test@example.com"}"""
        assertThrows(IOException::class.java) {
            typeMismatchJson.jsonToObj<TestUser>()
        }
        logger.info("类型不匹配正确抛出异常")
        
        // 测试缺少必需字段
        val missingFieldJson = """{"name": "Test", "email": "test@example.com"}"""
        val userWithMissingField = missingFieldJson.jsonToObj<TestUser>()
        logger.info("缺少字段JSON转对象: '{}' -> {}", missingFieldJson, userWithMissingField)
        assertNotNull(userWithMissingField)
        assertEquals(0, userWithMissingField.id) // 默认值
        assertEquals("Test", userWithMissingField.name)
        assertEquals("test@example.com", userWithMissingField.email)
    }

    // ==================== 性能测试 ====================

    @Test
    @DisplayName("性能测试")
    fun testPerformance() {
        logger.info("测试JSON工具类性能")
        
        val iterations = 1000
        val user = TestUser(999, "PerformanceTest", "perf@example.com", 99)
        val jsonString = user.toJsonString()
        
        // 测试序列化性能
        val serializeStartTime = System.currentTimeMillis()
        repeat(iterations) {
            user.toJsonString()
        }
        val serializeEndTime = System.currentTimeMillis()
        val serializeDuration = serializeEndTime - serializeStartTime
        
        logger.info("序列化性能测试: {}次迭代耗时 {}ms", iterations, serializeDuration)
        assertTrue(serializeDuration < 2000) // 应该在2秒内完成
        
        // 测试反序列化性能
        val deserializeStartTime = System.currentTimeMillis()
        repeat(iterations) {
            jsonString.jsonToObj<TestUser>()
        }
        val deserializeEndTime = System.currentTimeMillis()
        val deserializeDuration = deserializeEndTime - deserializeStartTime
        
        logger.info("反序列化性能测试: {}次迭代耗时 {}ms", iterations, deserializeDuration)
        assertTrue(deserializeDuration < 2000) // 应该在2秒内完成
    }
} 