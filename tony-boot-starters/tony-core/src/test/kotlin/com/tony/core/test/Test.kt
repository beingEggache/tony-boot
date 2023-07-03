package com.tony.core.test

import com.tony.utils.asToNotNull
import com.tony.utils.println
import com.tony.utils.typeParameter
import java.io.Serializable
import java.lang.reflect.ParameterizedType

fun main() {


}

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
        println(list::class.java.typeParameter())
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
