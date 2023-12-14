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

package com.tony.test.wechat

import com.tony.feign.config.FeignConfig
import okhttp3.Interceptor
import okhttp3.Response
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
class TestWechatConfig1 {
    @Bean
    fun testInterceptor1() = TestInterceptor1()
}

@Configuration
@Import(FeignConfig::class)
class TestWechatConfig2 {

    @Bean
    fun testInterceptor2() = TestInterceptor2()

}

class TestInterceptor1 : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        println(1)
        val request = chain.request()
        val request1 = request.newBuilder().headers(request.headers.newBuilder().add("1", "b").build()).build()
        return chain.proceed(request1)
    }
}

class TestInterceptor2 : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        println(2)
        val request = chain.request()
        val request1 = request.newBuilder().headers(request.headers.newBuilder().add("2", "b").build()).build()
        return chain.proceed(request1)
    }
}
