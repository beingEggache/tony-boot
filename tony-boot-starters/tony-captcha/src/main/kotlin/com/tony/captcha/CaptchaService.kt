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

public interface CaptchaService {
    /**
     * 验证
     * @param vo 验证码
     * @return 验证结果
     */
    public fun verify(vo: CaptchaVo): Boolean
}

public open class CaptchaVo(
    public val captchaId: String,
    public val captcha: String,
    public val type: String,
    @JsonIgnore
    public val captchaKeyRule: (CaptchaVo) -> String,
)

public class DefaultCaptchaServiceImpl : CaptchaService {
    override fun verify(vo: CaptchaVo): Boolean =
        RedisValues.hasKey(vo.captchaKeyRule(vo))
}

public class NoopCaptchaServiceImpl : CaptchaService {
    override fun verify(vo: CaptchaVo): Boolean = true
}

public object CaptchaManager {

    private val captchaService: CaptchaService by Beans.getBeanByLazy()

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
