package com.tony.core.test

import com.tony.utils.println
import com.tony.utils.typeParameter

fun main() {

    Child::class.java.typeParameter().println()
}

open class ParentT<in T>
open class ChildT : ParentT<Parent<*>>()
open class Parent<T>

open class Child : Parent<ChildT>()


fun quickSort(list: List<Int>): List<Int> =
    if (list.size < 2) list
    else {
        val pivot = list[list.size / 2]
        val less = list.filter { it < pivot }
        val equal = list.filter { it == pivot }
        val greater = list.filter { it > pivot }
        quickSort(less) + equal + quickSort(greater)
    }
