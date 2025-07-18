package tony.demo.sys.dto.enums

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue
import tony.core.enums.DEFAULT_INT_VALUE
import tony.core.enums.IntEnumCreator
import tony.core.enums.IntEnumValue

/**
 * 帐户状态
 * @author tangli
 * @date 2024/07/09 09:19
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
