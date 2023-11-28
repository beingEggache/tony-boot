package com.tony.test.core

import com.tony.utils.println
import java.time.LocalDateTime

fun main() {
    TestDelegate.saySomething.println()
    TestDelegate.saySomething.println()
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


interface ITestDelegate {
    val saySomething: String
}

object TestDelegate : ITestDelegate by object : ITestDelegate {
    override val saySomething: String
        get() = LocalDateTime.now().toString()
}
