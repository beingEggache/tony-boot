package com.tony

import com.tony.SpringContexts.Env

public object ApiProperty {

    @JvmStatic
    public val okCode: Int by Env.getPropertyByLazy("core.ok-code", 20000)

    @JvmStatic
    public val defaultOkMessage: String by Env.getPropertyByLazy("core.ok-msg", "操作成功")

    @JvmStatic
    public val unauthorizedCode: Int by Env.getPropertyByLazy("core.unauthorized-code", 40100)

    @JvmStatic
    public val errorCode: Int by Env.getPropertyByLazy("core.internal-server-error-code", 50000)

    @JvmStatic
    public val badRequestCode: Int by Env.getPropertyByLazy("core.bad-request-code", 40000)

    @JvmStatic
    public val badRequestMsg: String by Env.getPropertyByLazy("core.bad-request-msg", "请求有误，请检查后输入")

    @JvmStatic
    public val preconditionFailedCode: Int by Env.getPropertyByLazy("core.precondition-failed-code", 40010)

    @JvmStatic
    public val errorMsg: String by Env.getPropertyByLazy("core.internal-server-error-msg", "访客太多，请稍后重试")

    @JvmStatic
    public val notFoundMessage: String = "该对象不存在"

    @JvmStatic
    public val notFoundCode: Int = 40404
}
