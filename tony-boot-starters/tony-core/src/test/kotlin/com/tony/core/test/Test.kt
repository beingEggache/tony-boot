package com.tony.core.test

import com.tony.utils.toNumber
import com.tony.utils.println

fun main() {


    "123".toNumber(Double::class.java).println()
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
