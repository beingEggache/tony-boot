package com.tony.dto.enums

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue
import com.tony.enums.DEFAULT_INT_VALUE
import com.tony.enums.IntEnumCreator
import com.tony.enums.IntEnumValue

/**
 * 帐户状态
 * @author tangli
 * @date 2024/07/09 09:19
 * @since 1.0.0
 */
@Suppress("unused")
enum class AccountState(
    override val value: Int,
) : IntEnumValue {
    /**
     * 需要修改密码
     */
    NEED_CHANGE_PWD(0),

    /**
     * 正常
     */
    NORMAL(1),

    @JsonEnumDefaultValue
    UNUSED(DEFAULT_INT_VALUE),
    ;

    companion object : IntEnumCreator(AccountState::class.java) {
        @JsonCreator
        @JvmStatic
        override fun create(value: Int) =
            super.create(value)
    }
}
