package com.tony.captcha

/**
 * tony-boot-dependencies
 * CaptchaService
 *
 * @author tangli
 * @since 2022/7/12 11:39
 */
import com.fasterxml.jackson.annotation.JsonIgnore
import com.tony.cache.RedisValues

/**
 * 验证码 服务
 *
 * @author tangli
 * @since 2023/5/25 15:35
 */
public interface CaptchaService {
    /**
     * 验证
     * @param vo 验证码
     * @return 验证结果
     */
    public fun verify(vo: CaptchaVo): Boolean
}

/**
 * 验证码 vo
 *
 * @author tangli
 * @since 2023/5/25 15:35
 * @param captchaId
 * @param captcha
 * @param captchaKeyRule captcha key 获取方式.
 */
public open class CaptchaVo(
    public val captchaId: String,
    public val captcha: String,
    public val type: String,
    @JsonIgnore
    public val captchaKeyRule: (CaptchaVo) -> String,
)

/**
 * 验证码服务默认实现
 *
 * @author tangli
 * @since 2023/5/25 15:37
 */
public class DefaultCaptchaServiceImpl : CaptchaService {
    override fun verify(vo: CaptchaVo): Boolean =
        RedisValues.hasKey(vo.captchaKeyRule(vo))
}

/**
 * 验证码服务空实现.
 *
 * @author tangli
 * @since 2023/5/25 15:37
 */
public class NoopCaptchaServiceImpl : CaptchaService {
    override fun verify(vo: CaptchaVo): Boolean = true
}
