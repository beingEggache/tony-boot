package com.tony

import com.tony.Env.getPropertyByLazy

object ApiProperty {

    @JvmStatic
    val successCode: Int by getPropertyByLazy("core.success-code", 20000)

    @JvmStatic
    val defaultSuccessMessage: String by getPropertyByLazy("core.success-msg", "操作成功")

    @JvmStatic
    val unauthorizedCode: Int by getPropertyByLazy("core.unauthorized-code", 40100)

    @JvmStatic
    val errorCode: Int by getPropertyByLazy("core.error-code", 50000)

    @JvmStatic
    val validationErrorCode: Int by getPropertyByLazy("core.validation-error-code", 40000)

    @JvmStatic
    val bizErrorCode: Int by getPropertyByLazy("core.validation-error-code", 40010)

    @JvmStatic
    val errorMsg: String by getPropertyByLazy("core.error-msg", "访客太多，请稍后重试")

    @JvmStatic
    val validationErrorMsg: String by getPropertyByLazy("core.validation-error-msg", "请求有误，请检查后输入")
}
