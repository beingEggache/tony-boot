package com.tony.test

import com.tony.utils.typeParamOfSuperInterface
import java.io.InputStream
import java.net.URL

/**
 * TypeTest is
 * @author Tang Li
 * @date 2023/07/05 17:01
 */
fun main() {

    val genericSuperclass1 = TestGenericClass1::class.java.genericSuperclass
    val genericInterfaces1 = TestGenericClass1::class.java.genericInterfaces

    val typeParamOfSuperInterface =
        TestGenericClass1::class.java.typeParamOfSuperInterface(GenericSuperInterface::class.java)

    println(typeParamOfSuperInterface)

    println(genericSuperclass1)
    println(genericInterfaces1)

    val genericSuperclass2 = TestGenericClass2::class.java.genericSuperclass
    val genericInterfaces2 = TestGenericClass2::class.java.genericInterfaces

    println(genericSuperclass2)
    println(genericInterfaces2)

    val genericSuperclass3 = TestGenericClass3::class.java.genericSuperclass
    val genericInterfaces3 = TestGenericClass3::class.java.genericInterfaces

    println(genericSuperclass3)
    println(genericInterfaces3)

    val genericSuperclass4 = TestGenericClass4::class.java.genericSuperclass
    val genericInterfaces4 = TestGenericClass4::class.java.genericInterfaces

    println(genericSuperclass4)
    println(genericInterfaces4)

    val genericSuperclass5 = GenericSuperClass::class.java.genericSuperclass
    val genericInterfaces5 = GenericSuperClass::class.java.genericInterfaces

    println(genericSuperclass5)
    println(genericInterfaces5)

    val genericSuperclass6 = GenericSuperInterface::class.java.genericSuperclass
    val genericInterfaces6 = GenericSuperInterface::class.java.genericInterfaces

    println(genericSuperclass6)
    println(genericInterfaces6)
}

class TestGenericClass1 : GenericSuperInterface<Long, CharSequence, Set<String>>,
    GenericSuperClass<String, Int, List<String>>()

class TestGenericClass2 : GenericSuperInterface<Long, CharSequence, Set<String>>,
    GenericSuperClass<String, Int, List<String>>()

class TestGenericClass3 : GenericInterface,
    GenericSuperClass<String, Int, List<String>>()

class TestGenericClass4 : GenericInterface, GenericClass()

interface GenericInterface : GenericSuperInterface<Short, Byte, URL>
interface GenericSuperInterface<T, E, S>

open class GenericClass : GenericSuperClass<Byte, StringBuffer, InputStream>()
abstract class GenericSuperClass<T, E, S>
