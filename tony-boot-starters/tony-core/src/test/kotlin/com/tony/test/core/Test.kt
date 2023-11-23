package com.tony.test.core

import com.tony.utils.DigestAlgorithm
import com.tony.utils.println

fun main() {
    val src = "Hello World"
    DigestAlgorithm.MD5.digest(src).println()
    DigestAlgorithm.SHA1.digest(src).println()
    DigestAlgorithm.SHA256.digest(src).println()
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



