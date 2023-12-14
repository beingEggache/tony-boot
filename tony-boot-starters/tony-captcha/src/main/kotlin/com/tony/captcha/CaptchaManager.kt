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
 * @date 2023/05/25 19:38
 */
public object CaptchaManager {
    private val captchaService: CaptchaService by SpringContexts.getBeanByLazy()

    /**
     * 生成验证码
     * @param [type] 验证码类型, 可用来归类
     * @param [captchaKeyRule] 验证码key 生成规则
     * @param [timeout] 过期时间
     * @param [timeUnit] 时间单位, 默认为 [TimeUnit.SECONDS]
     * @return [CaptchaVo]
     * @author Tang Li
     * @date 2023/09/28 19:57
     * @since 1.0.0
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
     * @param [vo] 验证码对象
     * @param [message] 错误信息
     * @param [func] 回调
     * @return [R]
     * @author Tang Li
     * @date 2023/09/28 19:57
     * @since 1.0.0
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
