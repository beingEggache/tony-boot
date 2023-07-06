package com.tony.web.test.req

import com.tony.web.support.annotation.InjectEmptyIfNull

/**
 * TestInjectReq is
 * @author tangli
 * @since 2023/07/06 15:26
 */
@Suppress("ArrayInDataClass")
data class TestDefaultInjectReq(
    @InjectEmptyIfNull
    val name: String?,
    @InjectEmptyIfNull
    val list: List<String>?,
    @InjectEmptyIfNull
    val set: Set<String>?,
    @InjectEmptyIfNull
    val arr1: Array<Any>?,
    @InjectEmptyIfNull
    val arr2: Array<String>?,
    val booleanArr: BooleanArray?,
    val byteArr: ByteArray?,
    @InjectEmptyIfNull
    val shortArray: ShortArray?,
    @InjectEmptyIfNull
    val intArray: IntArray?,
    @InjectEmptyIfNull
    val longArray: LongArray?,
    @InjectEmptyIfNull
    val floatArray: FloatArray?,
    @InjectEmptyIfNull
    val doubleArray: DoubleArray?,
    @InjectEmptyIfNull
    val obj: TestInjectReqObjField?,
)

class TestInjectReqObjField {
    var list: List<String>? = null
    var obj: Map<String, Int>? = null
}
