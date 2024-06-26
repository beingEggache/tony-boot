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

package com.tony.mybatis.dao

import com.baomidou.mybatisplus.core.conditions.Wrapper
import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.baomidou.mybatisplus.core.toolkit.Constants
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper
import com.baomidou.mybatisplus.extension.kotlin.KtUpdateChainWrapper
import com.tony.ApiProperty
import com.tony.PageQueryLike
import com.tony.PageResultLike
import com.tony.exception.BaseException
import com.tony.exception.BizException
import com.tony.mybatis.wrapper.TonyKtQueryChainWrapper
import com.tony.mybatis.wrapper.TonyKtUpdateChainWrapper
import com.tony.mybatis.wrapper.TonyLambdaQueryChainWrapper
import com.tony.mybatis.wrapper.TonyQueryChainWrapper
import com.tony.utils.throwIfEmpty
import com.tony.utils.throwIfNull
import com.tony.utils.toPage
import com.tony.utils.toPageResult
import java.io.Serializable
import org.apache.ibatis.annotations.Param

/**
 * mybatis plus [BaseMapper] 包装, 增加了一些方法.
 * @author tangli
 * @date 2023/09/13 19:37
 * @since 1.0.0
 */
public interface BaseDao<T : Any> : BaseMapper<T> {
    /**
     * 物理删除
     * @param [queryWrapper] 查询条件
     * @return [Int] 影响行数
     * @author tangli
     * @date 2024/06/26 13:13
     * @since 1.0.0
     */
    public fun physicalDelete(
        @Param(Constants.WRAPPER) queryWrapper: Wrapper<T>,
    ): Int

    /**
     * 按id进行物理删除
     * @param [id] id
     * @return [Int] 影响行数
     * @author tangli
     * @date 2024/06/26 13:17
     * @since 1.0.0
     */
    public fun physicalDeleteById(id: Serializable): Int

    /**
     * 按ID进行物理删除
     * @param [idList] id列表
     * @return [Int] 影响行数
     * @author tangli
     * @date 2024/06/26 13:18
     * @since 1.0.0
     */
    public fun physicalDeleteByIds(
        @Param(Constants.COLL) idList: Collection<*>,
    ): Int

    /**
     * 根据id查询，为null 将会抛错
     * @param [id] id
     * @return [T]
     * @author tangli
     * @date 2023/09/13 19:38
     * @since 1.0.0
     */
    public fun selectByIdNotNull(id: Serializable): T =
        selectById(id).throwIfNull()

    /**
     * 根据id查询，为null 将会抛错
     * @param [id] id
     * @param [message] 消息
     * @return [T]
     * @author tangli
     * @date 2023/09/13 19:38
     * @since 1.0.0
     */
    public fun selectByIdNotNull(
        id: Serializable,
        message: String = ApiProperty.notFoundMessage,
    ): T =
        selectById(id).throwIfNull(message)

    /**
     * 根据id查询，为null 将会抛错
     * @param [id] id
     * @param [message] 消息
     * @param [ex] 异常类型
     * @return [T]
     * @author tangli
     * @date 2023/09/13 19:38
     * @since 1.0.0
     */
    public fun selectByIdNotNull(
        id: Serializable,
        message: String = ApiProperty.notFoundMessage,
        ex: (message: String, code: Int) -> BaseException,
    ): T =
        selectById(id).throwIfNull(message, ex = ex)

    /**
     * 根据id查询，为null 将会抛错
     * @param [id] id
     * @param [message] 消息
     * @param [code] 密码
     * @return [T]
     * @author tangli
     * @date 2023/09/13 19:38
     * @since 1.0.0
     */
    public fun selectByIdNotNull(
        id: Serializable,
        message: String = ApiProperty.notFoundMessage,
        code: Int = ApiProperty.notFoundCode,
    ): T =
        selectById(id).throwIfNull(message, code)

    /**
     * 根据id查询，为null 将会抛错
     * @param [id] id
     * @param [message] 消息
     * @param [code] 密码
     * @param [ex] 异常类型
     * @return [T]
     * @author tangli
     * @date 2023/09/13 19:38
     * @since 1.0.0
     */
    public fun selectByIdNotNull(
        id: Serializable,
        message: String = ApiProperty.notFoundMessage,
        code: Int = ApiProperty.notFoundCode,
        ex: (message: String, code: Int) -> BaseException,
    ): T =
        selectById(id).throwIfNull(message, code, ex)

    /**
     * 根据 entity 条件，查询一条记录.
     *
     * @param [queryWrapper] 查询包装器
     * @param [message] 消息
     * @param [code] 密码
     * @param [ex] ex
     * @return [T]
     * @author tangli
     * @date 2023/11/06 19:03
     * @since 1.0.0
     */
    public fun selectOneNotNull(
        @Param(Constants.WRAPPER) queryWrapper: Wrapper<T>,
        message: String = ApiProperty.notFoundMessage,
        code: Int = ApiProperty.notFoundCode,
        ex: (message: String, code: Int) -> BaseException = ::BizException,
    ): T =
        selectOne(queryWrapper).throwIfNull(message, code, ex)

    /**
     * 根据 entity 条件，查询全部记录
     *
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     */
    public fun selectListThrowIfEmpty(
        @Param(Constants.WRAPPER) queryWrapper: Wrapper<T>,
        message: String = ApiProperty.notFoundMessage,
        code: Int = ApiProperty.notFoundCode,
        ex: (message: String, code: Int) -> BaseException = ::BizException,
    ): List<T> =
        selectList(queryWrapper).throwIfEmpty(message, code, ex)

    /**
     * 根据 entity 条件，查询一条记录.
     *
     * 注意： 只返回第一个字段的值.
     * @param [queryWrapper] 查询包装器
     * @param [message] 消息
     * @param [code] 密码
     * @param [ex] ex
     * @return [List]<[E]>
     * @author tangli
     * @date 2023/11/06 19:12
     * @since 1.0.0
     */
    public fun <E> selectObjsThrowIfEmpty(
        @Param(Constants.WRAPPER) queryWrapper: Wrapper<T>,
        message: String = ApiProperty.notFoundMessage,
        code: Int = ApiProperty.notFoundCode,
        ex: (message: String, code: Int) -> BaseException = ::BizException,
    ): List<E> =
        selectObjs<E>(queryWrapper).throwIfEmpty(message, code, ex)

    /**
     * 根据 entity 条件，查询所有记录.
     *
     * 注意： 只返回第一个字段的值.
     * @param [queryWrapper] 查询包装器
     * @param [message] 消息
     * @param [code] 密码
     * @param [ex] ex
     * @return [List]<[Map]<[String], [Any]?>>
     * @author tangli
     * @date 2023/11/06 19:12
     * @since 1.0.0
     */
    public fun selectMapsThrowIfEmpty(
        @Param(Constants.WRAPPER) queryWrapper: Wrapper<T>,
        message: String = ApiProperty.notFoundMessage,
        code: Int = ApiProperty.notFoundCode,
        ex: (message: String, code: Int) -> BaseException = ::BizException,
    ): List<Map<String, Any?>> =
        selectMaps(queryWrapper).throwIfEmpty(message, code, ex)

    /**
     * 分页查询出全局统一结构.
     * @param [page] 页
     * @param [queryWrapper] 查询包装器
     * @return [PageResultLike]<[T]>
     * @author tangli
     * @date 2023/09/13 19:39
     * @since 1.0.0
     */
    public fun selectPageResult(
        page: PageQueryLike<*>,
        @Param(Constants.WRAPPER) queryWrapper: Wrapper<T>?,
    ): PageResultLike<T> =
        selectPage(page.toPage(), queryWrapper).toPageResult()

    /**
     * 分页查询出全局统一结构.
     * @param [page] 页
     * @param [queryWrapper] 查询包装器
     * @return [PageResultLike]<[Map]<[String], [Any]?>>
     * @author tangli
     * @date 2023/10/23 19:42
     * @since 1.0.0
     */
    public fun selectMapPageResult(
        page: PageQueryLike<*>,
        @Param(Constants.WRAPPER) queryWrapper: Wrapper<T>?,
    ): PageResultLike<Map<String, Any?>> =
        selectMapsPage(page.toPage(), queryWrapper).toPageResult()

    /**
     * 以下的方法使用介绍:
     *
     * 一. 名称介绍
     * 1. 方法名带有 query 的为对数据的查询操作, 方法名带有 update 的为对数据的修改操作
     * 2. 方法名带有 lambda 的为内部方法入参 column 支持函数式的
     * 二. 支持介绍
     *
     * 1. 方法名带有 query 的支持以 [com.baomidou.mybatisplus.extension.conditions.query.ChainQuery] 内部的方法名结尾进行数据查询操作
     * 2. 方法名带有 update 的支持以 [com.baomidou.mybatisplus.extension.conditions.update.ChainUpdate] 内部的方法名为结尾进行数据修改操作
     *
     * 三. 使用示例,只用不带 lambda 的方法各展示一个例子,其他类推
     * 1. 根据条件获取一条数据: `query().eq("column", value).one()`
     * 2. 根据条件删除一条数据: `update().eq("column", value).remove()`
     *
     */

    /**
     * 链式查询 普通
     * @return [TonyQueryChainWrapper]<[T]>
     * @author tangli
     * @date 2023/09/13 19:39
     * @since 1.0.0
     */
    public fun query(): TonyQueryChainWrapper<T> =
        TonyQueryChainWrapper(this)

    /**
     * 链式查询 lambda 式
     * <p>注意：不支持 Kotlin </p>
     * @return [TonyLambdaQueryChainWrapper]<[T]>
     * @author tangli
     * @date 2023/09/13 19:39
     * @since 1.0.0
     */
    public fun lambdaQuery(): TonyLambdaQueryChainWrapper<T> =
        TonyLambdaQueryChainWrapper(this)

    /**
     * 链式查询 普通.
     * 仅支持 Kotlin.
     * @return [TonyKtQueryChainWrapper]<[T]>
     * @author tangli
     * @date 2023/09/13 19:39
     * @since 1.0.0
     */
    @JvmSynthetic
    public fun ktQuery(): TonyKtQueryChainWrapper<T> =
        TonyKtQueryChainWrapper(this)

    /**
     * 链式更改 lambda 式.
     * 仅支持 Kotlin.
     * @return [KtUpdateChainWrapper]<[T]>
     * @author tangli
     * @date 2023/09/13 19:39
     * @since 1.0.0
     */
    @JvmSynthetic
    public fun ktUpdate(): TonyKtUpdateChainWrapper<T> =
        TonyKtUpdateChainWrapper(this, getEntityClass())

    /**
     * 链式更改 普通.
     * @return [UpdateChainWrapper]<[T]>
     * @author tangli
     * @date 2023/09/13 19:40
     * @since 1.0.0
     */
    public fun update(): UpdateChainWrapper<T> =
        UpdateChainWrapper(this)

    /**
     * 链式更改 lambda 式.
     * <p>注意：不支持 Kotlin </p>
     * @return [LambdaUpdateChainWrapper]<[T]>
     * @author tangli
     * @date 2023/09/13 19:40
     * @since 1.0.0
     */
    public fun lambdaUpdate(): LambdaUpdateChainWrapper<T> =
        LambdaUpdateChainWrapper(this)
}
