package com.tony.test

import com.tony.PageQuery
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
    val obj1StringAbsent = """{}""".jsonToObj<PageQuery<IntArray>>()
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
    val pageQueryObj = """{"query":{}}""".jsonToObj<PageQuery<JavaTestPojo>>()
    val pageQueryMap = """{"query":null}""".jsonToObj<PageQuery<Map<String, Any>>>()
    val pageQueryList = """{"query":null}""".jsonToObj<PageQuery<List<Int>>>()
    val pageQueryArr = """{"query":null}""".jsonToObj<PageQuery<Array<*>>>()
    val pageQueryAbsentArr = """{}""".jsonToObj<PageQuery<Array<*>>>()
    val pageQueryIntArr = """{"query":null}""".jsonToObj<PageQuery<IntArray>>()

    val pageQueryBoolean = """{"query":false}""".jsonToObj<PageQuery<Boolean>>()
    val pageQueryByte = """{"query":1}""".jsonToObj<PageQuery<Byte>>()

    val pageQueryShort = """{"query":2}""".jsonToObj<PageQuery<Short>>()
    val pageQueryInt = """{"query":3}""".jsonToObj<PageQuery<Int>>()

    val pageQueryLong = """{"query":4}""".jsonToObj<PageQuery<Long>>()
    val pageQueryBigInteger = """{"query":5}""".jsonToObj<PageQuery<BigInteger>>()

    val pageQueryFloat = """{"query":6}""".jsonToObj<PageQuery<Float>>()
    val pageQueryDouble = """{"query":7}""".jsonToObj<PageQuery<Double>>()
    val pageQueryBigDecimal = """{"query":8}""".jsonToObj<PageQuery<BigDecimal>>()

    val obj1String = """{"query":"aloha"}""".jsonToObj<PageQuery<String>>()
    val obj1StringAbsent = """{}""".jsonToObj<PageQuery<String>>()

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

