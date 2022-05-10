package com.tony.test

import java.time.Duration

/**
 *
 * @author tangli
 * @since 2020-11-05 11:31
 */
fun main() {
    val toDays = Duration.ofSeconds(80).toDays()
    println(toDays)

}
