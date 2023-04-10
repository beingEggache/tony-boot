package com.tony.captcha

import com.tony.SpringContexts
import com.tony.cache.RedisManager
import com.tony.cache.RedisValues
import com.tony.utils.throwIf
import com.tony.utils.uuid
import com.wf.captcha.SpecCaptcha
import java.util.concurrent.TimeUnit

public object CaptchaManager {

    private val captchaService: CaptchaService by SpringContexts.getBeanByLazy()

    @JvmStatic
    @JvmOverloads
    public fun genCaptcha(
        type: String,
        captchaKeyRule: (CaptchaVo) -> String,
        timeout: Long,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
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
    public fun <R> verify(
        vo: CaptchaVo,
        message: String = "验证码错误",
        func: () -> R,
    ): R {
        throwIf(!captchaService.verify(vo), message)
        val apply = func()
        RedisManager.delete(vo.captchaKeyRule(vo))
        return apply
    }
}
