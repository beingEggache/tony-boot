package com.tony.core.test

import com.tony.utils.println
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext

fun main() {

    SpelExpressionParser()
        .parseExpression("obj.name")
        .getValue(StandardEvaluationContext(), TestSpelObj().apply {
            name = "123"
        })
        .println()
}

class TestSpelObj {
    var name: String? = null
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
