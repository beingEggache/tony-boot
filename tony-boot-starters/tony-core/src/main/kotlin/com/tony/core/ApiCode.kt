package com.tony.core

import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
import javax.annotation.Resource

@Component
object ApiCode {

    private lateinit var environment: Environment

    @Resource
    private fun environment(environment: Environment) {
        ApiCode.environment = environment
    }

    val successCode: Int by lazy {
        environment.getProperty("core.success-code", Int::class.java, 20000)
    }

    val unauthorizedCode: Int by lazy {
        environment.getProperty("core.unauthorized-code", Int::class.java, 40100)
    }

    val errorCode: Int by lazy {
        environment.getProperty("core.error-code", Int::class.java, 50000)
    }

    val validationErrorCode: Int by lazy {
        environment.getProperty("core.validation-error-code", Int::class.java, 40000)
    }

    val bizErrorCode: Int by lazy {
        environment.getProperty("core.biz-error-code", Int::class.java, 40010)
    }

    val errorMsg: String by lazy {
        environment.getProperty("core.error-msg", "访客太多，请稍后重试")
    }

    val validationErrorMsg: String by lazy {
        environment.getProperty("core.validation-error-msg", "参数有误，请检查后输入")
    }
}
