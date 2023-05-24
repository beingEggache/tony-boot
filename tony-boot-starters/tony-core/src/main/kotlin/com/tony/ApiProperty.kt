package com.tony

import com.tony.SpringContexts.Env

/**
 * 核心常量.
 *
 * 比如成功返回码, 失败返回码等.
 *
 * 可读取配置.
 * @author tangli
 * @since 2022/9/29 10:20
 */
public object ApiProperty {

    /**
     * 全局成功返回码.
     *
     * 可由配置文件的 "core.ok-code" 修改默认值.
     *
     * 默认 20000.
     */
    @JvmStatic
    public val okCode: Int by Env.getPropertyByLazy("core.ok-code", 20000)

    /**
     * 全局成功返回消息.
     *
     * 可由配置文件的 "core.ok-msg" 修改默认值.
     *
     * 默认 "操作成功".
     */
    @JvmStatic
    public val defaultOkMessage: String by Env.getPropertyByLazy("core.ok-msg", "操作成功")

    /**
     * 全局未认证返回码.
     *
     * 可由配置文件的 "core.unauthorized-code" 修改默认值.
     *
     * 默认 40100.
     */
    @JvmStatic
    public val unauthorizedCode: Int by Env.getPropertyByLazy("core.unauthorized-code", 40100)

    /**
     * 全局错误返回码.
     *
     * 可由配置文件的 "core.internal-server-error-code" 修改默认值.
     *
     * 默认 50000.
     */
    @JvmStatic
    public val errorCode: Int by Env.getPropertyByLazy("core.internal-server-error-code", 50000)

    /**
     * 全局错误返回消息.
     *
     * 可由配置文件的 "core.internal-server-error-msg" 修改默认值.
     *
     * 默认 "访客太多，请稍后重试".
     */
    @JvmStatic
    public val errorMsg: String by Env.getPropertyByLazy("core.internal-server-error-msg", "访客太多，请稍后重试")

    /**
     * 全局错误请求码. 一般是请求不合法, 比如 http 方法不对应等.
     *
     * 可由配置文件的 "core.bad-request-code" 修改默认值.
     *
     * 默认 40000.
     */
    @JvmStatic
    public val badRequestCode: Int by Env.getPropertyByLazy("core.bad-request-code", 40000)

    /**
     * 全局错误请求返回消息. 一般是请求不合法, 比如 http 方法不对应等.
     *
     * 可由配置文件的 "core.bad-request-msg" 修改默认值.
     *
     * 默认 "请求有误，请检查后输入".
     */
    @JvmStatic
    public val badRequestMsg: String by Env.getPropertyByLazy("core.bad-request-msg", "请求有误，请检查后输入")

    /**
     * 全局校验不通过返回码.
     *
     * 可由配置文件的 "core.precondition-failed-code" 修改默认值.
     *
     * 默认 40010.
     */
    @JvmStatic
    public val preconditionFailedCode: Int by Env.getPropertyByLazy("core.precondition-failed-code", 40010)

    /**
     * 全局资源不存在返回码.
     *
     * 默认 40404.
     */
    @JvmStatic
    public val notFoundCode: Int = 40404

    /**
     * 全局资源不存在返回消息.
     *
     * 默认 "该对象不存在".
     */
    @JvmStatic
    public val notFoundMessage: String = "该对象不存在"
}
