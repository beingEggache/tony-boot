/*
 * MIT License
 *
 * Copyright (c) 2023-present, tangli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

@file:JvmName("ObjectMappers")
/**
 * ObjectMapper 相关
 * @author tangli
 * @date 2023/09/13 19:18
 */

package tony.core.jackson

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
 * @author tangli
 * @date 2023/09/13 19:18
 */
public fun ObjectMapper.initialize(): ObjectMapper =
    apply {
        val kotlinModule =
            KotlinModule
                .Builder()
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
        configOverride(Long::class.java).format = JsonFormat.Value.forShape(JsonFormat.Shape.STRING)
        configOverride(Float::class.java).format = JsonFormat.Value.forShape(JsonFormat.Shape.STRING)
        configOverride(Double::class.java).format = JsonFormat.Value.forShape(JsonFormat.Shape.STRING)
        configOverride(BigDecimal::class.java).format = JsonFormat.Value.forShape(JsonFormat.Shape.STRING)
        configOverride(BigInteger::class.java).format = JsonFormat.Value.forShape(JsonFormat.Shape.STRING)

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
