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

@file:JvmName("Daos")

/**
 * daoExtension
 *
 * @author Tang Li
 * @date 2022/7/13 10:10
 */

package com.tony.mybatis.dao

import com.baomidou.mybatisplus.core.enums.SqlMethod
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper
import com.tony.SpringContexts
import com.tony.utils.asToNotNull
import com.tony.utils.isTypesOrSubTypesOf
import com.tony.utils.rawClass
import com.tony.utils.typeParamOfSuperInterface
import java.lang.reflect.Proxy
import java.util.concurrent.ConcurrentHashMap
import java.util.function.BiConsumer
import org.apache.ibatis.logging.Log
import org.apache.ibatis.logging.LogFactory
import org.apache.ibatis.session.SqlSession
import org.apache.ibatis.session.SqlSessionFactory

internal val ENTITY_CLASS_MAP = ConcurrentHashMap<Class<*>, Class<*>>()

internal val MAPPER_CLASS_MAP = ConcurrentHashMap<Class<*>, Class<*>>()

internal val LOG_MAP = ConcurrentHashMap<Class<*>, Log>()

/**
 * 实际类别
 * @author Tang Li
 * @date 2023/09/28 10:54
 * @since 1.0.0
 */
internal fun <T : Any> BaseDao<T>.actualClass() =
    if (!Proxy.isProxyClass(this::class.java)) {
        this::class.java
    } else {
        this::class.java
            .genericInterfaces
            .first {
                it
                    .rawClass()
                    .isTypesOrSubTypesOf(BaseDao::class.java)
            }.rawClass()
    }

/**
 * 获取mybatis-plus 语句
 * @param [sqlMethod] 语句枚举
 * @return [String]
 * @author Tang Li
 * @date 2023/09/28 10:52
 * @since 1.0.0
 */
internal fun <T : Any> BaseDao<T>.getSqlStatement(sqlMethod: SqlMethod?): String =
    SqlHelper.getSqlStatement(getMapperClass(), sqlMethod)

/**
 * 获取logger
 * @return [Log]
 * @author Tang Li
 * @date 2023/09/28 10:52
 * @since 1.0.0
 */
internal fun <T : Any> BaseDao<T>.getLog(): Log =
    LOG_MAP.getOrPut(this::class.java) {
        LogFactory.getLog(actualClass())
    }

/**
 * 获取entityClass
 * @return [Class<T>]
 * @author Tang Li
 * @date 2023/09/28 10:53
 * @since 1.0.0
 */
internal fun <T : Any> BaseDao<T>.getEntityClass(): Class<T> =
    ENTITY_CLASS_MAP
        .getOrPut(this::class.java) {
            actualClass().typeParamOfSuperInterface(BaseDao::class.java).rawClass()
        }.asToNotNull()

/**
 * 获取mapper类型
 * @return [Class<out BaseDao<T>>]
 * @author Tang Li
 * @date 2023/09/28 10:53
 * @since 1.0.0
 */
internal fun <T : Any> BaseDao<T>.getMapperClass(): Class<out BaseDao<T>> =
    MAPPER_CLASS_MAP
        .getOrPut(this::class.java) {
            actualClass()
        }.asToNotNull()

/**
 * 批量执行
 * @param [batchList] 对象列表
 * @param [consumer] 回调
 * @return [Boolean]
 * @author Tang Li
 * @date 2023/09/28 10:53
 * @since 1.0.0
 */
internal fun <T : Any, E> BaseDao<T>.executeBatch(
    batchList: Collection<E>,
    consumer: BiConsumer<SqlSession, E>,
): Boolean =
    SqlHelper.executeBatch(
        SpringContexts.getBean(SqlSessionFactory::class.java),
        getLog(),
        batchList,
        batchList.size + 1,
        consumer
    )
