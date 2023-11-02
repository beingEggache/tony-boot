package com.tony.test

import com.tony.JPageQuery
import com.tony.core.test.JavaTestPojo
import com.tony.utils.jsonToObj
import com.tony.utils.println
import com.tony.utils.toJsonString
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Positive
import java.math.BigDecimal
import java.math.BigInteger

fun main() {
    val obj1StringAbsent = """{}""".jsonToObj<JPageQuery<IntArray>>()
    obj1StringAbsent.query.println()
}


data class TestQuery @JvmOverloads constructor(
    @field:Positive(message = "请输入正整数")
    val age: Int = 0,

    @field:NotBlank(message = "请输入姓名")
    val name: String = "",

    @field:NotEmpty(message = "请选择爱好")
    val hobby: List<String> = emptyList()
)

private fun extracted() {
    val pageQueryObj = """{"query":{}}""".jsonToObj<JPageQuery<JavaTestPojo>>()
    val pageQueryMap = """{"query":null}""".jsonToObj<JPageQuery<Map<String, Any>>>()
    val pageQueryList = """{"query":null}""".jsonToObj<JPageQuery<List<Int>>>()
    val pageQueryArr = """{"query":null}""".jsonToObj<JPageQuery<Array<*>>>()
    val pageQueryAbsentArr = """{}""".jsonToObj<JPageQuery<Array<*>>>()
    val pageQueryIntArr = """{"query":null}""".jsonToObj<JPageQuery<IntArray>>()

    val pageQueryBoolean = """{"query":false}""".jsonToObj<JPageQuery<Boolean>>()
    val pageQueryByte = """{"query":1}""".jsonToObj<JPageQuery<Byte>>()

    val pageQueryShort = """{"query":2}""".jsonToObj<JPageQuery<Short>>()
    val pageQueryInt = """{"query":3}""".jsonToObj<JPageQuery<Int>>()

    val pageQueryLong = """{"query":4}""".jsonToObj<JPageQuery<Long>>()
    val pageQueryBigInteger = """{"query":5}""".jsonToObj<JPageQuery<BigInteger>>()

    val pageQueryFloat = """{"query":6}""".jsonToObj<JPageQuery<Float>>()
    val pageQueryDouble = """{"query":7}""".jsonToObj<JPageQuery<Double>>()
    val pageQueryBigDecimal = """{"query":8}""".jsonToObj<JPageQuery<BigDecimal>>()

    val obj1String = """{"query":"aloha"}""".jsonToObj<JPageQuery<String>>()
    val obj1StringAbsent = """{}""".jsonToObj<JPageQuery<String>>()

    println("pageQueryObj: ${pageQueryObj.toJsonString()}")
    println("pageQueryMap:${pageQueryMap.toJsonString()}")
    println("pageQueryList:${pageQueryList.toJsonString()}")
    println("pageQueryArr:${pageQueryArr.toJsonString()}")
    println("pageQueryAbsentArr:${pageQueryAbsentArr.toJsonString()}")
    println("pageQueryIntArr:${pageQueryIntArr.toJsonString()}")
    println("pageQueryBoolean:${pageQueryBoolean.toJsonString()}")
    println("pageQueryByte:${pageQueryByte.toJsonString()}")
    println("pageQueryShort:${pageQueryShort.toJsonString()}")
    println("pageQueryInt:${pageQueryInt.toJsonString()}")
    println("pageQueryLong:${pageQueryLong.toJsonString()}")
    println("pageQueryBigInteger:${pageQueryBigInteger.toJsonString()}")
    println("pageQueryFloat:${pageQueryFloat.toJsonString()}")
    println("pageQueryDouble:${pageQueryDouble.toJsonString()}")
    println("pageQueryBigDecimal:${pageQueryBigDecimal.toJsonString()}")
    println("obj1String:${obj1String.toJsonString()}")
    println("obj1StringAbsent:${obj1StringAbsent.toJsonString()}")
}

