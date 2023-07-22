package com.tony.web.test.req

import com.fasterxml.jackson.annotation.JacksonInject
import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.annotation.OptBoolean

/**
 * TestInjectReq is
 * @author tangli
 * @since 2023/07/06 15:26
 */
data class TestDefaultInjectReq(

    @JsonSetter
    @get:JacksonInject("string0", useInput = OptBoolean.TRUE)
    val valNotNullTrue: String,

    @JsonSetter
    @get:JacksonInject("string1", useInput = OptBoolean.FALSE)
    val valNotNullFalse: String,

    @set:JacksonInject("string2", useInput = OptBoolean.TRUE)
    var varSetterTrue: String?,

    @set:JacksonInject("string3", useInput = OptBoolean.FALSE)
    var varSetterFalse: String?,
)
