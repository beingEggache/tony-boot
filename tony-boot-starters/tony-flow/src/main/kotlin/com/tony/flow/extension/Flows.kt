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

@file:JvmName("Flows")

package com.tony.flow.extension

import com.tony.ApiProperty
import com.tony.flow.exception.FlowException
import com.tony.mybatis.dao.BaseDao
import com.tony.mybatis.wrapper.TonyChainQuery
import com.tony.utils.throwIf
import com.tony.utils.throwIfEmpty
import com.tony.utils.throwIfNull
import com.tony.utils.throwIfNullOrEmpty
import java.io.Serializable

/**
 * Utils is
 * @author tangli
 * @date 2023/10/26 15:50
 * @since 1.0.0
 */
/**
 * 当 [condition] 为 true时, 抛出[FlowException].
 *
 * 异常信息为 [message], 默认为 [ApiProperty.notFoundMessage]
 *
 * 异常代码为 [code], 默认为 [ApiProperty.notFoundCode]
 * @param [message] 消息
 * @param [code] 密码
 * @author Tang Li
 * @date 2023/11/06 11:19
 * @since 1.0.0
 */
internal fun flowThrowIf(
    condition: Boolean,
    message: String,
    code: Int = ApiProperty.preconditionFailedCode,
) {
    throwIf(condition, message, code, ::FlowException)
}

/**
 * 当 [this]? 为 null时, 抛出[FlowException].
 *
 * 异常信息为 [message], 默认为 [ApiProperty.notFoundMessage]
 *
 * 异常代码为 [code], 默认为 [ApiProperty.notFoundCode]
 * @param [message] 消息
 * @param [code] 密码
 * @author Tang Li
 * @date 2023/11/06 11:19
 * @since 1.0.0
 */
@JvmOverloads
internal fun <T> T?.flowThrowIfNull(
    message: String = ApiProperty.notFoundMessage,
    code: Int = ApiProperty.notFoundCode,
): T =
    throwIfNull(message, code, ::FlowException)

/**
 * 当 [this]? 为 null 或者 空时, 抛出[FlowException].
 *
 * 异常信息为 [message], 默认为 [ApiProperty.notFoundMessage]
 *
 * 异常代码为 [code], 默认为 [ApiProperty.notFoundCode]
 * @param [message] 消息
 * @param [code] 密码
 * @return [C]
 * @author Tang Li
 * @date 2023/11/06 11:19
 * @since 1.0.0
 */
@JvmOverloads
internal fun <C : Collection<T>, T : Any?> C?.flowThrowIfEmpty(
    message: String = ApiProperty.notFoundMessage,
    code: Int = ApiProperty.notFoundCode,
): C =
    throwIfEmpty(message, code, ::FlowException)

/**
 * 当 [this]? 为 null 或者 空时, 抛出[FlowException].
 *
 * 异常信息为 [message], 默认为 [ApiProperty.notFoundMessage]
 *
 * 异常代码为 [code], 默认为 [ApiProperty.notFoundCode]
 * @param [message] 消息
 * @param [code] 密码
 * @return [C]
 * @author Tang Li
 * @date 2023/11/06 11:19
 * @since 1.0.0
 */
@JvmOverloads
internal fun <C : Map<*, *>> C?.flowThrowIfEmpty(
    message: String = ApiProperty.notFoundMessage,
    code: Int = ApiProperty.notFoundCode,
): C =
    throwIfEmpty(message, code, ::FlowException)

/**
 * 当 [this]? 为 null 或者 空时, 抛出[FlowException].
 *
 * 异常信息为 [message], 默认为 [ApiProperty.notFoundMessage]
 *
 * 异常代码为 [code], 默认为 [ApiProperty.notFoundCode]
 * @param [message] 消息
 * @param [code] 密码
 * @return [C]
 * @author Tang Li
 * @date 2023/11/06 11:19
 * @since 1.0.0
 */
@JvmOverloads
public fun <C : CharSequence> C?.flowThrowIfNullOrEmpty(
    message: String = ApiProperty.notFoundMessage,
    code: Int = ApiProperty.notFoundCode,
): C =
    this.throwIfNullOrEmpty(message, code, ::FlowException)

/**
 * 根据id查询，为null 抛出[FlowException].
 * @param [id] id
 * @return [T]
 * @author Tang Li
 * @date 2023/09/13 10:38
 * @since 1.0.0
 */
internal fun <T : Any> BaseDao<T>.flowSelectByIdNotNull(id: Serializable?): T =
    selectById(id).throwIfNull(ex = ::FlowException)

/**
 * 根据id查询，为null 抛出[FlowException].
 * @param [id] id
 * @param [message] 消息
 * @return [T]
 * @author Tang Li
 * @date 2023/09/13 10:38
 * @since 1.0.0
 */
internal fun <T : Any> BaseDao<T>.flowSelectByIdNotNull(
    id: Serializable?,
    message: String = ApiProperty.notFoundMessage,
): T =
    selectById(id).throwIfNull(message, ex = ::FlowException)

/**
 * 根据id查询，为null 抛出[FlowException].
 * @param [id] id
 * @param [message] 消息
 * @param [code] 密码
 * @return [T]
 * @author Tang Li
 * @date 2023/09/13 10:38
 * @since 1.0.0
 */
internal fun <T : Any> BaseDao<T>.flowSelectByIdNotNull(
    id: Serializable,
    message: String = ApiProperty.notFoundMessage,
    code: Int = ApiProperty.notFoundCode,
): T =
    selectById(id).throwIfNull(message, code, ex = ::FlowException)

/**
 * 查询单条记录.为 null 时抛出[FlowException].
 * @param [message] 默认为 [ApiProperty.notFoundMessage]
 * @return [T]
 * @author Tang Li
 * @date 2023/10/23 14:50
 * @since 1.0.0
 */
internal fun <T : Any> TonyChainQuery<T>.flowOneNotNull(message: String = ApiProperty.notFoundMessage): T =
    baseMapper
        .selectOne(wrapper)
        .throwIfNull(message, ApiProperty.notFoundCode, ex = ::FlowException)

/**
 * 当 [TonyChainQuery.list] 为 null 或者 空时, 抛出抛出[FlowException].
 *
 * 异常信息为 [message]
 * @param [message] 消息
 * @return [TonyChainQuery.list]
 * @author Tang Li
 * @date 2023/11/06 11:19
 * @since 1.0.0
 */
internal fun <T : Any> TonyChainQuery<T>.flowListThrowIfEmpty(message: String = ApiProperty.notFoundMessage): List<T> =
    listThrowIfEmpty(message, ::FlowException)
