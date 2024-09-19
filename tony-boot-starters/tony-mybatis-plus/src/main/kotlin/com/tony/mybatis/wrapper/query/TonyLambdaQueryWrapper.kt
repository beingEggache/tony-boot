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

import com.baomidou.mybatisplus.core.conditions.AbstractLambdaWrapper
import com.baomidou.mybatisplus.core.conditions.SharedString
import com.baomidou.mybatisplus.core.conditions.query.Query
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper
import com.baomidou.mybatisplus.core.toolkit.support.SFunction
import com.tony.utils.throwIfNull
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.Predicate

/**
 * mybatis plus 对应对象的包装, 用来适配一些 kotlin dao方法.
 * Lambda 语法使用 Wrapper
 * @author tangli
 * @date 2023/09/13 19:42
 * @since 1.0.0
 */
public class TonyLambdaQueryWrapper<T : Any> :
    AbstractLambdaWrapper<T, TonyLambdaQueryWrapper<T>>,
    Query<TonyLambdaQueryWrapper<T>, T, SFunction<T, *>> {
    /**
     * 查询字段
     */
    private var sqlSelect: SharedString? = SharedString()

    internal constructor(entityClass: Class<T>) {
        this.entityClass = entityClass
        this.initNeed()
    }

    internal constructor(
        entity: T,
        entityClass: Class<T>?,
        sqlSelect: SharedString?,
        paramNameSeq: AtomicInteger?,
        paramNameValuePairs: Map<String?, Any?>?,
        mergeSegments: MergeSegments?,
        paramAlias: SharedString?,
        lastSql: SharedString?,
        sqlComment: SharedString?,
        sqlFirst: SharedString?,
    ) {
        this.entity = entity
        this.entityClass = entityClass
        this.paramNameSeq = paramNameSeq
        this.paramNameValuePairs = paramNameValuePairs
        this.expression = mergeSegments
        this.sqlSelect = sqlSelect
        this.paramAlias = paramAlias
        this.lastSql = lastSql
        this.sqlComment = sqlComment
        this.sqlFirst = sqlFirst
    }

    override fun select(
        condition: Boolean,
        columns: List<SFunction<T, *>?>,
    ): TonyLambdaQueryWrapper<T> {
        if (condition && columns.isNotEmpty()) {
            sqlSelect?.setStringValue(columnsToString(false, columns))
        }
        return typedThis
    }

    /**
     * 过滤查询的字段信息(主键除外!)
     *
     * 例1: 只要 java 字段名以 "test" 开头的             -> select(i -&gt; i.getProperty().startsWith("test"))
     *
     * 例2: 只要 java 字段属性是 CharSequence 类型的     -> select(TableFieldInfo::isCharSequence)
     *
     * 例3: 只要 java 字段没有填充策略的                 -> select(i -&gt; i.getFieldFill() == FieldFill.DEFAULT)
     *
     * 例4: 要全部字段                                   -> select(i -&gt; true)
     *
     * 例5: 只要主键字段                                 -> select(i -&gt; false)
     *
     * @param predicate 过滤方式
     * @return this
     */
    override fun select(
        entityClass: Class<T>?,
        predicate: Predicate<TableFieldInfo>,
    ): TonyLambdaQueryWrapper<T> {
        this.entityClass = (entityClass ?: this.entityClass).throwIfNull("entityClass can not be null")
        sqlSelect?.setStringValue(TableInfoHelper.getTableInfo(this.entityClass).chooseSelect(predicate))
        return typedThis
    }

    override fun getSqlSelect(): String? =
        sqlSelect?.stringValue

    /**
     * 用于生成嵌套 sql
     *
     * 故 sqlSelect 不向下传递
     */
    override fun instance(): TonyLambdaQueryWrapper<T> =
        TonyLambdaQueryWrapper(
            entity,
            entityClass,
            null,
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
        sqlSelect?.toNull()
    }
}
