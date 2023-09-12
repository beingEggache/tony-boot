package com.tony.redis.test.model

import java.math.BigDecimal
import java.math.BigInteger

/**
 * TestModels is
 * @author Tang Li
 * @date 2023/06/05 11:14
 */
class SimpleObj(val name: String, val age: Int)

class ObjWithNumberTypes(
    val byte: Byte,
    val short: Short,
    val int: Int,
    val long: Long,
    val bigInteger: BigInteger,
    val float: Float,
    val double: Double,
    val bigDecimal: BigDecimal,
)

class ObjWithList(val name: String, val list: List<String>)

class ObjWithMap(val name: String, val map: Map<String, SimpleObj>)

class ObjWithObjList(val name: String, val list: List<SimpleObj>)

class ObjWithObjMap(val name: String, val list: Map<String, ObjWithObjList>)
