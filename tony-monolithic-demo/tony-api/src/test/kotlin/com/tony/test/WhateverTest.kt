package com.tony.test

/**
 *
 * @author tangli
 * @since 2020-11-05 11:31
 */
fun main() {


    val func0 = {}
    val func1 = { a: String -> a }
    val anyFunc: Any.() -> Unit = {}
    val anyFunc1: Any.() -> String = { "" }

    val funcList = listOf(func0, func1, anyFunc, anyFunc1)

    println(func0 is Function<*>)
    println(func1 is Function<*>)
    println(anyFunc is Function<*>)
    println(anyFunc1 is Function<*>)
}
