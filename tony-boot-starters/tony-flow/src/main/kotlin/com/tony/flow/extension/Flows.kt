@file:JvmName("Flows")

package com.tony.flow.extension

import com.tony.ApiProperty
import com.tony.flow.exception.FlowException
import com.tony.mybatis.dao.BaseDao
import com.tony.mybatis.wrapper.TonyChainQuery
import com.tony.utils.throwIf
import com.tony.utils.throwIfEmpty
import com.tony.utils.throwIfNull
import java.io.Serializable

/**
 * Utils is
 * @author tangli
 * @date 2023/10/26 15:50
 * @since 1.0.0
 */
internal fun flowThrowIf(
    condition: Boolean,
    message: String,
    code: Int = ApiProperty.preconditionFailedCode,
) {
    throwIf(condition, message, code, ::FlowException)
}

@JvmOverloads
internal fun <T> T?.flowThrowIfNull(
    message: String = ApiProperty.notFoundMessage,
    code: Int = ApiProperty.notFoundCode,
): T {
    throwIf(this == null, message, code, ::FlowException)
    return this!!
}

@JvmOverloads
internal fun <C : Collection<T>, T : Any?> C?.flowThrowIfEmpty(
    message: String = ApiProperty.notFoundMessage,
    code: Int = ApiProperty.notFoundCode,
): C =
    throwIfEmpty(message, code, ::FlowException)

/**
 * 根据id查询，为null 将会抛错
 * @param [id] id
 * @return [T]
 * @author Tang Li
 * @date 2023/09/13 10:38
 * @since 1.0.0
 */
internal fun <T : Any> BaseDao<T>.flowSelectByIdNotNull(id: Serializable): T =
    selectById(id).throwIfNull(ex = ::FlowException)

/**
 * 根据id查询，为null 将会抛错
 * @param [id] id
 * @param [message] 消息
 * @return [T]
 * @author Tang Li
 * @date 2023/09/13 10:38
 * @since 1.0.0
 */
internal fun <T : Any> BaseDao<T>.flowSelectByIdNotNull(
    id: Serializable,
    message: String = ApiProperty.notFoundMessage,
): T =
    selectById(id).throwIfNull(message, ex = ::FlowException)

/**
 * 根据id查询，为null 将会抛错
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
 * 查询单条记录.为 null 时抛错.
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
 * 当 [TonyChainQuery.list] 为 null 或者 空时, 抛出异常.
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
