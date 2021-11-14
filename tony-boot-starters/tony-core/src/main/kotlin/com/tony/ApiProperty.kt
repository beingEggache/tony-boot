package com.tony



import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

import javax.annotation.Resource

@Component
object ApiProperty {

    private lateinit var environment: Environment

    @Resource
    private fun environment(environment: Environment) {
        ApiProperty.environment = environment
    }

    @JvmStatic
    val successCode: Int by lazy {
        environment.getProperty("core.success-code", Int::class.java, 20000)
    }

    @JvmStatic
    val unauthorizedCode: Int by lazy {
        environment.getProperty("core.unauthorized-code", Int::class.java, 40100)
    }

    @JvmStatic
    val errorCode: Int by lazy {
        environment.getProperty("core.error-code", Int::class.java, 50000)
    }

    @JvmStatic
    val validationErrorCode: Int by lazy {
        environment.getProperty("core.validation-error-code", Int::class.java, 40000)
    }

    @JvmStatic
    val bizErrorCode: Int by lazy {
        environment.getProperty("core.biz-error-code", Int::class.java, 40010)
    }

    @JvmStatic
    val errorMsg: String by lazy {
        environment.getProperty("core.error-msg", "访客太多，请稍后重试")
    }

    @JvmStatic
    val validationErrorMsg: String by lazy {
        environment.getProperty("core.validation-error-msg", "参数有误，请检查后输入")
    }
}
