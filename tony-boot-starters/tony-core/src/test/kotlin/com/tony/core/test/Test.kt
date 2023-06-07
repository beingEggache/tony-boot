package com.tony.core.test

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.type.TypeFactory
import com.tony.utils.asTo

fun main() {

    val type = (object : TypeReference<String>() {}).type
    val clazz = type.asTo<Class<String>>()
    println(clazz)

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
