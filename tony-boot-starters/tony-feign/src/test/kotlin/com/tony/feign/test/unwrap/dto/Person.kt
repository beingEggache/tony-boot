/**
 * dtos
 *
 * TODO
 *
 * @author tangli
 * @since 2021/12/27 9:29
 */
package com.tony.feign.test.unwrap.dto

import jakarta.validation.constraints.NotNull


data class Person(
    val array: IntArray?,
    @field:NotNull(message = "请输入号码")
    val number: Int?,
    val string: String?,
    val map: Map<String, *>?
)
