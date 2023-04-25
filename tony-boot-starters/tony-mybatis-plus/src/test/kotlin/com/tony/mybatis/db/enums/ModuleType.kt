package com.tony.mybatis.db.enums

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue
import com.tony.enums.EnumCreator
import com.tony.enums.EnumIntValue

/**
 *
 * @author tangli
 * @since 2020-11-04 14:54
 */
enum class ModuleType(
    override val value: Int,
) : EnumIntValue {
    API(1),
    ROUTE(2),
    COMPONENT(3),

    @JsonEnumDefaultValue
    UNUSED(EnumCreator.defaultIntValue),
    ;

    companion object : EnumCreator<ModuleType, Int>(ModuleType::class.java) {

        @JsonCreator
        @JvmStatic
        override fun create(value: Int) =
            super.create(value)
    }
}
