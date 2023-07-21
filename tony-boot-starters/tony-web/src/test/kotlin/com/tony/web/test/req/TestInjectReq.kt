package com.tony.web.test.req

import com.fasterxml.jackson.annotation.JacksonInject
import com.fasterxml.jackson.annotation.OptBoolean

/**
 * TestInjectReq is
 * @author tangli
 * @since 2023/07/06 15:26
 */
@Suppress("ArrayInDataClass")
data class TestDefaultInjectReq(
    @field:JacksonInject("string", useInput = OptBoolean.TRUE)
    val name: String = "",
    val arr: BooleanArray,
    val list: List<String>,
    val set: Set<String>,
    val obj: TestInjectReqObjField = TestInjectReqObjField().apply { this.list = listOf("aloha") },
)

class TestInjectReqObjField {
    var list: List<String>? = null
    var obj: Map<String, Int>? = null
}
