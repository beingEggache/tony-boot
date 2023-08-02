package com.tony.misc

import com.tony.ApiResultLike
import java.math.BigDecimal
import java.math.BigInteger
import java.time.temporal.TemporalAccessor
import java.util.Date

public val notSupportResponseWrapClasses: Array<Class<*>?> =
    arrayOf(
        ApiResultLike::class.java,
        Enum::class.java,
        Date::class.java,
        TemporalAccessor::class.java,
        // converterType is use StringHttpMessageConverter
        java.lang.CharSequence::class.java,
        CharSequence::class.java,
        Char::class.javaObjectType,
        Char::class.javaPrimitiveType,
        Boolean::class.javaObjectType,
        Boolean::class.javaPrimitiveType,
        Long::class.javaObjectType, Long::class.javaPrimitiveType,
        Int::class.javaObjectType, Int::class.javaPrimitiveType,
        Double::class.javaObjectType, Double::class.javaPrimitiveType,
        Byte::class.javaObjectType, Byte::class.javaPrimitiveType,
        Short::class.javaObjectType, Short::class.javaPrimitiveType,
        Float::class.javaObjectType, Float::class.javaPrimitiveType,
        BigInteger::class.java,
        BigDecimal::class.java
    )
