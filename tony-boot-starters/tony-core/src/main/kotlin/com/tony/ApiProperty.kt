/*
 * MIT License
 *
 * Copyright (c) 2023-present, tangli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.tony

import com.tony.SpringContexts.Env
import com.tony.utils.getLogger

/**
 * 核心常量.
 *
 * 比如成功返回码, 失败返回码等.
 *
 * 可读取配置.
 * @author Tang Li
 * @date 2022/09/29 19:20
 */
public data object ApiProperty {
    private inline fun <reified T : Any> getPropertyAvoidInitError(
        key: String,
        default: T,
    ): T =
        try {
            Env.getProperty(key, T::class.java, default)
        } catch (e: Throwable) {
            getLogger("com.tony.utils.Funcs").warn(e.message ?: e.cause?.message)
            default
        }

    /**
     * 全局成功返回码.
     *
     * 可由配置文件的 "core.ok-code" 修改默认值.
     *
     * 默认 20000.
     */
    @get:JvmName("okCode")
    @JvmStatic
    public val okCode: Int
        get() = getPropertyAvoidInitError("core.ok-code", 20000)

    /**
     * 全局成功返回消息.
     *
     * 可由配置文件的 "core.ok-msg" 修改默认值.
     *
     * 默认 "操作成功".
     */
    @get:JvmName("defaultOkMessage")
    @JvmStatic
    public val defaultOkMessage: String
        get() = getPropertyAvoidInitError("core.ok-msg", "操作成功")

    /**
     * 全局未认证返回码.
     *
     * 可由配置文件的 "core.unauthorized-code" 修改默认值.
     *
     * 默认 40100.
     */
    @get:JvmName("unauthorizedCode")
    @JvmStatic
    public val unauthorizedCode: Int
        get() = getPropertyAvoidInitError("core.unauthorized-code", 40100)

    /**
     * 全局错误返回码.
     *
     * 可由配置文件的 "core.internal-server-error-code" 修改默认值.
     *
     * 默认 50000.
     */
    @get:JvmName("errorCode")
    @JvmStatic
    public val errorCode: Int
        get() = getPropertyAvoidInitError("core.internal-server-error-code", 50000)

    /**
     * 全局错误返回消息.
     *
     * 可由配置文件的 "core.internal-server-error-msg" 修改默认值.
     *
     * 默认 "访客太多，请稍后重试".
     */
    @get:JvmName("errorMsg")
    @JvmStatic
    public val errorMsg: String
        get() = getPropertyAvoidInitError("core.internal-server-error-msg", "访客太多，请稍后重试")

    /**
     * 全局错误请求码. 一般是请求不合法, 比如 http 方法不对应等.
     *
     * 可由配置文件的 "core.bad-request-code" 修改默认值.
     *
     * 默认 40000.
     */
    @get:JvmName("badRequestCode")
    @JvmStatic
    public val badRequestCode: Int
        get() = getPropertyAvoidInitError("core.bad-request-code", 40000)

    /**
     * 全局错误请求返回消息. 一般是请求不合法, 比如 http 方法不对应等.
     *
     * 可由配置文件的 "core.bad-request-msg" 修改默认值.
     *
     * 默认 "请求有误，请检查后输入".
     */
    @get:JvmName("badRequestMsg")
    @JvmStatic
    public val badRequestMsg: String
        get() = getPropertyAvoidInitError("core.bad-request-msg", "请求有误，请检查后输入")

    /**
     * 全局校验不通过返回码.
     *
     * 可由配置文件的 "core.precondition-failed-code" 修改默认值.
     *
     * 默认 40010.
     */
    @get:JvmName("preconditionFailedCode")
    @JvmStatic
    public val preconditionFailedCode: Int
        get() = getPropertyAvoidInitError("core.precondition-failed-code", 40010)

    /**
     * 全局资源不存在返回码.
     *
     * 默认 40404.
     */
    @get:JvmName("notFoundCode")
    @JvmStatic
    public val notFoundCode: Int = 40404

    /**
     * 全局资源不存在返回消息.
     *
     * 默认 "该对象不存在".
     */
    @get:JvmName("notFoundMessage")
    @JvmStatic
    public val notFoundMessage: String = "该对象不存在"
}
