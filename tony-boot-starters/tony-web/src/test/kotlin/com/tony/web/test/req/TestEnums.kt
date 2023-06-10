package com.tony.web.test.req

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue
import com.tony.enums.DEFAULT_INT_VALUE
import com.tony.enums.EnumCreator
import com.tony.enums.IntEnumValue
import com.tony.enums.StringEnumValue

enum class TestStringEnum(
    override val value: String
) : StringEnumValue {
    TEST_1("1"),
    TEST_2("1"),
    ;
    init {
        TestStringEnum.Companion
    }

    companion object : EnumCreator<TestStringEnum, String>(TestStringEnum::class.java) {
        @JsonCreator
        @JvmStatic
        override fun create(value: String): TestStringEnum? {
            return super.create(value)
        }
    }
}

enum class TestIntEnum(
    override val value: Int
) : IntEnumValue {
    TEST_1(1),
    TEST_2(2),
    @JsonEnumDefaultValue
    UNKNOWN(DEFAULT_INT_VALUE)
    ;
    companion object : EnumCreator<TestIntEnum, Int>(TestIntEnum::class.java) {

        @JsonCreator
        @JvmStatic
        override fun create(value: Int): TestIntEnum? {
            return super.create(value)
        }
    }
}
