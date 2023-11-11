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

@file:JvmName("Fuses")
@file:Suppress("NOTHING_TO_INLINE")

package com.tony.fus.extension

import com.tony.ApiProperty
import com.tony.fus.exception.FusException
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
 * 当[condition]为真时,抛出[FusException]异常.
 *
 * 异常信息为[message], 异常代码为[code],默认为[ApiProperty.preconditionFailedCode]
 *
 * @param condition 异常条件
 * @param message 异常信息
 * @param code 异常代码
 */
internal inline fun fusThrowIf(
    condition: Boolean,
    message: String,
    code: Int = ApiProperty.preconditionFailedCode,
) {
    throwIf(condition, message, code, ::FusException)
}

/**
 * 当 [this] [T]? 为 null 时, 抛出[FusException]异常.
 *
 * 异常信息为 [message], 默认为 [ApiProperty.notFoundMessage]
 *
 * 异常代码为 [code], 默认为 [ApiProperty.notFoundCode]
 *
 * @receiver [T]?
 * @param [T] 自身类型
 * @param message 异常信息
 * @param code 异常代码
 * @return [T] this
 *
 * @see throwIf
 */
@JvmOverloads
internal inline fun <T> T?.fusThrowIfNull(
    message: String = ApiProperty.notFoundMessage,
    code: Int = ApiProperty.notFoundCode,
): T =
    throwIfNull(message, code, ::FusException)

/**
 * 当 [this]? 为 null 或者 空时, 抛出[FusException].
 *
 * 异常信息为 [message], 默认为 [ApiProperty.notFoundMessage]
 *
 * 异常代码为 [code], 默认为 [ApiProperty.notFoundCode]
 * @param [message] 异常信息
 * @param [code] 异常代码
 * @return [C]
 * @author Tang Li
 * @date 2023/11/06 11:19
 * @since 1.0.0
 */
@JvmOverloads
internal inline fun <C : Collection<T>, T : Any?> C?.fusThrowIfEmpty(
    message: String = ApiProperty.notFoundMessage,
    code: Int = ApiProperty.notFoundCode,
): C =
    throwIfEmpty(message, code, ::FusException)

/**
 * 当 [this]? 为 null 或者 空时, 抛出[FusException].
 *
 * 异常信息为 [message], 默认为 [ApiProperty.notFoundMessage]
 *
 * 异常代码为 [code], 默认为 [ApiProperty.notFoundCode]
 * @receiver [C]
 * @param [C] Map类型
 * @param [message] 异常信息
 * @param [code] 异常代码
 * @return [C]
 * @author Tang Li
 * @date 2023/11/06 11:19
 * @since 1.0.0
 */
@JvmOverloads
internal inline fun <C : Map<*, *>> C?.fusThrowIfEmpty(
    message: String = ApiProperty.notFoundMessage,
    code: Int = ApiProperty.notFoundCode,
): C =
    throwIfEmpty(message, code, ::FusException)

/**
 * 当 [this]? 为 null 或者 空时, 抛出[FusException]异常.
 *
 * 异常信息为 [message], 默认为 [ApiProperty.notFoundMessage]
 *
 * 异常代码为 [code], 默认为 [ApiProperty.notFoundCode]
 * @receiver [C]
 * @param [C] 字符串类型
 * @param [message] 异常信息
 * @param [code] 异常代码
 * @return [C]
 * @author Tang Li
 * @date 2023/11/06 11:19
 * @since 1.0.0
 */
@JvmOverloads
public inline fun <C : CharSequence> C?.fusThrowIfNullOrEmpty(
    message: String = ApiProperty.notFoundMessage,
    code: Int = ApiProperty.notFoundCode,
): C =
    throwIfNullOrEmpty(message, code, ::FusException)

/**
 * 根据id查询，为null 抛出[FusException].
 * @param [id] id
 * @return [T]
 * @author Tang Li
 * @date 2023/09/13 10:38
 * @since 1.0.0
 */
internal inline fun <T : Any> BaseDao<T>.fusSelectByIdNotNull(id: Serializable?): T =
    selectById(id).throwIfNull(ex = ::FusException)

/**
 * 根据id查询，为null 抛出[FusException].
 * @param [id] id
 * @param [message] 消息
 * @return [T]
 * @author Tang Li
 * @date 2023/09/13 10:38
 * @since 1.0.0
 */
internal inline fun <T : Any> BaseDao<T>.fusSelectByIdNotNull(
    id: Serializable?,
    message: String = ApiProperty.notFoundMessage,
): T =
    selectById(id).throwIfNull(message, ex = ::FusException)

/**
 * 根据id查询，为null 抛出[FusException].
 * @param [id] id
 * @param [message] 消息
 * @param [code] 密码
 * @return [T]
 * @author Tang Li
 * @date 2023/09/13 10:38
 * @since 1.0.0
 */
internal inline fun <T : Any> BaseDao<T>.fusSelectByIdNotNull(
    id: Serializable,
    message: String = ApiProperty.notFoundMessage,
    code: Int = ApiProperty.notFoundCode,
): T =
    selectById(id).throwIfNull(message, code, ex = ::FusException)

/**
 * 查询单条记录.为 null 时抛出[FusException].
 * @param [message] 默认为 [ApiProperty.notFoundMessage]
 * @return [T]
 * @author Tang Li
 * @date 2023/10/23 14:50
 * @since 1.0.0
 */
internal inline fun <T : Any> TonyChainQuery<T>.fusOneNotNull(message: String = ApiProperty.notFoundMessage): T =
    baseMapper
        .selectOne(wrapper)
        .throwIfNull(message, ApiProperty.notFoundCode, ex = ::FusException)

/**
 * 当 [TonyChainQuery.list] 为 null 或者 空时, 抛出抛出[FusException].
 *
 * 异常信息为 [message]
 * @param [message] 消息
 * @return [TonyChainQuery.list]
 * @author Tang Li
 * @date 2023/11/06 11:19
 * @since 1.0.0
 */
internal inline fun <T : Any> TonyChainQuery<T>.fusListThrowIfEmpty(
    message: String = ApiProperty.notFoundMessage,
): List<T> =
    listThrowIfEmpty(message, ::FusException)
