package tony.test

import tony.utils.md5

/**
 *
 * @author tangli
 * @date 2020-11-05 11:31
 */
fun main() {
    val uppercase = "123456".md5().uppercase()
    val pwd = "${uppercase}TONY_SALT".md5().uppercase()
    println(uppercase)
    println(pwd)
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
    return this
}
