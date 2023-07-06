@file:JvmName("Daos")

/**
 * daoExtension
 *
 * @author tangli
 * @since 2022/7/13 10:10
 */

package com.tony.mybatis.dao

import com.baomidou.mybatisplus.core.enums.SqlMethod
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper
import com.tony.SpringContexts
import com.tony.utils.asToNotNull
import com.tony.utils.isTypesOrSubTypesOf
import com.tony.utils.rawClass
import com.tony.utils.typeParamOfSuperInterface
import org.apache.ibatis.logging.Log
import org.apache.ibatis.logging.LogFactory
import org.apache.ibatis.session.SqlSession
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.lang.reflect.Proxy
import java.util.concurrent.ConcurrentHashMap
import java.util.function.BiConsumer

internal val ENTITY_CLASS_MAP = ConcurrentHashMap<Class<*>, Class<*>>()

internal val MAPPER_CLASS_MAP = ConcurrentHashMap<Class<*>, Class<*>>()

internal val LOG_MAP = ConcurrentHashMap<Class<*>, Log>()

public val namedParameterJdbcTemplate: NamedParameterJdbcTemplate by SpringContexts.getBeanByLazy()

internal fun <T : Any> BaseDao<T>.actualClass() =
    if (!Proxy.isProxyClass(this::class.java)) {
        this::class.java
    } else {
        this::class.java
            .genericInterfaces
            .first {
                it.rawClass()
                    .isTypesOrSubTypesOf(BaseDao::class.java)
            }
            .rawClass()
    }

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
    LOG_MAP.getOrPut(this::class.java) {
        LogFactory.getLog(actualClass())
    }

/**
 * 获取entityClass
 *
 * @return entityClass
 */
internal fun <T : Any> BaseDao<T>.getEntityClass(): Class<T> =
    ENTITY_CLASS_MAP.getOrPut(this::class.java) {
        actualClass().typeParamOfSuperInterface(BaseDao::class.java).rawClass()
    }.asToNotNull()

/**
 * 获取mapper类型
 *
 * @return mapper类型
 */
internal fun <T : Any> BaseDao<T>.getMapperClass(): Class<out BaseDao<T>> =
    MAPPER_CLASS_MAP.getOrPut(this::class.java) {
        actualClass()
    }.asToNotNull()

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
    consumer: BiConsumer<SqlSession, E>,
): Boolean =
    SqlHelper.executeBatch(getEntityClass(), getLog(), batchList, batchList.size + 1, consumer)

@JvmSynthetic
public inline fun <reified T> queryObject(sql: String, params: Map<String, Any?> = mapOf()): List<T> =
    namedParameterJdbcTemplate.query(sql, params, BeanPropertyRowMapper(T::class.java))

@JvmOverloads
public fun <T> queryObject(sql: String, clazz: Class<T>, params: Map<String, Any?> = mapOf()): MutableList<T> =
    namedParameterJdbcTemplate.query(sql, params, BeanPropertyRowMapper(clazz))
