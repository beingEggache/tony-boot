package com.tony.core.test

import com.tony.utils.println

fun main() {

    val b = true

    println("Boolean::class:  " + Boolean::class)
    println("Boolean::class.java:  " + Boolean::class.java)
    println("Boolean::class.javaObjectType:  " + Boolean::class.javaObjectType)
    println("Boolean::class.javaPrimitiveType:  " + Boolean::class.javaPrimitiveType)

    println("b.javaClass:  " + b.javaClass)
    println("b::class:  " + b::class)
    println("b::class.java:   " + b::class.java)
    println("b::class.javaObjectType:  " + b::class.javaObjectType)
    println("b::class.javaPrimitiveType:  " + b::class.javaPrimitiveType)
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
