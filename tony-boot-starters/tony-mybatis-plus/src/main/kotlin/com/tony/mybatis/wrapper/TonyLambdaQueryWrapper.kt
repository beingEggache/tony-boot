package com.tony.mybatis.wrapper

import com.baomidou.mybatisplus.core.conditions.AbstractLambdaWrapper
import com.baomidou.mybatisplus.core.conditions.SharedString
import com.baomidou.mybatisplus.core.conditions.query.Query
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils
import com.baomidou.mybatisplus.core.toolkit.Assert
import com.baomidou.mybatisplus.core.toolkit.support.SFunction
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.Predicate

/**
 * Lambda 语法使用 Wrapper
 *
 * @author tangli
 * @since 2022/7/27
 */
@Suppress("unused")
class TonyLambdaQueryWrapper<T : Any> :
    AbstractLambdaWrapper<T, TonyLambdaQueryWrapper<T>>,
    Query<TonyLambdaQueryWrapper<T>, T, SFunction<T, *>> {
    /**
     * 查询字段
     */
    private var sqlSelect: SharedString? = SharedString()

    @JvmOverloads
    constructor(entity: T? = null) {
        super.setEntity(entity)
        super.initNeed()
    }

    constructor(entityClass: Class<T>?) {
        super.setEntityClass(entityClass)
        super.initNeed()
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
        sqlFirst: SharedString?
    ) {
        super.setEntity(entity)
        super.setEntityClass(entityClass)
        this.paramNameSeq = paramNameSeq
        this.paramNameValuePairs = paramNameValuePairs
        this.expression = mergeSegments
        this.sqlSelect = sqlSelect
        this.paramAlias = paramAlias
        this.lastSql = lastSql
        this.sqlComment = sqlComment
        this.sqlFirst = sqlFirst
    }

    /**
     * SELECT 部分 SQL 设置
     *
     * @param columns 查询字段
     */
    @SuppressWarnings(value = ["varargs", "unchecked"])
    @SafeVarargs
    override fun select(vararg columns: SFunction<T, *>): TonyLambdaQueryWrapper<T> = apply {
        if (ArrayUtils.isNotEmpty(columns)) {
            sqlSelect?.stringValue = columnsToString(false, *columns)
        }
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
    override fun select(entityClass: Class<T>?, predicate: Predicate<TableFieldInfo>): TonyLambdaQueryWrapper<T> {
        var innerEntityClass = entityClass
        if (innerEntityClass == null) {
            innerEntityClass = this.entityClass
        } else {
            setEntityClass(entityClass)
        }
        Assert.notNull(innerEntityClass, "entityClass can not be null")
        sqlSelect?.stringValue = TableInfoHelper.getTableInfo(innerEntityClass).chooseSelect(predicate)
        return typedThis
    }

    override fun getSqlSelect(): String? = sqlSelect?.stringValue

    /**
     * 用于生成嵌套 sql
     *
     * 故 sqlSelect 不向下传递
     */
    override fun instance(): TonyLambdaQueryWrapper<T> {
        return TonyLambdaQueryWrapper(
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
    }

    override fun clear() {
        super.clear()
        sqlSelect?.toNull()
    }
}
