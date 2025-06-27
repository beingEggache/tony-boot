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

package tony.captcha

/**
 * 验证码服务相关
 *
 * @author tangli
 * @date 2022/07/12 19:39
 */
import com.fasterxml.jackson.annotation.JsonIgnore
import tony.redis.RedisManager

/**
 * 验证码 服务
 * @author tangli
 * @date 2023/09/28 19:58
 */
public fun interface CaptchaService {
    /**
     * 验证
     * @param [vo] 验证码
     * @return [Boolean]
     * @author tangli
     * @date 2023/09/28 19:58
     * @since 1.0.0
     */
    public fun verify(vo: CaptchaVo): Boolean
}

/**
 * 验证码 vo
 *
 * @author tangli
 * @date 2023/05/25 19:35
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
 * @author tangli
 * @date 2023/09/28 19:58
 */
public class DefaultCaptchaServiceImpl : CaptchaService {
    override fun verify(vo: CaptchaVo): Boolean =
        RedisManager.hasKey(vo.captchaKeyRule(vo))
}

/**
 * 验证码服务空实现.
 * @author tangli
 * @date 2023/09/28 19:58
 */
public class NoopCaptchaServiceImpl : CaptchaService {
    override fun verify(vo: CaptchaVo): Boolean =
        true
}
