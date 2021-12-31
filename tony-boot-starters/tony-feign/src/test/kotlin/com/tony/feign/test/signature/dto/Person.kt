/**
 * com.tony-dependencies
 * dtos
 *
 * TODO
 *
 * @author tangli
 * @since 2021/12/27 9:29
 */
package com.tony.feign.test.signature.dto


data class Person(
    val array: IntArray?,
    val number: Int?,
    val string: String?,
    val map: Map<String, *>?
)
