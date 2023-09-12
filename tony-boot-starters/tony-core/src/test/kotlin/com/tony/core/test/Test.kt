package com.tony.core.test

import com.tony.codec.encodeToString
import com.tony.codec.enums.Encoding
import com.tony.utils.println

fun main() {
    val src = "Hello World"
    src.encodeToString(Encoding.BASE64).println()
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



