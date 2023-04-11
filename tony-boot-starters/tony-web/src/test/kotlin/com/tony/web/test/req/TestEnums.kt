package com.tony.web.test.req

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue
import com.tony.enums.EnumCreator
import com.tony.enums.EnumIntValue
import com.tony.enums.EnumStringValue

enum class TestStringEnum(
    override val value: String
) : EnumStringValue {
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
) : EnumIntValue {
    TEST_1(1),
    TEST_2(2),
    @JsonEnumDefaultValue
    UNKNOWN(EnumCreator.defaultIntValue)
    ;
    companion object : EnumCreator<TestIntEnum, Int>(TestIntEnum::class.java) {

        @JsonCreator
        @JvmStatic
        override fun create(value: Int): TestIntEnum? {
            return super.create(value)
        }
    }
}
