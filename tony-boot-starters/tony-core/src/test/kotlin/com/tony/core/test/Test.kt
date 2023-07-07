package com.tony.core.test

import com.tony.utils.asToNotNull
import com.tony.utils.jsonToObj
import com.tony.utils.println
import com.tony.utils.toJsonString
import com.tony.utils.typeParamOfSuperClass
import java.io.Serializable
import java.lang.reflect.ParameterizedType
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date

fun main() {
    val person1 = """{"age":18}""".jsonToObj<Person>()
    val person2 = """{"age":18}""".jsonToObj<Person>()
    val person3 = """{"age":18}""".jsonToObj<Person>()
    println(person1)
    println(person2)
    println(person3)
    println(person1.toJsonString())
    println(person2.toJsonString())
    println(person3.toJsonString())
}

data class Person(
    val name: Array<String> = emptyArray(),
    val age: Int,
    val date: Date = Date(),
    val localDateTime: LocalDateTime = LocalDateTime.now(),
    val localDate: LocalDate = LocalDate.now()
)

open class Parent

open class Child : Parent()

class TestGeneric


private fun printlnSuperType() {
    val firstParameter = TestLbs::class
        .java
        .methods
        .filter { it.name == "testLbs" }
        .first()
        .parameterTypes
        .first()
    firstParameter
        .genericSuperclass
        .asToNotNull<ParameterizedType>()
        .actualTypeArguments
        .first()
        .typeName
        .println()
    TestLbs().testLbs(ChildContainer())
}

class TestLbs {
    fun testLbs(list: ChildContainer) {
        println(list::class.java.typeParamOfSuperClass())
    }
}

open class DataContainer<T> : Serializable where T : Serializable
class ChildContainer : DataContainer<DataContainer<in Number>>()


fun quickSort(list: List<Int>): List<Int> =
    if (list.size < 2) list
    else {
        val pivot = list[list.size / 2]
        val less = list.filter { it < pivot }
        val equal = list.filter { it == pivot }
        val greater = list.filter { it > pivot }
        quickSort(less) + equal + quickSort(greater)
    }
