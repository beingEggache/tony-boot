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

package tony.annotation.redis
/**
 * redis 相关注解
 *
 * @author tangli
 * @date 2023/05/24 19:10
 */

/**
 * redis缓存注解.
 *
 * 给常规的 @Cacheable 加了过期时间
 *
 * @author tangli
 * @date 2023/05/24 19:10
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
public annotation class RedisCacheable(
    /**
     * 缓存键, 支持简单字符串模板,和 expressions配合使用
     */
    val cacheKey: String,
    /**
     * 通过方法参数执行spel, 生成缓存键
     */
    val expressions: Array<String> = [],
    /**
     * 多久过期, 默认-3, 到今天结束
     */
    val expire: Long = TODAY_END,
) {
    public companion object {
        public const val TODAY_END: Long = -3L
    }
}

/**
 * redis 删除缓存注解.
 *
 * 可重复注解.
 *
 * @author tangli
 * @date 2023/05/24 19:11
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Repeatable
public annotation class RedisCacheEvict(
    val cacheKey: String,
    val expressions: Array<String> = [],
)
