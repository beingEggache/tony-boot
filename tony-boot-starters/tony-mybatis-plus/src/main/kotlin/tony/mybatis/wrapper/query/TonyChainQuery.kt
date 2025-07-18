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

package tony.mybatis.wrapper.query

import com.baomidou.mybatisplus.extension.conditions.query.ChainQuery
import java.util.Collections
import org.apache.ibatis.exceptions.TooManyResultsException
import tony.core.ApiProperty
import tony.core.exception.BaseException
import tony.core.model.PageQueryLike
import tony.core.model.PageResultLike
import tony.core.utils.throwIfFalse
import tony.core.utils.throwIfNull
import tony.core.utils.throwIfTrue
import tony.mybatis.dao.BaseDao
import tony.mybatis.dao.getEntityClass

/**
 * 链式查询.
 * @param [T] 实体类型
 * @author tangli
 * @date 2023/08/11 19:18
 */
public interface TonyChainQuery<T : Any> : ChainQuery<T> {
    override fun getBaseMapper(): BaseDao<T>

    /**
     * 查询单条记录.为 null 时抛错.
     * @return [T]
     * @author tangli
     * @date 2023/10/23 19:50
     */
    public fun oneNotNull(): T =
        baseMapper.selectOneNotNull(wrapper)

    /**
     * 查询单条记录.为 null 时抛错.
     * @param [message] 默认为 [ApiProperty.notFoundMessage]
     * @return [T]
     * @author tangli
     * @date 2023/10/23 19:50
     */
    public fun oneNotNull(message: String): T =
        baseMapper.selectOneNotNull(wrapper, message)

    /**
     * 查询单条记录.为 null 时抛错.
     * @param [message] 默认为 [ApiProperty.notFoundMessage]
     * @return [T]
     * @author tangli
     * @date 2023/10/23 19:50
     */
    public fun oneNotNull(
        message: String,
        ex: (message: String, code: Int) -> BaseException,
    ): T =
        baseMapper.selectOneNotNull(wrapper, message, ex = ex)

    /**
     * 查询某个条件是否存在, 存在就抛错.
     * @param [message] 消息
     * @author tangli
     * @date 2023/10/23 19:49
     */
    public fun throwIfExists(message: String) {
        exists().throwIfTrue(message, ApiProperty.notFoundCode)
    }

    /**
     * 查询某个条件是否存在, 存在就抛错.
     * @param [message] 消息
     * @param [ex] 异常类型
     * @author tangli
     * @date 2023/10/23 19:49
     */
    public fun throwIfExists(
        message: String,
        ex: (message: String, code: Int) -> BaseException,
    ) {
        exists().throwIfTrue(message, ApiProperty.notFoundCode)
    }

    /**
     * 查询某个条件是否不存在, 不存在就抛错.
     * @param [message] 消息
     * @author tangli
     * @date 2023/10/23 19:49
     */
    public fun throwIfNotExists(message: String) {
        exists().throwIfFalse(message, ApiProperty.notFoundCode)
    }

    /**
     * 查询某个条件是否不存在, 不存在就抛错.
     * @param [message] 消息
     * @param [ex] 异常类型
     * @author tangli
     * @date 2023/10/23 19:49
     */
    public fun throwIfNotExists(
        message: String,
        ex: (message: String, code: Int) -> BaseException,
    ) {
        exists().throwIfFalse(message, ApiProperty.notFoundCode)
    }

    /**
     * 列表查询, 再根据[transformer] 对 [T] 转换成 [R]
     * @param [transformer] 转换器
     * @return [List]<[R]>
     * @author tangli
     * @date 2023/11/22 19:23
     */
    public fun <R> list(transformer: java.util.function.Function<T, R>): List<R> {
        var list: MutableList<R> = Collections.emptyList()
        var initList = false
        baseMapper.selectList(wrapper) {
            if (!initList) {
                list = ArrayList(it.resultCount)
                initList = true
            }
            list.add(transformer.apply(it.resultObject))
        }
        return list
    }

    /**
     * 当 [list] 为 null 或者 空时, 抛出异常.
     *
     * @return [List]<[T]>
     * @author tangli
     * @date 2023/11/06 19:19
     */
    public fun listThrowIfEmpty(): List<T> =
        baseMapper.selectListThrowIfEmpty(wrapper)

    /**
     * 当 [list] 为 null 或者 空时, 抛出异常.
     *
     * 异常信息为 [message]
     * @param [message] 消息
     * @return [List]<[T]>
     * @author tangli
     * @date 2023/11/06 19:19
     */
    public fun listThrowIfEmpty(message: String): List<T> =
        baseMapper.selectListThrowIfEmpty(wrapper, message)

    /**
     * 当 [list] 为 null 或者 空时, 抛出异常.
     *
     * 异常信息为 [message]
     * @param [message] 消息
     * @param [ex] 异常类型
     * @return [List]<[T]>
     * @author tangli
     * @date 2023/11/06 19:19
     */
    public fun listThrowIfEmpty(
        message: String,
        ex: (message: String, code: Int) -> BaseException,
    ): List<T> =
        baseMapper.selectListThrowIfEmpty(wrapper, message, ex = ex)

    /**
     * 分页查询出全局统一结构.
     * @param [page] 全局统一请求分页结构.
     * @return [PageResultLike]<[T]>
     * @author tangli
     * @date 2023/10/23 19:49
     */
    public fun pageResult(page: PageQueryLike<*>): PageResultLike<T> =
        baseMapper.selectPageResult(page, wrapper)

    /**
     * 查询全局统一分页结构.
     * @param [page] 全局统一请求分页结构.
     * @return [PageResultLike]<[Map]<[String], [Any]?>>
     * @author tangli
     * @date 2023/10/23 19:49
     */
    public fun mapPageResult(page: PageQueryLike<*>): PageResultLike<Map<String, Any?>> =
        baseMapper.selectMapPageResult(page, wrapper)

    /**
     * 查询全部记录.
     *
     * 注意： 只返回第一个字段的值.
     * @return [List]<[E]?>
     * @author tangli
     * @date 2023/10/23 19:48
     */
    public fun <E> listObj(): List<E?> =
        baseMapper.selectObjs<E>(wrapper)

    /**
     * 当 [listObj] 为 null 或者 空时, 抛出异常.
     *
     * 注意： 只返回第一个字段的值.
     * @return [List]<[E]?>
     * @author tangli
     * @date 2023/10/23 19:48
     */
    public fun <E> listObjThrowIfEmpty(): List<E?> =
        baseMapper.selectObjsThrowIfEmpty(wrapper)

    /**
     * 当 [listObj] 为 null 或者 空时, 抛出异常.
     *
     * 注意： 只返回第一个字段的值.
     * @param [message] 消息
     * @return [List]<[E]?>
     * @author tangli
     * @date 2023/10/23 19:48
     */
    public fun <E> listObjThrowIfEmpty(message: String): List<E?> =
        baseMapper.selectObjsThrowIfEmpty(wrapper, message)

    /**
     * 当 [listObj] 为 null 或者 空时, 抛出异常.
     *
     * 注意： 只返回第一个字段的值.
     * @param [message] 消息
     * @param [ex] 异常类型
     * @return [List]<[E]?>
     * @author tangli
     * @date 2023/10/23 19:48
     */
    public fun <E> listObjThrowIfEmpty(
        message: String,
        ex: (message: String, code: Int) -> BaseException,
    ): List<E?> =
        baseMapper.selectObjsThrowIfEmpty(wrapper, message, ex = ex)

    /**
     * 查询单条记录.
     *
     * 查询一条记录，限制取一条记录, 注意：多条数据会报异常.
     *
     * 注意： 只返回第一个字段的值.
     * @return [E]?
     * @author tangli
     * @date 2023/10/23 19:48
     */
    public fun <E> oneObj(): E? {
        val list: List<E?> = listObj()
        // 抄自 DefaultSqlSession#selectOne
        return if (list.size == 1) {
            list[0]
        } else if (list.size > 1) {
            throw TooManyResultsException(
                "Expected one result (or null) to be returned by oneObj(), but found: ${list.size}"
            )
        } else {
            null
        }
    }

    /**
     * 查询单条记录.为 null 时抛错.
     * @return [E]
     * @author tangli
     * @date 2023/10/23 19:47
     */
    public fun <E> oneObjNotNull(): E =
        oneObj<E>().throwIfNull(ApiProperty.notFoundMessage, ApiProperty.notFoundCode)

    /**
     * 查询单条记录.为 null 时抛错.
     * @param [message] 消息
     * @return [E]
     * @author tangli
     * @date 2023/10/23 19:46
     */
    public fun <E> oneObjNotNull(message: String): E =
        oneObj<E>().throwIfNull(message, ApiProperty.notFoundCode)

    /**
     * 查询单条记录.为 null 时抛错.
     * @param [message] 消息
     * @param [ex] 异常类型
     * @return [E]
     * @author tangli
     * @date 2023/10/23 19:46
     */
    public fun <E> oneObjNotNull(
        message: String,
        ex: (message: String, code: Int) -> BaseException,
    ): E =
        oneObj<E>().throwIfNull(message, ApiProperty.notFoundCode, ex = ex)

    /**
     * 查询全部记录.
     * @return [List]<[Map]<[String], [Any]?>>
     * @author tangli
     * @date 2023/10/23 19:50
     */
    public fun listMap(): List<Map<String, Any?>> =
        baseMapper.selectMaps(wrapper)

    /**
     * 当 [listMap] 为 null 或者 空时, 抛出异常.
     *
     * @return [List]<[Map]<[String], [Any]?>>
     * @author tangli
     * @date 2023/10/23 19:50
     */
    public fun listMapThrowIfEmpty(): List<Map<String, Any?>> =
        baseMapper.selectMapsThrowIfEmpty(wrapper)

    /**
     * 当 [listMap] 为 null 或者 空时, 抛出异常.
     * @param [message] 消息
     * @return [List]<[Map]<[String], [Any]?>>
     * @author tangli
     * @date 2023/10/23 19:50
     */
    public fun listMapThrowIfEmpty(message: String): List<Map<String, Any?>> =
        baseMapper.selectMapsThrowIfEmpty(wrapper, message)

    /**
     * 当 [listMap] 为 null 或者 空时, 抛出异常.
     * @param [message] 消息
     * @param [ex] 异常类型
     * @return [List]<[Map]<[String], [Any]?>>
     * @author tangli
     * @date 2023/10/23 19:50
     */
    public fun listMapThrowIfEmpty(
        message: String,
        ex: (message: String, code: Int) -> BaseException,
    ): List<Map<String, Any?>> =
        baseMapper.selectMapsThrowIfEmpty(wrapper, message, ex = ex)

    /**
     * 查询单条记录.
     * 查询一条记录，限制取一条记录, 注意：多条数据会报异常.
     * @return [Map]<[String], [Any]?>?
     * @author tangli
     * @date 2023/10/23 19:51
     */
    public fun oneMap(): Map<String, Any?>? {
        val list = listMap()
        // 抄自 DefaultSqlSession#selectOne
        return if (list.size == 1) {
            list[0]
        } else if (list.size > 1) {
            throw TooManyResultsException(
                "Expected one result (or null) to be returned by oneObj(), but found: ${list.size}"
            )
        } else {
            null
        }
    }

    /**
     * one map 不为空
     * @return [Map]<[String], [Any]?>
     * @author tangli
     * @date 2023/09/13 19:41
     */
    public fun oneMapNotNull(): Map<String, Any?> =
        oneMap().throwIfNull(ApiProperty.notFoundMessage, ApiProperty.notFoundCode)

    /**
     * one map 不为空
     * @param [message] 消息
     * @return [Map]<[String], [Any]>
     * @author tangli
     * @date 2023/09/13 19:40
     */
    public fun oneMapNotNull(message: String): Map<String, Any?> =
        oneMap().throwIfNull(message, ApiProperty.notFoundCode)

    /**
     * one map 不为空
     * @param [message] 消息
     * @param [ex] 异常类型
     * @return [Map]<[String], [Any]>
     * @author tangli
     * @date 2023/09/13 19:40
     */
    public fun oneMapNotNull(
        message: String,
        ex: (message: String, code: Int) -> BaseException,
    ): Map<String, Any?> =
        oneMap().throwIfNull(message, ApiProperty.notFoundCode, ex = ex)

    override fun getEntityClass(): Class<T> =
        baseMapper.getEntityClass()
}
