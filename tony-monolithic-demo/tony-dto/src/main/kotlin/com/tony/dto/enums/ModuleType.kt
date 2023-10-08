package com.tony.dto.enums

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue
import com.tony.enums.DEFAULT_INT_VALUE
import com.tony.enums.IntEnumCreator
import com.tony.enums.IntEnumValue

/**
 *
 * @author Tang Li
 * @date 2020-11-04 14:54
 */
enum class ModuleType(
    override val value: Int,
) : IntEnumValue {
    API(1),
    ROUTE(2),
    COMPONENT(3),

    @JsonEnumDefaultValue
    UNUSED(DEFAULT_INT_VALUE),
    ;

    companion object : IntEnumCreator(ModuleType::class.java) {

        @JsonCreator
        @JvmStatic
        override fun create(value: Int) = super.create(value)
    }
}
