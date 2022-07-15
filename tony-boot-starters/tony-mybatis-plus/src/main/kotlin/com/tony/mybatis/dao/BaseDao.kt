/**
 * BaseDao
 *
 * @author tangli
 * @since 2022/7/12 15:28
 */
package com.tony.mybatis.dao

import com.baomidou.mybatisplus.core.enums.SqlMethod
import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper
import com.baomidou.mybatisplus.core.toolkit.Assert
import com.baomidou.mybatisplus.core.toolkit.Constants
import com.baomidou.mybatisplus.core.toolkit.StringUtils
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper
import com.baomidou.mybatisplus.extension.kotlin.KtQueryChainWrapper
import com.baomidou.mybatisplus.extension.kotlin.KtUpdateChainWrapper
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers
import org.apache.ibatis.binding.MapperMethod.ParamMap
import org.springframework.transaction.annotation.Transactional

@Suppress("unused")
interface BaseDao<T : Any> : BaseMapper<T> {

    /**
     * 批量插入
     *
     * @param batchList 实体列表
     * @return 执行结果
     */
    @Transactional(rollbackFor = [Throwable::class])
    fun insertBatch(batchList: Collection<T>): Boolean = executeBatch(batchList) { sqlSession, entity ->
        sqlSession.insert(getSqlStatement(SqlMethod.INSERT_ONE), entity)
    }

    /**
     * 批量保存更新
     *
     * @param batchList 实体列表
     * @return 执行结果
     */
    @Transactional(rollbackFor = [Throwable::class])
    fun upsertBatch(batchList: Collection<T>): Boolean {
        val entityClass = getEntityClass()
        val tableInfo = TableInfoHelper.getTableInfo(entityClass)
        Assert.notNull(tableInfo, "error: can not execute. because can not find cache of TableInfo for entity!")
        val keyProperty = tableInfo.keyProperty
        Assert.notEmpty(keyProperty, "error: can not execute. because can not find column for id from entity!")
        val sqlStatement = getSqlStatement(SqlMethod.INSERT_ONE)
        return executeBatch(batchList) { sqlSession, entity ->
            val isInsert =
                StringUtils.checkValNull(tableInfo.getPropertyValue(entity, keyProperty)) || sqlSession.selectList<T>(
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
     *
     * @param batchList 实体列表
     * @return 执行结果
     */
    @Transactional(rollbackFor = [Throwable::class])
    fun updateByIdBatch(batchList: Collection<T>): Boolean = executeBatch(batchList) { sqlSession, entity ->
        val param = ParamMap<T>()
        param[Constants.ENTITY] = entity
        sqlSession.update(getSqlStatement(SqlMethod.UPDATE_BY_ID), param)
    }

    /**
     * 批量删除,如果使用了逻辑删除，就是批量逻辑删除.
     *
     * @param batchList 实体列表
     * @return 执行结果
     */
    @Transactional(rollbackFor = [Throwable::class])
    fun deleteByIdBatch(batchList: Collection<T>): Boolean {
        val sqlStatement = getSqlStatement(SqlMethod.DELETE_BY_ID)
        val entityClass = getEntityClass()
        val tableInfo = TableInfoHelper.getTableInfo(entityClass)
        return executeBatch(batchList) { sqlSession, e ->
            if (tableInfo.isWithUpdateFill && tableInfo.isWithLogicDelete) {
                if (entityClass.isAssignableFrom(e.javaClass)) {
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
     * 以下的方法使用介绍:
     *
     * 一. 名称介绍
     * 1. 方法名带有 query 的为对数据的查询操作, 方法名带有 update 的为对数据的修改操作
     * 2. 方法名带有 lambda 的为内部方法入参 column 支持函数式的
     * 二. 支持介绍
     *
     * 1. 方法名带有 query 的支持以 [ChainQuery] 内部的方法名结尾进行数据查询操作
     * 2. 方法名带有 update 的支持以 [ChainUpdate] 内部的方法名为结尾进行数据修改操作
     *
     * 三. 使用示例,只用不带 lambda 的方法各展示一个例子,其他类推
     * 1. 根据条件获取一条数据: `query().eq("column", value).one()`
     * 2. 根据条件删除一条数据: `update().eq("column", value).remove()`
     *
     */
    /**
     * 链式查询 普通
     *
     * @return QueryWrapper 的包装类
     */
    fun query(): QueryChainWrapper<T> = ChainWrappers.queryChain(this)

    /**
     * 链式查询 lambda 式
     *
     * 注意：不支持 Kotlin
     *
     * @return LambdaQueryWrapper 的包装类
     */
    fun lambdaQuery(): LambdaQueryChainWrapper<T> = ChainWrappers.lambdaQueryChain(this)

    /**
     * 链式查询 lambda 式
     * kotlin 使用
     *
     * @return KtQueryWrapper 的包装类
     */
    fun ktQuery(): KtQueryChainWrapper<T> = ChainWrappers.ktQueryChain(this, getEntityClass())

    /**
     * 链式查询 lambda 式
     * kotlin 使用
     *
     * @return KtQueryWrapper 的包装类
     */
    fun ktUpdate(): KtUpdateChainWrapper<T> = ChainWrappers.ktUpdateChain(this, getEntityClass())

    /**
     * 链式更改 普通
     *
     * @return UpdateWrapper 的包装类
     */
    fun update(): UpdateChainWrapper<T> = ChainWrappers.updateChain(this)

    /**
     * 链式更改 lambda 式
     * <p>注意：不支持 Kotlin </p>
     *
     * @return LambdaUpdateWrapper 的包装类
     */
    fun lambdaUpdate(): LambdaUpdateChainWrapper<T> = ChainWrappers.lambdaUpdateChain(this)
}
