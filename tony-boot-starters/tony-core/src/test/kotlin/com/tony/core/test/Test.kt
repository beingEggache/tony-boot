package com.tony.core.test

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.core.JsonToken
import com.tony.utils.OBJECT_MAPPER
import com.tony.utils.asToNotNull
import com.tony.utils.getFromRootAsString
import com.tony.utils.println
import com.tony.utils.typeParameter
import java.io.Serializable
import java.lang.reflect.ParameterizedType

fun main() {
    val jsonDataInt = """{"code":20000,"message":"ok","data":{"int":999}}"""

    jsonDataInt.getFromRootAsString("data").println()
    val node = OBJECT_MAPPER.readTree(jsonDataInt)
    node["code"].asInt().println()
    node.get("data").println()

}
private val valueJsonToken =
    setOf(
        JsonToken.VALUE_NULL,
        JsonToken.VALUE_TRUE,
        JsonToken.VALUE_FALSE,
        JsonToken.VALUE_STRING,
        JsonToken.VALUE_NUMBER_INT,
        JsonToken.VALUE_NUMBER_FLOAT,
        JsonToken.VALUE_EMBEDDED_OBJECT,
    )
private val jsonFactory = JsonFactory()
public fun String.getNode(field: String): String? {
    jsonFactory.createParser(this).use {
        while (
            try {
                it.nextToken()
            } catch (e: JsonParseException) {
                return null
            } != null
        ) {
            if (it.currentToken == JsonToken.FIELD_NAME &&
                it.currentName == field &&
                it.parsingContext.parent.inRoot()
            ) {
                val token = it.nextToken()
                return if (token in valueJsonToken) {
                    it.text
                } else {
                    null
                }
            }
        }
    }
    return null
}

class TestIntData {

    var int: Int? = 1
}

class TestStringData {

    var int: Int? = 1
}

private fun printlnSuperType() {
    val firstParameter = TestLbs::class
        .java
        .methods
        .filter { it.name == "testLbs" }
        .first()
        .parameterTypes
        .first()
    firstParameter
        .genericSuperclass
        .asToNotNull<ParameterizedType>()
        .actualTypeArguments
        .first()
        .typeName
        .println()
    TestLbs().testLbs(ChildContainer())
}

class TestLbs {
    fun testLbs(list: ChildContainer) {
        println(list::class.java.typeParameter())
    }
}

open class DataContainer<T> : Serializable where T : Serializable
class ChildContainer : DataContainer<DataContainer<in Number>>()


fun quickSort(list: List<Int>): List<Int> =
    if (list.size < 2) list
    else {
        val pivot = list[list.size / 2]
        val less = list.filter { it < pivot }
        val equal = list.filter { it == pivot }
        val greater = list.filter { it > pivot }
        quickSort(less) + equal + quickSort(greater)
    }
