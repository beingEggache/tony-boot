package com.tony

import com.tony.SpringContexts.Env

public object ApiProperty {

    @JvmStatic
    public val successCode: Int by Env.getPropertyByLazy("core.success-code", 20000)

    @JvmStatic
    public val defaultSuccessMessage: String by Env.getPropertyByLazy("core.success-msg", "操作成功")

    @JvmStatic
    public val unauthorizedCode: Int by Env.getPropertyByLazy("core.unauthorized-code", 40100)

    @JvmStatic
    public val errorCode: Int by Env.getPropertyByLazy("core.error-code", 50000)

    @JvmStatic
    public val validationErrorCode: Int by Env.getPropertyByLazy("core.validation-error-code", 40000)

    @JvmStatic
    public val bizErrorCode: Int by Env.getPropertyByLazy("core.validation-error-code", 40010)

    @JvmStatic
    public val errorMsg: String by Env.getPropertyByLazy("core.error-msg", "访客太多，请稍后重试")

    @JvmStatic
    public val validationErrorMsg: String by Env.getPropertyByLazy("core.validation-error-msg", "请求有误，请检查后输入")

    @JvmStatic
    public val notFoundMessage: String = "该对象不存在"

    @JvmStatic
    public val notFoundCode: Int = 40404
}
