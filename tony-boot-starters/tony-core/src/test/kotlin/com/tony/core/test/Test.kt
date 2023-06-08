package com.tony.core.test

import com.tony.utils.println

fun main() {

    val clazz = TestFieldGeneric::class.java
    val genericType1 = clazz.getDeclaredField("field1").genericType
    val genericType2 = clazz.getDeclaredField("field2").genericType
    println(genericType1.typeName)
    println(genericType2.typeName)
    println(genericType1 == genericType2)
}

class TestFieldGeneric {
    val field1: List<Map<String, List<String>>>? = null
    val field2: List<Map<String, List<String>>>? = null
}

fun quickSort(list: List<Int>): List<Int> =
    if (list.size < 2) list
    else {
        val pivot = list[list.size / 2]
        val less = list.filter { it < pivot }
        val equal = list.filter { it == pivot }
        val greater = list.filter { it > pivot }
        quickSort(less) + equal + quickSort(greater)
    }
