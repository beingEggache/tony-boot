package com.tony

import com.tony.Env.envPropByLazy

object ApiProperty {

    @JvmStatic
    val successCode: Int by envPropByLazy("core.success-code", 20000)

    @JvmStatic
    val unauthorizedCode: Int by envPropByLazy("core.unauthorized-code", 40100)

    @JvmStatic
    val errorCode: Int by envPropByLazy("core.error-code", 50000)

    @JvmStatic
    val validationErrorCode: Int by envPropByLazy("core.validation-error-code", 40000)

    @JvmStatic
    val bizErrorCode: Int by envPropByLazy("core.validation-error-code", 40010)

    @JvmStatic
    val errorMsg: String by envPropByLazy("core.error-msg", "访客太多，请稍后重试")

    @JvmStatic
    val validationErrorMsg: String by envPropByLazy("core.validation-error-msg", "参数有误，请检查后输入")
}
