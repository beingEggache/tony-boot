/**
 * tony-boot-dependencies
 * CaptchaService
 *
 * @author tangli
 * @since 2022/7/12 11:39
 */
package com.tony.captcha

import com.fasterxml.jackson.annotation.JsonIgnore
import com.tony.Beans
import com.tony.cache.RedisManager
import com.tony.cache.RedisValues
import com.tony.utils.throwIf
import com.tony.utils.uuid
import com.wf.captcha.SpecCaptcha
import java.util.concurrent.TimeUnit

interface CaptchaService {
    /**
     * 验证
     * @param vo 验证码
     * @return 验证结果
     */
    fun verify(vo: CaptchaVo): Boolean
}

open class CaptchaVo(
    val captchaId: String,
    val captcha: String,
    val type: String,
    @JsonIgnore
    val captchaKeyRule: (CaptchaVo) -> String
)

class DefaultCaptchaServiceImpl : CaptchaService {
    override fun verify(vo: CaptchaVo): Boolean =
        RedisValues.hasKey(vo.captchaKeyRule(vo))
}

class NoopCaptchaServiceImpl : CaptchaService {
    override fun verify(vo: CaptchaVo): Boolean = true
}

object CaptchaManager {

    private val captchaService: CaptchaService by Beans.getBeanByLazy()

    @JvmStatic
    @JvmOverloads
    fun genCaptcha(
        type: String,
        captchaKeyRule: (CaptchaVo) -> String,
        timeout: Long,
        timeUnit: TimeUnit = TimeUnit.SECONDS
    ): CaptchaVo {
        val captchaId = uuid()
        val specCaptcha = SpecCaptcha(130, 30, 4)
        val captchaVo = CaptchaVo(captchaId, specCaptcha.toBase64(), type, captchaKeyRule)
        val captchaKey = captchaKeyRule.invoke(captchaVo)
        val text: String = specCaptcha.text()
        RedisValues.set(captchaKey, text, timeout, timeUnit)
        return captchaVo
    }

    @JvmStatic
    @JvmOverloads
    fun <R> verify(
        vo: CaptchaVo,
        message: String = "验证码错误",
        func: () -> R
    ): R {
        throwIf(!captchaService.verify(vo), message)
        val apply = func()
        RedisManager.delete(vo.captchaKeyRule(vo))
        return apply
    }
}
