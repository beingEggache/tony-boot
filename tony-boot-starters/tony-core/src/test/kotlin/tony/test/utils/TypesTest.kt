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
import com.fasterxml.jackson.databind.type.TypeFactory
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import tony.utils.*
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDateTime
import java.util.*

/**
 * 类型工具类单元测试
 * @author AI
 * @date 2024/06/09
 * @since 1.0.0
 */
@DisplayName("Types测试")
class TypesTest {

    // 测试用的泛型类
    abstract class TestGenericClass<T> {
        fun getType(): Type = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]
    }

    class StringGenericClass : TestGenericClass<String>()

    @Nested
    @DisplayName("Type扩展方法测试")
    inner class TypeExtensionTest {
        @Test
        @DisplayName("Type.rawClass():Class类型")
        fun testRawClassWithClass() {
            val stringClass: Type = String::class.java
            val result = stringClass.rawClass()
            assertEquals(String::class.java, result)
        }

        @Test
        @DisplayName("Type.rawClass():ParameterizedType类型")
        fun testRawClassWithParameterizedType() {
            val genericClass = StringGenericClass()
            val type = genericClass.getType()
            val result = type.rawClass()
            assertEquals(String::class.java, result)
        }

        @Test
        @DisplayName("Type.toJavaType():转换JavaType")
        fun testToJavaType() {
            val stringClass: Type = String::class.java
            val javaType = stringClass.toJavaType()
            assertNotNull(javaType)
            assertEquals(String::class.java, javaType.rawClass)
        }

        @Test
        @DisplayName("Type.toCollectionJavaType():集合类型转换")
        fun testToCollectionJavaType() {
            val stringClass: Type = String::class.java
            val collectionType = stringClass.toCollectionJavaType(List::class.java)
            assertNotNull(collectionType)
            assertTrue(collectionType.isCollectionLikeType)
        }
    }

    @Nested
    @DisplayName("Class扩展方法测试")
    inner class ClassExtensionTest {
        @Test
        @DisplayName("Class.typeParamOfSuperClass():获取父类泛型参数")
        fun testTypeParamOfSuperClass() {
            val genericClass = StringGenericClass()
            val type = genericClass.javaClass.typeParamOfSuperClass()
            assertEquals(String::class.java, type)
        }

        @Test
        @DisplayName("Class.typeParamOfSuperClass():指定索引")
        fun testTypeParamOfSuperClassWithIndex() {
            val genericClass = StringGenericClass()
            val type = genericClass.javaClass.typeParamOfSuperClass(0)
            assertEquals(String::class.java, type)
        }

        @Test
        @DisplayName("Class.isTypeOrSubTypeOf():直接类型")
        fun testIsTypeOrSubTypeOfDirect() {
            assertTrue(String::class.java.isTypeOrSubTypeOf(String::class.java))
        }

        @Test
        @DisplayName("Class.isTypeOrSubTypeOf():继承关系")
        fun testIsTypeOrSubTypeOfInheritance() {
            assertTrue(ArrayList::class.java.isTypeOrSubTypeOf(List::class.java))
            assertTrue(LinkedList::class.java.isTypeOrSubTypeOf(Collection::class.java))
        }

        @Test
        @DisplayName("Class.isTypeOrSubTypeOf():无关系")
        fun testIsTypeOrSubTypeOfNoRelation() {
            assertFalse(String::class.java.isTypeOrSubTypeOf(Integer::class.java))
        }

        @Test
        @DisplayName("Class.isTypeOrSubTypeOf():null类型")
        fun testIsTypeOrSubTypeOfNull() {
            assertFalse(String::class.java.isTypeOrSubTypeOf(null))
        }

        @Test
        @DisplayName("Class.isTypesOrSubTypesOf():变参形式")
        fun testIsTypesOrSubTypesOfVararg() {
            val targetType = ArrayList::class.java
            val result = targetType.isTypesOrSubTypesOf(List::class.java, Collection::class.java, Object::class.java)
            assertTrue(result)
        }

        @Test
        @DisplayName("Class.isTypesOrSubTypesOf():集合形式")
        fun testIsTypesOrSubTypesOfCollection() {
            val targetType = ArrayList::class.java
            val types = listOf(List::class.java, Collection::class.java)
            val result = targetType.isTypesOrSubTypesOf(types)
            assertTrue(result)
        }

        @Test
        @DisplayName("Class.isNumberTypes():数字类型")
        fun testIsNumberTypes() {
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
                assertTrue(type.isNumberTypes())
            }
        }

        @Test
        @DisplayName("Class.isNumberTypes():非数字类型")
        fun testIsNumberTypesNonNumber() {
            val nonNumberTypes = listOf(
                String::class.java,
                Boolean::class.java,
                Date::class.java,
                List::class.java
            )
            nonNumberTypes.forEach { type ->
                assertFalse(type.isNumberTypes())
            }
        }

        @Test
        @DisplayName("Class.isStringLikeType():字符串类型")
        fun testIsStringLikeType() {
            val stringTypes = listOf(
                String::class.java,
                CharSequence::class.java,
                StringBuilder::class.java,
                StringBuffer::class.java
            )
            stringTypes.forEach { type ->
                assertTrue(type.isStringLikeType())
            }
        }

        @Test
        @DisplayName("Class.isStringLikeType():非字符串类型")
        fun testIsStringLikeTypeNonString() {
            val nonStringTypes = listOf(
                Int::class.java,
                Boolean::class.java,
                Date::class.java,
                List::class.java
            )
            nonStringTypes.forEach { type ->
                assertFalse(type.isStringLikeType())
            }
        }

        @Test
        @DisplayName("Class.isArrayLikeType():数组类型")
        fun testIsArrayLikeType() {
            val arrayTypes = listOf(
                Array<String>::class.java,
                List::class.java,
                Set::class.java,
                Collection::class.java,
                ArrayList::class.java,
                LinkedList::class.java
            )
            arrayTypes.forEach { type ->
                assertTrue(type.isArrayLikeType())
            }
        }

        @Test
        @DisplayName("Class.isArrayLikeType():非数组类型")
        fun testIsArrayLikeTypeNonArray() {
            val nonArrayTypes = listOf(
                String::class.java,
                Int::class.java,
                Boolean::class.java,
                Date::class.java
            )
            nonArrayTypes.forEach { type ->
                assertFalse(type.isArrayLikeType())
            }
        }
    }

    @Nested
    @DisplayName("TypeReference扩展方法测试")
    inner class TypeReferenceExtensionTest {
        @Test
        @DisplayName("TypeReference.rawClass():获取原始类")
        fun testTypeReferenceRawClass() {
            val typeRef = object : TypeReference<String>() {}
            val result = typeRef.rawClass()
            assertEquals(String::class.java, result)
        }

        @Test
        @DisplayName("TypeReference.isStringLikeType():字符串类型")
        fun testTypeReferenceIsStringLikeType() {
            val stringTypeRef = object : TypeReference<String>() {}
            assertTrue(stringTypeRef.isStringLikeType())

            val charTypeRef = object : TypeReference<Char>() {}
            assertTrue(charTypeRef.isStringLikeType())
        }

        @Test
        @DisplayName("TypeReference.isStringLikeType():非字符串类型")
        fun testTypeReferenceIsStringLikeTypeNonString() {
            val intTypeRef = object : TypeReference<Int>() {}
            assertFalse(intTypeRef.isStringLikeType())
        }

        @Test
        @DisplayName("TypeReference.isNumberTypes():数字类型")
        fun testTypeReferenceIsNumberTypes() {
            val intTypeRef = object : TypeReference<Int>() {}
            assertTrue(intTypeRef.isNumberTypes())

            val longTypeRef = object : TypeReference<Long>() {}
            assertTrue(longTypeRef.isNumberTypes())

            val bigIntegerTypeRef = object : TypeReference<BigInteger>() {}
            assertTrue(bigIntegerTypeRef.isNumberTypes())
        }

        @Test
        @DisplayName("TypeReference.isNumberTypes():非数字类型")
        fun testTypeReferenceIsNumberTypesNonNumber() {
            val stringTypeRef = object : TypeReference<String>() {}
            assertFalse(stringTypeRef.isNumberTypes())
        }
    }

    @Nested
    @DisplayName("JavaType扩展方法测试")
    inner class JavaTypeExtensionTest {
        @Test
        @DisplayName("JavaType.rawClass():获取原始类")
        fun testJavaTypeRawClass() {
            val javaType = TypeFactory.defaultInstance().constructType(String::class.java)
            val result = javaType.rawClass<String>()
            assertEquals(String::class.java, result)
        }

        @Test
        @DisplayName("JavaType.toCollectionJavaType():转换为集合类型")
        fun testJavaTypeToCollectionJavaType() {
            val javaType = TypeFactory.defaultInstance().constructType(String::class.java)
            val collectionType = javaType.toCollectionJavaType(List::class.java)
            assertNotNull(collectionType)
            assertTrue(collectionType.isCollectionLikeType)
        }

        @Test
        @DisplayName("JavaType.isDateTimeLikeType():日期时间类型")
        fun testJavaTypeIsDateTimeLikeType() {
            val dateType = TypeFactory.defaultInstance().constructType(Date::class.java)
            assertTrue(dateType.isDateTimeLikeType())

            val localDateTimeType = TypeFactory.defaultInstance().constructType(LocalDateTime::class.java)
            assertTrue(localDateTimeType.isDateTimeLikeType())
        }

        @Test
        @DisplayName("JavaType.isDateTimeLikeType():非日期时间类型")
        fun testJavaTypeIsDateTimeLikeTypeNonDateTime() {
            val stringType = TypeFactory.defaultInstance().constructType(String::class.java)
            assertFalse(stringType.isDateTimeLikeType())
        }

        @Test
        @DisplayName("JavaType.isArrayLikeType():数组类型")
        fun testJavaTypeIsArrayLikeType() {
            val arrayType = TypeFactory.defaultInstance().constructArrayType(String::class.java)
            assertTrue(arrayType.isArrayLikeType())

            val collectionType = TypeFactory.defaultInstance().constructCollectionType(List::class.java, String::class.java)
            assertTrue(collectionType.isArrayLikeType())
        }

        @Test
        @DisplayName("JavaType.isArrayLikeType():非数组类型")
        fun testJavaTypeIsArrayLikeTypeNonArray() {
            val stringType = TypeFactory.defaultInstance().constructType(String::class.java)
            assertFalse(stringType.isArrayLikeType())
        }

        @Test
        @DisplayName("JavaType.isBooleanType():布尔类型")
        fun testJavaTypeIsBooleanType() {
            val booleanType = TypeFactory.defaultInstance().constructType(Boolean::class.java)
            assertTrue(booleanType.isBooleanType())
        }

        @Test
        @DisplayName("JavaType.isBooleanType():非布尔类型")
        fun testJavaTypeIsBooleanTypeNonBoolean() {
            val stringType = TypeFactory.defaultInstance().constructType(String::class.java)
            assertFalse(stringType.isBooleanType())
        }

        @Test
        @DisplayName("JavaType.isNumberType():数字类型")
        fun testJavaTypeIsNumberType() {
            val intType = TypeFactory.defaultInstance().constructType(Int::class.java)
            assertTrue(intType.isNumberType())

            val longType = TypeFactory.defaultInstance().constructType(Long::class.java)
            assertTrue(longType.isNumberType())

            val bigDecimalType = TypeFactory.defaultInstance().constructType(BigDecimal::class.java)
            assertTrue(bigDecimalType.isNumberType())

            val bigIntegerType = TypeFactory.defaultInstance().constructType(BigInteger::class.java)
            assertTrue(bigIntegerType.isNumberType())
        }

        @Test
        @DisplayName("JavaType.isNumberType():非数字类型")
        fun testJavaTypeIsNumberTypeNonNumber() {
            val stringType = TypeFactory.defaultInstance().constructType(String::class.java)
            assertFalse(stringType.isNumberType())
        }

        @Test
        @DisplayName("JavaType.isByteType():字节类型")
        fun testJavaTypeIsByteType() {
            val byteType = TypeFactory.defaultInstance().constructType(Byte::class.java)
            assertTrue(byteType.isByteType())
        }

        @Test
        @DisplayName("JavaType.isShortType():短整型")
        fun testJavaTypeIsShortType() {
            val shortType = TypeFactory.defaultInstance().constructType(Short::class.java)
            assertTrue(shortType.isShortType())
        }

        @Test
        @DisplayName("JavaType.isIntType():整型")
        fun testJavaTypeIsIntType() {
            val intType = TypeFactory.defaultInstance().constructType(Int::class.java)
            assertTrue(intType.isIntType())
        }

        @Test
        @DisplayName("JavaType.isLongType():长整型")
        fun testJavaTypeIsLongType() {
            val longType = TypeFactory.defaultInstance().constructType(Long::class.java)
            assertTrue(longType.isLongType())
        }

        @Test
        @DisplayName("JavaType.isFloatType():浮点型")
        fun testJavaTypeIsFloatType() {
            val floatType = TypeFactory.defaultInstance().constructType(Float::class.java)
            assertTrue(floatType.isFloatType())
        }

        @Test
        @DisplayName("JavaType.isDoubleType():双精度浮点型")
        fun testJavaTypeIsDoubleType() {
            val doubleType = TypeFactory.defaultInstance().constructType(Double::class.java)
            assertTrue(doubleType.isDoubleType())
        }

        @Test
        @DisplayName("JavaType.isObjLikeType():对象类型")
        fun testJavaTypeIsObjLikeType() {
            val mapType = TypeFactory.defaultInstance().constructMapType(Map::class.java, String::class.java, String::class.java)
            assertTrue(mapType.isObjLikeType())

            val customType = TypeFactory.defaultInstance().constructType(TestUser::class.java)
            assertTrue(customType.isObjLikeType())
        }

        @Test
        @DisplayName("JavaType.isObjLikeType():非对象类型")
        fun testJavaTypeIsObjLikeTypeNonObject() {
            val stringType = TypeFactory.defaultInstance().constructType(String::class.java)
            assertFalse(stringType.isObjLikeType())

            val intType = TypeFactory.defaultInstance().constructType(Int::class.java)
            assertFalse(intType.isObjLikeType())
        }

        @Test
        @DisplayName("JavaType.isStringLikeType():字符串类型")
        fun testJavaTypeIsStringLikeType() {
            val stringType = TypeFactory.defaultInstance().constructType(String::class.java)
            assertTrue(stringType.isStringLikeType())

            val charType = TypeFactory.defaultInstance().constructType(Char::class.java)
            assertTrue(charType.isStringLikeType())
        }

        @Test
        @DisplayName("JavaType.isStringLikeType():非字符串类型")
        fun testJavaTypeIsStringLikeTypeNonString() {
            val intType = TypeFactory.defaultInstance().constructType(Int::class.java)
            assertFalse(intType.isStringLikeType())
        }
    }

    // 测试用的数据类
    data class TestUser(val id: Int, val name: String)
}
