package com.tony.core.test

import com.tony.utils.println
import com.tony.utils.toJsonString
import kotlin.reflect.KClass

fun main() {
    "adasdasd".toJsonString().println()
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

class TestAnno {

    fun testAnno() {
    }
}



