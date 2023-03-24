package com.tony.mybatis.wrapper

import com.baomidou.mybatisplus.core.conditions.SharedString
import com.baomidou.mybatisplus.core.conditions.query.Query
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils
import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache
import com.baomidou.mybatisplus.extension.kotlin.AbstractKtWrapper
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.Predicate
import kotlin.reflect.KProperty

public open class TonyKtQueryWrapper<T : Any> :
    AbstractKtWrapper<T, TonyKtQueryWrapper<T>>,
    Query<TonyKtQueryWrapper<T>, T, KProperty<*>> {

    /**
     * 查询字段
     */
    private var sqlSelect: SharedString = SharedString()

    public constructor(entity: T) {
        this.entity = entity
        initNeed()
    }

    public constructor(entityClass: Class<T>) {
        this.entityClass = entityClass
        initNeed()
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

    /**
     * SELECT 部分 SQL 设置
     *
     * @param columns 查询字段
     */
    @SafeVarargs
    override fun select(vararg columns: KProperty<*>): TonyKtQueryWrapper<T> {
        if (ArrayUtils.isNotEmpty(columns)) {
            this.sqlSelect.stringValue = columnsToString(false, *columns)
        }
        return typedThis
    }

    /**
     * 过滤查询的字段信息(主键除外!)
     *
     * 例1: 只要 java 字段名以 "test" 开头的              -> select(i -> i.getProperty().startsWith("test"))
     *
     * 例2: 只要 java 字段属性是 CharSequence 类型的       -> select(TableFieldInfo::isCharSequence)
     *
     * 例3: 只要 java 字段没有填充策略的                   -> select(i -> i.getFieldFill == FieldFill.DEFAULT)
     *
     * 例4: 要全部字段                                   -> select(i -> true)
     *
     * 例5: 只要主键字段                                 -> select(i -> false)
     *
     *
     * @param predicate 过滤方式
     * @return this
     */
    override fun select(
        entityClass: Class<T>,
        predicate: Predicate<TableFieldInfo>,
    ): TonyKtQueryWrapper<T> {
        this.entityClass = entityClass
        this.sqlSelect.stringValue = TableInfoHelper.getTableInfo(getEntityClass()).chooseSelect(predicate)
        return typedThis
    }

    override fun getSqlSelect(): String? = sqlSelect.stringValue

    /**
     * 用于生成嵌套 sql
     *
     * 故 sqlSelect 不向下传递
     */
    override fun instance(): TonyKtQueryWrapper<T> =
        TonyKtQueryWrapper(
            entity,
            entityClass,
            sqlSelect,
            paramNameSeq,
            paramNameValuePairs,
            columnMap,
            SharedString.emptyString(),
            SharedString.emptyString(),
            SharedString.emptyString(),
        )

    override fun clear() {
        super.clear()
        sqlSelect.toNull()
    }
}
