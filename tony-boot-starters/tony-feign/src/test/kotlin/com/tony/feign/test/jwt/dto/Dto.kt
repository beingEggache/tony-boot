/**
 * dtos
 *
 * TODO
 *
 * @author tangli
 * @since 2021/12/27 9:29
 */
package com.tony.feign.test.jwt.dto


data class LoginReq(
    val account: String?,
    val pwd: String?
)

data class Person(
    val name: String,
    val age: Int
)
