package com.tony.test

import com.tony.utils.secureRandom

/**
 *
 * @author Tang Li
 * @date 2020-11-05 11:31
 */
fun main() {
    val list = makeArray(100)

    list.bubbleSort().forEach { println(it) }
}

fun Array<Int>.bubbleSort():Array<Int> {
    for (index in this.indices) {
        for (innerIndex in 0 until  this.size -1 - index) {
            if (this[innerIndex] > this[innerIndex + 1]) {
                val temp = this[innerIndex]
                this[innerIndex] = this[innerIndex + 1]
                this[innerIndex + 1] = temp
            }
        }
    }
    return this;
}

private fun makeArray(size: Int): Array<Int> {
    val set = HashSet<Int>(size)
    repeat(size) {
        set.add(secureRandom.nextInt(size))
    }

    return set.shuffled().toTypedArray()
}
