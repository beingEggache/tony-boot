@file:JvmName("ObjectMappers")

package com.tony.jackson

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.annotation.Nulls
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.StreamWriteFeature
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDateTime
import java.util.Date
import java.util.TimeZone

/**
 * ObjectMapper 初始化
 * @return [ObjectMapper]
 * @author Tang Li
 * @date 2023/09/13 10:18
 * @since 1.0.0
 */
public fun ObjectMapper.initialize(): ObjectMapper = apply {
    val kotlinModule = KotlinModule.Builder()
        .apply {
            enable(KotlinFeature.NullIsSameAsDefault)
            enable(KotlinFeature.NullToEmptyCollection)
            enable(KotlinFeature.NullToEmptyMap)
        }.build()
    registerModules(kotlinModule, JavaTimeModule(), ParameterNamesModule())
    setTimeZone(TimeZone.getDefault())
    setSerializationInclusion(JsonInclude.Include.ALWAYS)
    setDefaultSetterInfo(JsonSetter.Value.forValueNulls(Nulls.SKIP, Nulls.SKIP))

    configOverride(Date::class.java).format = JsonFormat.Value.forPattern("yyyy-MM-dd HH:mm:ss")
    configOverride(LocalDateTime::class.java).format = JsonFormat.Value.forPattern("yyyy-MM-dd HH:mm:ss")
    configOverride(Long::class.java).setFormat(JsonFormat.Value.forShape(JsonFormat.Shape.STRING))
    configOverride(Float::class.java).setFormat(JsonFormat.Value.forShape(JsonFormat.Shape.STRING))
    configOverride(Double::class.java).setFormat(JsonFormat.Value.forShape(JsonFormat.Shape.STRING))
    configOverride(BigDecimal::class.java).setFormat(JsonFormat.Value.forShape(JsonFormat.Shape.STRING))
    configOverride(BigInteger::class.java).setFormat(JsonFormat.Value.forShape(JsonFormat.Shape.STRING))

    disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
    disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    enable(
        DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE,
        DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS,
        DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT
    )
    disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    enable(
        JsonGenerator.Feature.IGNORE_UNKNOWN,
        JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN,
        StreamWriteFeature.USE_FAST_DOUBLE_WRITER.mappedFeature()
    )
    enable(
        JsonParser.Feature.USE_FAST_BIG_NUMBER_PARSER,
        JsonParser.Feature.USE_FAST_DOUBLE_PARSER
    )
}
