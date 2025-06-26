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

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import tony.utils.*
import java.lang.reflect.Type
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*

/**
 * 类型工具类单元测试
 * @author AI
 * @date 2024/06/09
 * @since 1.0.0
 */
object TypesTest {

    private val logger = LoggerFactory.getLogger(TypesTest::class.java)

    @Test
    @DisplayName("数字类型判断测试")
    fun testNumberTypes() {
        logger.info("测试数字类型判断")

        // 测试数字类型
        val numberTypes = listOf(
            Long::class.java,
            Int::class.java,
            Double::class.java,
            Float::class.java,
            Byte::class.java,
            Short::class.java,
            BigInteger::class.java,
            BigDecimal::class.java
        )

        numberTypes.forEach { type ->
            val isNumber = type.isNumberTypes()
            logger.info("类型 {} 是否为数字类型: {}", type.simpleName, isNumber)
            assertTrue(isNumber)
        }

        // 测试非数字类型
        val nonNumberTypes = listOf(
            String::class.java,
            Boolean::class.java,
            Date::class.java,
            List::class.java
        )

        nonNumberTypes.forEach { type ->
            val isNumber = type.isNumberTypes()
            logger.info("类型 {} 是否为数字类型: {}", type.simpleName, isNumber)
            assertFalse(isNumber)
        }
    }

    @Test
    @DisplayName("字符串类型判断测试")
    fun testStringLikeTypes() {
        logger.info("测试字符串类型判断")

        // 测试字符串类型
        val stringTypes = listOf(
            String::class.java,
            CharSequence::class.java,
            StringBuilder::class.java,
            StringBuffer::class.java
        )

        stringTypes.forEach { type ->
            val isString = type.isStringLikeType()
            logger.info("类型 {} 是否为字符串类型: {}", type.simpleName, isString)
            assertTrue(isString)
        }

        // 测试非字符串类型
        val nonStringTypes = listOf(
            Int::class.java,
            Boolean::class.java,
            Date::class.java,
            List::class.java
        )

        nonStringTypes.forEach { type ->
            val isString = type.isStringLikeType()
            logger.info("类型 {} 是否为字符串类型: {}", type.simpleName, isString)
            assertFalse(isString)
        }
    }

    @Test
    @DisplayName("数组类型判断测试")
    fun testArrayLikeTypes() {
        logger.info("测试数组类型判断")

        // 测试数组类型
        val arrayTypes = listOf(
            Array<String>::class.java,
            List::class.java,
            Set::class.java,
            Collection::class.java,
            ArrayList::class.java,
            LinkedList::class.java
        )

        arrayTypes.forEach { type ->
            val isArray = type.isArrayLikeType()
            logger.info("类型 {} 是否为数组类型: {}", type.simpleName, isArray)
            assertTrue(isArray)
        }

        // 测试非数组类型
        val nonArrayTypes = listOf(
            String::class.java,
            Int::class.java,
            Boolean::class.java,
            Date::class.java
        )

        nonArrayTypes.forEach { type ->
            val isArray = type.isArrayLikeType()
            logger.info("类型 {} 是否为数组类型: {}", type.simpleName, isArray)
            assertFalse(isArray)
        }
    }

    @Test
    @DisplayName("类型继承关系判断测试")
    fun testTypeInheritance() {
        logger.info("测试类型继承关系判断")

        // 测试直接类型
        assertTrue(String::class.java.isTypeOrSubTypeOf(String::class.java))
        logger.info("String 是 String 的子类型: true")

        // 测试继承关系
        assertTrue(ArrayList::class.java.isTypeOrSubTypeOf(List::class.java))
        logger.info("ArrayList 是 List 的子类型: true")

        assertTrue(LinkedList::class.java.isTypeOrSubTypeOf(List::class.java))
        logger.info("LinkedList 是 List 的子类型: true")

        assertTrue(ArrayList::class.java.isTypeOrSubTypeOf(Collection::class.java))
        logger.info("ArrayList 是 Collection 的子类型: true")

        // 测试无继承关系
        assertFalse(String::class.java.isTypeOrSubTypeOf(Integer::class.java))
        logger.info("String 不是 Integer 的子类型: false")

        assertFalse(Int::class.java.isTypeOrSubTypeOf(String::class.java))
        logger.info("Int 不是 String 的子类型: false")
    }

    @Test
    @DisplayName("多类型判断测试")
    fun testMultipleTypes() {
        logger.info("测试多类型判断")

        val targetType = ArrayList::class.java
        val validTypes = listOf(List::class.java, Collection::class.java, Object::class.java)
        val invalidTypes = listOf(String::class.java, Integer::class.java, Boolean::class.java)

        val isValid = targetType.isTypesOrSubTypesOf(*validTypes.toTypedArray())
        val isInvalid = targetType.isTypesOrSubTypesOf(*invalidTypes.toTypedArray())

        logger.info("ArrayList 是否为 List/Collection/Object 的子类型: {}", isValid)
        logger.info("ArrayList 是否为 String/Integer/Boolean 的子类型: {}", isInvalid)

        assertTrue(isValid)
        assertFalse(isInvalid)
    }

    @Test
    @DisplayName("原始类型获取测试")
    fun testRawClass() {
        logger.info("测试原始类型获取")

        // 测试Class类型
        val stringClass = String::class.java
        val rawClass1 = stringClass.rawClass()
        logger.info("String类的原始类型: {}", rawClass1.simpleName)
        assertEquals(String::class.java, rawClass1)

        // 测试泛型类型（通过反射获取）
        val listType = object : java.lang.reflect.ParameterizedType {
            override fun getRawType(): Type = List::class.java
            override fun getOwnerType(): Type? = null
            override fun getActualTypeArguments(): Array<Type> = arrayOf(String::class.java)
        }

        val rawClass2 = listType.rawClass()
        logger.info("List<String>的原始类型: {}", rawClass2.simpleName)
        assertEquals(List::class.java, rawClass2)
    }

    @Test
    @DisplayName("JavaType转换测试")
    fun testJavaTypeConversion() {
        logger.info("测试JavaType转换")

        val stringType = String::class.java
        val javaType = stringType.toJavaType()

        logger.info("String类型转换为JavaType: {}", javaType)
        assertNotNull(javaType)
        assertEquals(String::class.java, javaType.rawClass)
    }

    @Test
    @DisplayName("集合JavaType构造测试")
    fun testCollectionJavaType() {
        logger.info("测试集合JavaType构造")

        val stringType = String::class.java
        val listJavaType = stringType.toCollectionJavaType(List::class.java)
        val setJavaType = stringType.toCollectionJavaType(Set::class.java)

        logger.info("List<String>的JavaType: {}", listJavaType)
        logger.info("Set<String>的JavaType: {}", setJavaType)

        assertNotNull(listJavaType)
        assertNotNull(setJavaType)
        assertEquals(List::class.java, listJavaType.rawClass)
        assertEquals(Set::class.java, setJavaType.rawClass)
    }

    @Test
    @DisplayName("泛型参数获取测试")
    fun testGenericParameterExtraction() {
        logger.info("测试泛型参数获取")

        // 创建一个带泛型的类
        abstract class TestGenericClass<T> {
            fun getType(): Type = this::class.java.typeParamOfSuperClass()
        }

        // 创建具体实现
        class StringGenericClass : TestGenericClass<String>()

        val stringClass = StringGenericClass::class.java
        val genericType = stringClass.typeParamOfSuperClass()

        logger.info("泛型参数类型: {}", genericType.typeName)
        assertEquals(String::class.java, genericType)
    }

    interface TestGenericInterface<T> {
        fun getType(): Type = this::class.java.typeParamOfSuperInterface(TestGenericInterface::class.java)
    }
    @Test
    @DisplayName("接口泛型参数获取测试")
    fun testInterfaceGenericParameter() {
        logger.info("测试接口泛型参数获取")

        // 创建一个带泛型的接口


        // 创建具体实现
        class IntegerGenericClass : TestGenericInterface<Int>

        val integerClass = IntegerGenericClass::class.java
        val genericType = integerClass.typeParamOfSuperInterface(TestGenericInterface::class.java)

        logger.info("接口泛型参数类型: {}", genericType.typeName)
        assertEquals(Integer::class.java, genericType)
    }

    @Test
    @DisplayName("类型转换测试")
    fun testTypeConversion() {
        logger.info("测试类型转换")

        // 测试安全转换
        val stringValue: Any = "test"
        val convertedString = stringValue.asTo<String>()
        logger.info("Any转String: {}", convertedString)
        assertEquals("test", convertedString)

        // 测试数字转换
        val intValue: Any = 123
        val convertedInt = intValue.asTo<Int>()
        logger.info("Any转Int: {}", convertedInt)
        assertEquals(123, convertedInt)

        // 测试null转换
        val nullValue: Any? = null
        val convertedNull = nullValue.asTo<String>()
        logger.info("null转换: {}", convertedNull)
        assertNull(convertedNull)
    }

    @Test
    @DisplayName("类型转换异常测试")
    fun testTypeConversionException() {
        logger.info("测试类型转换异常")

        val stringValue: Any = "test"

        // 测试不兼容的类型转换
        assertThrows(ClassCastException::class.java) {
            @Suppress("UnusedVariable", "unused")
            val asTo = stringValue.asTo<Int>()
        }
        logger.info("String转Int正确抛出ClassCastException")
    }

    @Test
    @DisplayName("复杂类型判断测试")
    fun testComplexTypeJudgment() {
        logger.info("测试复杂类型判断")

        // 测试嵌套泛型
        val mapType = Map::class.java
        val isMapArray = mapType.isArrayLikeType()
        logger.info("Map是否为数组类型: {}", isMapArray)
        assertFalse(isMapArray) // Map实现了Collection接口

        // 测试原始类型
        val intArrayType = IntArray::class.java
        val isIntArrayArray = intArrayType.isArrayLikeType()
        logger.info("IntArray是否为数组类型: {}", isIntArrayArray)
        assertTrue(isIntArrayArray)

        // 测试包装类型
        val integerType = Integer::class.java
        val isIntegerNumber = integerType.isNumberTypes()
        logger.info("Integer是否为数字类型: {}", isIntegerNumber)
        assertTrue(isIntegerNumber)
    }

    @Test
    @DisplayName("边界情况测试")
    fun testEdgeCases() {
        logger.info("测试边界情况")

        // 测试null类型
        val nullType: Class<*>? = null
        if (nullType != null) {
            val isNumber = nullType.isNumberTypes()
            logger.info("null类型是否为数字类型: {}", isNumber)
        }

        // 测试Object类型
        val objectType = Object::class.java
        val isObjectString = objectType.isStringLikeType()
        val isObjectNumber = objectType.isNumberTypes()
        val isObjectArray = objectType.isArrayLikeType()

        logger.info("Object是否为字符串类型: {}", isObjectString)
        logger.info("Object是否为数字类型: {}", isObjectNumber)
        logger.info("Object是否为数组类型: {}", isObjectArray)

        assertFalse(isObjectString)
        assertFalse(isObjectNumber)
        assertFalse(isObjectArray)
    }
}
