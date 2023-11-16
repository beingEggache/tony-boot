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

package com.tony.mybatis.wrapper

import com.baomidou.mybatisplus.core.conditions.SharedString
import com.baomidou.mybatisplus.core.conditions.query.Query
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils
import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.Predicate
import kotlin.reflect.KProperty1

/**
 * mybatis plus 对应对象的包装, 用来适配一些 kotlin dao方法.
 * @author Tang Li
 * @date 2023/09/13 10:41
 * @since 1.0.0
 */
public open class TonyKtQueryWrapper<T : Any> :
    TonyAbstractKtWrapper<T, TonyKtQueryWrapper<T>>,
    Query<TonyKtQueryWrapper<T>, T, KProperty1<T, *>> {
    /**
     * 查询字段
     */
    private var sqlSelect: SharedString = SharedString()

    internal constructor(entityClass: Class<T>) {
        this.entityClass = entityClass
        this.initNeed()
    }

    internal constructor(
        entity: T?,
        entityClass: Class<T>,
        sqlSelect: SharedString,
        paramNameSeq: AtomicInteger,
        paramNameValuePairs: Map<String, Any>,
        columnMap: Map<String, ColumnCache>,
        lastSql: SharedString,
        sqlComment: SharedString,
        sqlFirst: SharedString,
    ) {
        this.entity = entity
        this.paramNameSeq = paramNameSeq
        this.paramNameValuePairs = paramNameValuePairs
        this.expression = MergeSegments()
        this.columnMap = columnMap
        this.sqlSelect = sqlSelect
        this.entityClass = entityClass
        this.lastSql = lastSql
        this.sqlComment = sqlComment
        this.sqlFirst = sqlFirst
    }

    override fun select(
        condition: Boolean,
        columns: MutableList<KProperty1<T, *>>,
    ): TonyKtQueryWrapper<T> {
        if (condition && columns.isNotEmpty()) {
            this.sqlSelect.stringValue = columnsToString(false, columns)
        }
        return typedThis
    }

    override fun select(
        entityClass: Class<T>,
        predicate: Predicate<TableFieldInfo>,
    ): TonyKtQueryWrapper<T> {
        this.entityClass = entityClass
        this.sqlSelect.stringValue = TableInfoHelper.getTableInfo(getEntityClass()).chooseSelect(predicate)
        return typedThis
    }

    public fun select(vararg columns: String): TonyKtQueryWrapper<T> {
        if (ArrayUtils.isNotEmpty(columns)) {
            sqlSelect.stringValue = columns.joinToString()
        }
        return typedThis
    }

    override fun getSqlSelect(): String? =
        sqlSelect.stringValue

    /**
     * 用于生成嵌套 sql
     *
     * 故 sqlSelect 不向下传递
     */
    override fun instance(): TonyKtQueryWrapper<T> =
        TonyKtQueryWrapper(
            entity, entityClass, sqlSelect, paramNameSeq, paramNameValuePairs, columnMap,
            SharedString.emptyString(), SharedString.emptyString(), SharedString.emptyString()
        )

    override fun clear() {
        super.clear()
        sqlSelect.toNull()
    }
}
