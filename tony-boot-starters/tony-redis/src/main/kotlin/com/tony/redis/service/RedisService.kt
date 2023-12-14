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

package com.tony.redis.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JavaType
import com.tony.redis.serializer.SerializerMode
import com.tony.redis.toNum
import com.tony.utils.asTo
import com.tony.utils.isNumberTypes
import com.tony.utils.rawClass

/**
 * RedisService is
 * @author Tang Li
 * @date 2023/06/09 19:30
 */
public interface RedisService :
    RedisValueOp,
    RedisMapOp,
    RedisListOp {
    /**
     * 序列化反序列化方式
     */
    public val serializerMode: SerializerMode
}

public sealed interface RedisValueTransformer {
    /**
     * 输出转换为
     * @param [type] 类型
     * @return [T]?
     * @author Tang Li
     * @date 2023/09/13 19:44
     * @since 1.0.0
     */
    public fun <T : Any> Any?.outputTransformTo(type: Class<T>): T? {
        if (type.isNumberTypes()) {
            return toNum(type)
        }
        return this?.asTo()
    }

    /**
     * 输出转换为
     * @param [type] 类型
     * @return [T]
     * @author Tang Li
     * @date 2023/09/13 19:44
     * @since 1.0.0
     */
    public fun <T : Any> Any?.outputTransformTo(type: JavaType): T? =
        outputTransformTo(type.rawClass())

    /**
     * 输出转换为
     * @param [type] 类型
     * @return [T]
     * @author Tang Li
     * @date 2023/09/13 19:44
     * @since 1.0.0
     */
    public fun <T : Any> Any?.outputTransformTo(type: TypeReference<T>): T? =
        outputTransformTo(type.rawClass())

    /**
     * 输入转换为
     * @return [Any]
     * @author Tang Li
     * @date 2023/09/13 19:44
     * @since 1.0.0
     */
    public fun Any.inputTransformTo(): Any =
        this
}
