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
import com.tony.fus.db.enums.PerformType
import com.tony.fus.exception.FusException
import com.tony.fus.model.enums.MultiApproveMode
import com.tony.mybatis.dao.BaseDao
import com.tony.mybatis.wrapper.TonyChainQuery
import com.tony.utils.throwIfEmpty
import com.tony.utils.throwIfNull
import com.tony.utils.throwIfNullOrEmpty
import com.tony.utils.throwIfTrue
import java.io.Serializable

/**
 * Utils is
 * @author tangli
 * @date 2023/10/26 19:50
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
@JvmSynthetic
internal inline fun Boolean.fusThrowIfTrue(
    message: String,
    code: Int = ApiProperty.preconditionFailedCode,
) {
    this.throwIfTrue(message, code, ::FusException)
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
 * @see throwIfTrue
 */
@JvmSynthetic
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
 * @author tangli
 * @date 2023/11/06 19:19
 * @since 1.0.0
 */
@JvmSynthetic
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
 * @author tangli
 * @date 2023/11/06 19:19
 * @since 1.0.0
 */
@JvmSynthetic
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
 * @author tangli
 * @date 2023/11/06 19:19
 * @since 1.0.0
 */
@JvmSynthetic
@JvmOverloads
public inline fun <C : CharSequence> C?.fusThrowIfNullOrEmpty(
    message: String = ApiProperty.notFoundMessage,
    code: Int = ApiProperty.notFoundCode,
): C =
    throwIfNullOrEmpty(message, code, ::FusException)

/**
 * 根据id查询，为null 抛出[FusException].
 * @param [id] id
 * @param [message] 消息
 * @param [code] 密码
 * @return [T]
 * @author tangli
 * @date 2023/09/13 19:38
 * @since 1.0.0
 */
@JvmSynthetic
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
 * @author tangli
 * @date 2023/10/23 19:50
 * @since 1.0.0
 */
@JvmSynthetic
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
 * @author tangli
 * @date 2023/11/06 19:19
 * @since 1.0.0
 */
@JvmSynthetic
internal inline fun <T : Any> TonyChainQuery<T>.fusListThrowIfEmpty(
    message: String = ApiProperty.notFoundMessage,
): List<T> =
    listThrowIfEmpty(message, ::FusException)

/**
 * 多人审批时审批方式 转换 参与类型
 * @return [PerformType]
 * @author tangli
 * @date 2023/11/14 19:38
 * @since 1.0.0
 */
@JvmSynthetic
public fun MultiApproveMode?.ofPerformType(): PerformType =
    if (this?.value == null) {
        PerformType.SORT
    } else {
        PerformType.create(value) ?: PerformType.SORT
    }
