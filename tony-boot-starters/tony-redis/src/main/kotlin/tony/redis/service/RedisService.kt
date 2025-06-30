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

package tony.redis.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JavaType
import tony.redis.serializer.SerializerMode
import tony.redis.toNum
import tony.utils.asTo
import tony.utils.isNumberTypes
import tony.utils.rawClass

/**
 * RedisService
 *
 * 统一定义 Redis 常用操作接口，支持 Value、Map、List 等多种数据结构。
 * 支持多种序列化方式（如 JACKSON、PROTOSTUFF），可通过 serializerMode 切换。
 *
 * 注意事项：
 * - 泛型类型推断仅适用于简单类型，复杂嵌套类型建议使用 TypeReference 或 JavaType。
 * - 类型转换、序列化/反序列化失败时会抛出异常，建议业务方捕获处理。
 * - 若需扩展自定义枚举类型，请实现 IntEnumValue/StringEnumValue 并提供对应 EnumCreator。
 * - 频繁序列化/反序列化建议复用 ObjectMapper 等工具，提升性能。
 *
 * @author tangli
 * @date 2023/06/09 19:30
 */
public interface RedisService :
    RedisValueOp,
    RedisMapOp,
    RedisListOp {
    /**
     * 当前 RedisService 使用的序列化/反序列化方式
     */
    public val serializerMode: SerializerMode
}

/**
 * RedisValueTransformer
 *
 * 提供 Redis 读写时的类型转换能力，兼容数字、字符串、枚举、对象等多种类型。
 *
 * 注意事项：
 * - outputTransformTo 支持 Class、JavaType、TypeReference 三种类型描述，建议复杂类型用 TypeReference。
 * - 类型转换失败时会抛出异常，建议业务方捕获处理。
 * - inputTransformTo 默认将对象序列化为字符串，数字类型原样返回。
 *
 * @author tangli
 * @date 2023/09/13 19:44
 */
public sealed interface RedisValueTransformer {
    /**
     * 输出转换为指定类型（Class）
     * @param type 目标类型
     * @return 转换后的对象，失败时抛出异常
     * @author tangli
     * @date 2023/09/13 19:44
     */
    public fun <T : Any> Any?.outputTransformTo(type: Class<T>): T? =
        if (type.isNumberTypes()) {
            toNum(type)
        } else {
            this?.asTo()
        }

    /**
     * 输出转换为指定类型（JavaType）
     * @param type 目标类型
     * @return 转换后的对象，失败时抛出异常
     * @see com.fasterxml.jackson.databind.JavaType
     * @author tangli
     * @date 2023/09/13 19:44
     */
    public fun <T : Any> Any?.outputTransformTo(type: JavaType): T? =
        outputTransformTo(type.rawClass())

    /**
     * 输出转换为指定类型（TypeReference）
     * @param type 目标类型
     * @return 转换后的对象，失败时抛出异常
     * @see com.fasterxml.jackson.core.type.TypeReference
     * @author tangli
     * @date 2023/09/13 19:44
     */
    public fun <T : Any> Any?.outputTransformTo(type: TypeReference<T>): T? =
        outputTransformTo(type.rawClass())

    /**
     * 输入转换为 Redis 可存储类型
     * @return 可序列化的对象，数字类型原样返回，其它类型序列化为字符串
     * @author tangli
     * @date 2023/09/13 19:44
     */
    public fun Any.inputTransformTo(): Any =
        this
}
