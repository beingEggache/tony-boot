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

package com.tony.web

import com.tony.SpringContexts.getBeanByLazy

/**
 * WebContext 扩展.
 *
 * 增加了获取用户标识和session对象的方法.
 *
 * @author Tang Li
 * @date 2023/05/25 19:18
 */
public object WebContextExtensions {
    @JvmStatic
    private val webSession: WebSession by getBeanByLazy()

    /**
     * 用户标识
     */
    @get:JvmSynthetic
    @JvmStatic
    public val WebContext.userId: String
        get() =
            webSession.userId

    /**
     * Api session
     */
    @Suppress("UnusedReceiverParameter")
    @get:JvmSynthetic
    @JvmStatic
    public val WebContext.webSession: WebSession
        get() =
            WebContextExtensions.webSession

    /**
     * webSession
     * @return webSession
     */
    @JvmStatic
    public fun apiSession(): WebSession =
        webSession

    /**
     * 用户标识
     * @return 用户标识
     */
    @JvmStatic
    public fun userId(): String =
        webSession.userId
}
