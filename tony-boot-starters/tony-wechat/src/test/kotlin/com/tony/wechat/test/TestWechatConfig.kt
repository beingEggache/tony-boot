package com.tony.wechat.test

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
