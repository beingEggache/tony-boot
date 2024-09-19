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

package com.tony.mybatis.wrapper.query

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper
import com.baomidou.mybatisplus.core.conditions.SharedString
import com.baomidou.mybatisplus.core.conditions.query.Query
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper
import com.baomidou.mybatisplus.core.toolkit.sql.SqlInjectionUtils
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.Predicate

/**
 * mybatis plus 对应对象的包装, 用来适配一些 kotlin dao方法.
 *
 * @author tangli
 * @date 2023/05/25 19:32
 */

public open class TonyQueryWrapper<T : Any> :
    AbstractWrapper<T, String, TonyQueryWrapper<T>>,
    Query<TonyQueryWrapper<T>, T, String> {
    /**
     * 查询字段
     */
    private val sqlSelect: SharedString = SharedString()

    internal constructor(entityClass: Class<T>) {
        this.entityClass = entityClass
        this.initNeed()
    }

    /**
     * 非对外公开的构造方法,只用于生产嵌套 sql
     *
     * @param entityClass 本不应该需要的
     */
    private constructor(
        entity: T,
        entityClass: Class<T>,
        paramNameSeq: AtomicInteger,
        paramNameValuePairs: Map<String, Any>,
        mergeSegments: MergeSegments,
        paramAlias: SharedString,
        lastSql: SharedString,
        sqlComment: SharedString,
        sqlFirst: SharedString,
    ) {
        this.entity = entity
        this.entityClass = entityClass
        this.paramNameSeq = paramNameSeq
        this.paramNameValuePairs = paramNameValuePairs
        this.expression = mergeSegments
        this.paramAlias = paramAlias
        this.lastSql = lastSql
        this.sqlComment = sqlComment
        this.sqlFirst = sqlFirst
    }

    /**
     * 检查 SQL 注入过滤
     */
    private var checkSqlInjection = false

    /**
     * 开启检查 SQL 注入
     */
    public fun checkSqlInjection(): TonyQueryWrapper<T> =
        apply {
            checkSqlInjection = true
        }

    protected override fun columnToString(column: String): String {
        if (checkSqlInjection && SqlInjectionUtils.check(column)) {
            throw MybatisPlusException("Discovering SQL injection column: $column")
        }
        return column
    }

    override fun select(
        condition: Boolean,
        columns: List<String>,
    ): TonyQueryWrapper<T> {
        if (condition && columns.isNotEmpty()) {
            sqlSelect.setStringValue(columns.joinToString())
        }
        return typedThis
    }

    public override fun select(
        entityClass: Class<T>,
        predicate: Predicate<TableFieldInfo>,
    ): TonyQueryWrapper<T> {
        super.setEntityClass(entityClass)
        sqlSelect.setStringValue(TableInfoHelper.getTableInfo(getEntityClass()).chooseSelect(predicate))
        return typedThis
    }

    override fun getSqlSelect(): String =
        sqlSelect.stringValue

    /**
     * 返回一个支持 lambda 函数写法的 wrapper
     */
    public fun lambda(): TonyLambdaQueryWrapper<T> =
        TonyLambdaQueryWrapper(
            entity,
            entityClass,
            sqlSelect,
            paramNameSeq,
            paramNameValuePairs,
            expression,
            paramAlias,
            lastSql,
            sqlComment,
            sqlFirst
        )

    /**
     * 用于生成嵌套 sql
     *
     *
     * 故 sqlSelect 不向下传递
     *
     */
    override fun instance(): TonyQueryWrapper<T> =
        TonyQueryWrapper(
            entity,
            entityClass,
            paramNameSeq,
            paramNameValuePairs,
            MergeSegments(),
            paramAlias,
            SharedString.emptyString(),
            SharedString.emptyString(),
            SharedString.emptyString()
        )

    override fun clear() {
        super.clear()
        sqlSelect.toNull()
    }
}
