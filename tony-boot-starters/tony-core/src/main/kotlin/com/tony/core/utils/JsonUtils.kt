@file:JvmName("JsonUtils")

package com.tony.core.utils

import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.IOException

fun <T> T?.toJsonString(): String = if (this != null) OBJECT_MAPPER.writeValueAsString(this) else ""

@Throws(IOException::class)
inline fun <reified T> String.jsonToObj(): T =
    OBJECT_MAPPER.readValue(this)

@Throws(IOException::class)
fun <T> String.jsonToObj(javaType: JavaType): T =
    OBJECT_MAPPER.readValue(this, javaType)

fun JavaType.isArrayLikeType() =
    isArrayType || isCollectionLikeType

fun JavaType.isNumberType() =
    isTypeOrSubTypeOf(Number::class.java)

fun JavaType.isObjLikeType() =
    isMapLikeType || (!isArrayLikeType() && !isNumberType() && !isStringLikeType())

fun JavaType.isStringLikeType() =
    isTypeOrSubTypeOf(CharSequence::class.java) ||
        isTypeOrSubTypeOf(Character::class.java) ||
        isTypeOrSubTypeOf(Char::class.java)
