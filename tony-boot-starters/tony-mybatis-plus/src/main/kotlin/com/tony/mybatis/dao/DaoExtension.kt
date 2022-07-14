@file:JvmName("Daos")

/**
 * ktwl-boot-dependencies
 * daoExtension
 *
 * @author tangli
 * @since 2022/7/13 10:10
 */
package com.tony.mybatis.dao

import com.baomidou.mybatisplus.core.enums.SqlMethod
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper
import org.apache.ibatis.logging.Log
import org.apache.ibatis.logging.LogFactory
import org.apache.ibatis.session.SqlSession
import org.springframework.aop.framework.AopProxyUtils
import java.lang.reflect.Proxy
import java.util.function.BiConsumer

internal val ENTITY_CLASS_MAP = HashMap<Class<*>, Class<*>>()

internal val MAPPER_CLASS_MAP = HashMap<Class<*>, Class<*>>()

internal val LOG_MAP = HashMap<Class<*>, Log>()

/**
 * 获取mybatis-plus 语句
 *
 * @param sqlMethod 语句枚举
 * @return 语句
 */
internal fun <T : Any> BaseDao<T>.getSqlStatement(sqlMethod: SqlMethod?): String =
    SqlHelper.getSqlStatement(getMapperClass(), sqlMethod)

/**
 * 获取logger
 *
 * @return logger
 */
internal fun <T : Any> BaseDao<T>.getLog(): Log =
    LOG_MAP.getOrPut(javaClass) {
        LogFactory.getLog(javaClass)
    }

/**
 * 获取entityClass
 *
 * @return entityClass
 */
@Suppress("UNCHECKED_CAST")
internal fun <T : Any> BaseDao<T>.getEntityClass(): Class<T> =
    ENTITY_CLASS_MAP.getOrPut(this.javaClass) {
        ReflectionKit.getSuperClassGenericType(this.javaClass, BaseDao::class.java, 0)
    } as Class<T>

/**
 * 获取mapper类型
 *
 * @return mapper类型
 */
@Suppress("UNCHECKED_CAST")
internal fun <T : Any> BaseDao<T>.getMapperClass(): Class<out BaseDao<T>> =
    MAPPER_CLASS_MAP.getOrPut(javaClass) {
        if (!Proxy.isProxyClass(javaClass)) {
            javaClass
        } else AopProxyUtils.proxiedUserInterfaces(this).first()
    } as Class<out BaseDao<T>>

/**
 * 批量执行
 *
 * @param batchList 对象列表
 * @param consumer  回调
 * @param <E>       对象类型
 * @return 执行结果
 */
internal fun <T : Any, E> BaseDao<T>.executeBatch(
    batchList: Collection<E>,
    consumer: BiConsumer<SqlSession, E>
): Boolean =
    SqlHelper.executeBatch(getEntityClass(), getLog(), batchList, batchList.size, consumer)
