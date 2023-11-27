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
import com.baomidou.mybatisplus.core.enums.SqlMethod
import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.baomidou.mybatisplus.core.metadata.TableInfo
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper
import com.baomidou.mybatisplus.core.toolkit.Constants
import com.baomidou.mybatisplus.core.toolkit.StringUtils
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper
import com.baomidou.mybatisplus.extension.kotlin.KtUpdateChainWrapper
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers
import com.tony.ApiProperty
import com.tony.PageQueryLike
import com.tony.PageResultLike
import com.tony.exception.BaseException
import com.tony.exception.BizException
import com.tony.mybatis.wrapper.TonyKtQueryChainWrapper
import com.tony.mybatis.wrapper.TonyLambdaQueryChainWrapper
import com.tony.mybatis.wrapper.TonyQueryChainWrapper
import com.tony.utils.isTypesOrSubTypesOf
import com.tony.utils.throwIfEmpty
import com.tony.utils.throwIfNull
import com.tony.utils.toPage
import com.tony.utils.toPageResult
import java.io.Serializable
import java.util.Objects
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.binding.MapperMethod.ParamMap
import org.springframework.transaction.annotation.Transactional

/**
 * mybatis plus [BaseMapper] 包装, 增加了一些方法.
 * @author Tang Li
 * @date 2023/09/13 10:37
 * @since 1.0.0
 */
public interface BaseDao<T : Any> : BaseMapper<T> {
    /**
     * 根据id查询，为null 将会抛错
     * @param [id] id
     * @return [T]
     * @author Tang Li
     * @date 2023/09/13 10:38
     * @since 1.0.0
     */
    public fun selectByIdNotNull(id: Serializable): T =
        selectById(id).throwIfNull()

    /**
     * 根据id查询，为null 将会抛错
     * @param [id] id
     * @param [message] 消息
     * @return [T]
     * @author Tang Li
     * @date 2023/09/13 10:38
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
     * @author Tang Li
     * @date 2023/09/13 10:38
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
     * @author Tang Li
     * @date 2023/09/13 10:38
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
     * @author Tang Li
     * @date 2023/09/13 10:38
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
     * @author Tang Li
     * @date 2023/11/06 14:03
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
     * @return [List]List<E>
     * @author Tang Li
     * @date 2023/11/06 14:12
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
     * @return [List] List<Map<String, Any?>>
     * @author Tang Li
     * @date 2023/11/06 14:12
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
     * TableId 注解存在更新记录，否插入一条记录
     * @param [entity] 实体对象
     * @return [Boolean]
     * @author Tang Li
     * @date 2023/09/13 10:38
     * @since 1.0.0
     */
    public fun upsert(entity: T?): Boolean {
        if (null != entity) {
            val tableInfo: TableInfo =
                TableInfoHelper
                    .getTableInfo(getEntityClass())
                    .throwIfNull("error: can not execute. because can not find cache of TableInfo for entity!")
            val keyProperty =
                tableInfo
                    .keyProperty
                    .throwIfNull("error: can not execute. because can not find column for id from entity!")
            val idVal = tableInfo.getPropertyValue(entity, keyProperty)
            return if (StringUtils.checkValNull(idVal) || Objects.isNull(selectById(idVal as Serializable))) {
                insert(entity) > 0
            } else {
                updateById(entity) > 0
            }
        }
        return false
    }

    /**
     * 批量插入
     * @param [batchList] 实体列表
     * @return [Boolean]
     * @author Tang Li
     * @date 2023/09/13 10:38
     * @since 1.0.0
     */
    @Transactional(rollbackFor = [Throwable::class])
    public fun insertBatch(batchList: Collection<T>): Boolean =
        executeBatch(batchList) { sqlSession, entity ->
            sqlSession.insert(getSqlStatement(SqlMethod.INSERT_ONE), entity)
        }

    /**
     * 批量保存更新
     * @param [batchList] 实体列表
     * @return [Boolean]
     * @author Tang Li
     * @date 2023/09/13 10:38
     * @since 1.0.0
     */
    @Transactional(rollbackFor = [Throwable::class])
    public fun upsertBatch(batchList: Collection<T>): Boolean {
        val entityClass = getEntityClass()
        val tableInfo =
            TableInfoHelper
                .getTableInfo(entityClass)
                .throwIfNull("error: can not execute. because can not find cache of TableInfo for entity!")
        val keyProperty =
            tableInfo
                .keyProperty
                .throwIfNull("error: can not execute. because can not find column for id from entity!")
        val sqlStatement = getSqlStatement(SqlMethod.INSERT_ONE)
        return executeBatch(batchList) { sqlSession, entity ->
            val isInsert =
                StringUtils.checkValNull(tableInfo.getPropertyValue(entity, keyProperty)) ||
                    sqlSession
                        .selectList<T>(
                            getSqlStatement(SqlMethod.SELECT_BY_ID),
                            entity
                        ).isNullOrEmpty()
            if (isInsert) {
                sqlSession.insert(sqlStatement, entity)
            } else {
                val param = ParamMap<T>()
                param[Constants.ENTITY] = entity
                sqlSession.update(getSqlStatement(SqlMethod.UPDATE_BY_ID), param)
            }
        }
    }

    /**
     * 批量更新
     * @param [batchList] 实体列表
     * @return [Boolean]
     * @author Tang Li
     * @date 2023/09/13 10:38
     * @since 1.0.0
     */
    @Transactional(rollbackFor = [Throwable::class])
    public fun updateByIdBatch(batchList: Collection<T>): Boolean =
        executeBatch(batchList) { sqlSession, entity ->
            val param = ParamMap<T>()
            param[Constants.ENTITY] = entity
            sqlSession.update(getSqlStatement(SqlMethod.UPDATE_BY_ID), param)
        }

    /**
     * 批量删除,如果使用了逻辑删除，就是批量逻辑删除.
     * @param [batchList] 实体列表
     * @return [Boolean]
     * @author Tang Li
     * @date 2023/09/13 10:38
     * @since 1.0.0
     */
    @Transactional(rollbackFor = [Throwable::class])
    public fun deleteByIdBatch(batchList: Collection<T>): Boolean {
        val sqlStatement = getSqlStatement(SqlMethod.DELETE_BY_ID)
        val entityClass = getEntityClass()
        val tableInfo = TableInfoHelper.getTableInfo(entityClass)
        return executeBatch(batchList) { sqlSession, e ->
            if (tableInfo.isWithUpdateFill && tableInfo.isWithLogicDelete) {
                if (e.isTypesOrSubTypesOf(entityClass)) {
                    sqlSession.update(sqlStatement, e)
                } else {
                    val instance = tableInfo.newInstance<T>()
                    tableInfo.setPropertyValue(instance, tableInfo.keyProperty, e)
                    sqlSession.update(sqlStatement, instance)
                }
            } else {
                sqlSession.update(sqlStatement, e)
            }
        }
    }

    /**
     * 分页查询出全局统一结构.
     * @param [page] 页
     * @param [queryWrapper] 查询包装器
     * @return [E]
     * @author Tang Li
     * @date 2023/09/13 10:39
     * @since 1.0.0
     */
    public fun <E : PageResultLike<T>> selectPageResult(
        page: PageQueryLike<*>,
        @Param(Constants.WRAPPER) queryWrapper: Wrapper<T>?,
    ): E =
        selectPage(page.toPage(), queryWrapper).toPageResult()

    /**
     * 分页查询出全局统一结构.
     * @param [page] 页
     * @param [queryWrapper] 查询包装器
     * @return [E]
     * @author Tang Li
     * @date 2023/10/23 14:42
     * @since 1.0.0
     */
    public fun <E : PageResultLike<Map<String, Any?>>> selectMapPageResult(
        page: PageQueryLike<*>,
        @Param(Constants.WRAPPER) queryWrapper: Wrapper<T>?,
    ): E =
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
     * @return [TonyQueryChainWrapper]
     * @author Tang Li
     * @date 2023/09/13 10:39
     * @since 1.0.0
     */
    public fun query(): TonyQueryChainWrapper<T> =
        TonyQueryChainWrapper(this)

    /**
     * 链式查询 lambda 式
     * <p>注意：不支持 Kotlin </p>
     * @return [TonyLambdaQueryChainWrapper]
     * @author Tang Li
     * @date 2023/09/13 10:39
     * @since 1.0.0
     */
    public fun lambdaQuery(): TonyLambdaQueryChainWrapper<T> =
        TonyLambdaQueryChainWrapper(this)

    /**
     * 链式查询 普通.
     * 仅支持 Kotlin.
     * @return [TonyKtQueryChainWrapper]
     * @author Tang Li
     * @date 2023/09/13 10:39
     * @since 1.0.0
     */
    @JvmSynthetic
    public fun ktQuery(): TonyKtQueryChainWrapper<T> =
        TonyKtQueryChainWrapper(this)

    /**
     * 链式更改 lambda 式.
     * 仅支持 Kotlin.
     * @return [KtUpdateChainWrapper]
     * @author Tang Li
     * @date 2023/09/13 10:39
     * @since 1.0.0
     */
    @JvmSynthetic
    public fun ktUpdate(): KtUpdateChainWrapper<T> =
        ChainWrappers.ktUpdateChain(this, getEntityClass())

    /**
     * 链式更改 普通.
     * @return [UpdateChainWrapper]
     * @author Tang Li
     * @date 2023/09/13 10:40
     * @since 1.0.0
     */
    public fun update(): UpdateChainWrapper<T> =
        ChainWrappers.updateChain(this)

    /**
     * 链式更改 lambda 式.
     * <p>注意：不支持 Kotlin </p>
     * @return [LambdaUpdateChainWrapper]
     * @author Tang Li
     * @date 2023/09/13 10:40
     * @since 1.0.0
     */
    public fun lambdaUpdate(): LambdaUpdateChainWrapper<T> =
        ChainWrappers.lambdaUpdateChain(this)
}
