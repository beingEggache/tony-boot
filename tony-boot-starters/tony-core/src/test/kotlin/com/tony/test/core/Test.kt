package com.tony.test.core

import com.tony.utils.dateOverlap
import com.tony.utils.overlap
import com.tony.utils.toDate
import java.time.LocalDate

fun main() {
    val start1 = LocalDate.of(2023, 12, 4)
    val end1 = LocalDate.of(2023, 12, 8)

    val start2 = LocalDate.of(2023, 12, 3)
    val end2 = LocalDate.of(2023, 12, 9)

    println((start1 to end1).overlap(start2 to end2))
    println((start1.toDate() to end1.toDate()).dateOverlap(start2.toDate() to end2.toDate()))

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
