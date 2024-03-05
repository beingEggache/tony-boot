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

package com.tony.wechat.client.req

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * ## 获取稳定版接口调用凭据 请求参数
 * 微信稳定访问令牌req
 * @author tangli
 * @date 2023/09/25 19:49
 * @since 1.0.0
 */
public data class WechatStableAccessTokenReq(
    /**
     * 账号唯一凭证，即 AppID，可在「微信公众平台 - 设置 - 开发设置」页中获得.
     *
     * （需要已经成为开发者，且账号没有异常状态）
     */
    @JsonProperty("appid")
    public val appId: String?,
    /**
     * 账号唯一凭证密钥，即 AppSecret，获取方式同 appid
     */
    @JsonProperty("secret")
    public val secret: String?,
    /**
     * 默认使用 false.
     *
     * 1. force_refresh = false 时为普通调用模式，access_token 有效期内重复调用该接口不会更新 access_token;
     * 2. 当force_refresh = true 时为强制刷新模式，会导致上次获取的 access_token 失效，并返回新的 access_token
     */
    @JsonProperty("force_refresh")
    public val forceRefresh: Boolean = false,
    /**
     * 填写 client_credential
     */
    @JsonProperty("grant_type")
    public val grantType: String = "client_credential",
)
