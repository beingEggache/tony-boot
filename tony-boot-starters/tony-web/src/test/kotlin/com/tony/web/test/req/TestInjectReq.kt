package com.tony.web.test.req

/**
 * TestInjectReq is
 * @author tangli
 * @since 2023/07/06 15:26
 */
data class TestDefaultInjectReq(
    val name: String = "ok then",
    val list: List<String>,
    val set: Set<String>,
    val obj: TestInjectReqObjField = TestInjectReqObjField().apply { this.list = listOf("aloha") },
)

class TestInjectReqObjField {
    var list: List<String>? = null
    var obj: Map<String, Int>? = null
}
