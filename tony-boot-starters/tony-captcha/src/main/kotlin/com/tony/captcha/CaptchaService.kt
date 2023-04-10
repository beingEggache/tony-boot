/**
 * tony-boot-dependencies
 * CaptchaService
 *
 * @author tangli
 * @since 2022/7/12 11:39
 */
package com.tony.captcha

import com.fasterxml.jackson.annotation.JsonIgnore
import com.tony.cache.RedisValues

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
