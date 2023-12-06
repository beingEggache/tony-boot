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

package com.tony.redis.serializer.wrapper

/**
 * Redis protoStuff 序列化, 反序列化对象包装.
 *
 * 有 [com.tony.redis.RedisValues.increment]和 [com.tony.redis.RedisValues.get] [com.tony.redis.RedisValues.set] 需求的[Long], [Double] 类型不建议使用!
 * 因为 [com.tony.redis.RedisValues.increment]不会走 [org.springframework.data.redis.serializer.RedisSerializer.serialize].
 *
 * 但经过特殊处理, 所有 [Number] 类型都直接原生处理, 没走 [com.tony.redis.serializer.ProtostuffSerializer.serialize].
 * @author Tang Li
 * @date 2023/5/24 18:12
 */
internal class ProtoWrapper
    internal constructor(
        var data: Any? = null,
    )
