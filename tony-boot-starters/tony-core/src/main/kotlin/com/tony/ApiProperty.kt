package com.tony

object ApiProperty {

    @JvmStatic
    val successCode: Int by lazy {
        Env.prop("core.success-code", 20000)
    }

    @JvmStatic
    val unauthorizedCode: Int by lazy {
        Env.prop("core.unauthorized-code", 40100)
    }

    @JvmStatic
    val errorCode: Int by lazy {
        Env.prop("core.error-code", 50000)
    }

    @JvmStatic
    val validationErrorCode: Int by lazy {
        Env.prop("core.validation-error-code", 40000)
    }

    @JvmStatic
    val bizErrorCode: Int by lazy {
        Env.prop("core.validation-error-code", 40010)
    }

    @JvmStatic
    val errorMsg: String by lazy {
        Env.prop("core.error-msg", "访客太多，请稍后重试")
    }

    @JvmStatic
    val validationErrorMsg: String by lazy {
        Env.prop("core.validation-error-msg", "参数有误，请检查后输入")
    }
}
