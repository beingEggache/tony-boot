package com.tony.captcha

import com.tony.SpringContexts
import com.tony.redis.RedisManager
import com.tony.redis.RedisValues
import com.tony.utils.throwIf
import com.tony.utils.uuid
import com.wf.captcha.SpecCaptcha
import java.util.concurrent.TimeUnit

/**
 * 验证码服务 单例类.
 *
 * @author Tang Li
 * @date 2023/5/25 15:38
 */
public object CaptchaManager {

    private val captchaService: CaptchaService by SpringContexts.getBeanByLazy()

    /**
     * 生成验证码
     *
     * @param type 验证码类型, 可用来归类
     * @param captchaKeyRule 验证码key 生成规则
     * @param timeout 过期时间
     * @param timeUnit 时间单位, 默认为 [TimeUnit.SECONDS]
     * @return
     */
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

    /**
     * 验证码校验.
     *
     * 验证码校验不通过会直接抛错.
     * 成功会返回 回调 [func] 结果.
     * @param R 回调结果类型
     * @param vo 验证码对象
     * @param message 错误信息
     * @param func 回调
     * @return
     */
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
