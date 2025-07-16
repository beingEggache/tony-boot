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

package tony.web.auth.interceptor

/**
 * 登录校验拦截器 等.
 * @author tangli
 * @date 2020-11-04 13:33
 */
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import tony.annotation.web.auth.NoLoginCheck
import tony.core.utils.hasAnnotation
import tony.web.WebContext
import tony.web.auth.WebContextExtensions.webSession

/**
 * 登录校验拦截器.
 *
 * @author tangli
 * @date 2023/05/25 19:14
 */
public interface LoginCheckInterceptor : HandlerInterceptor {
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
    ): Boolean {
        if (handler !is HandlerMethod) return true
        if (handler.beanType.hasAnnotation(NoLoginCheck::class.java)) return true
        if (handler.method.hasAnnotation(NoLoginCheck::class.java)) return true
        throw (WebContext.webSession.unauthorizedException ?: return true)
    }
}

/**
 * 登录校验拦截器. 默认实现.
 *
 * @author tangli
 * @date 2023/05/25 19:15
 */
internal class DefaultLoginCheckInterceptor : LoginCheckInterceptor
