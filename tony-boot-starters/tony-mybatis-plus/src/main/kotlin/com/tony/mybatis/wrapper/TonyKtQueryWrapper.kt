package com.tony.mybatis.wrapper

import com.baomidou.mybatisplus.core.conditions.SharedString
import com.baomidou.mybatisplus.core.conditions.query.Query
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils
import com.baomidou.mybatisplus.core.toolkit.StringPool
import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache
import com.baomidou.mybatisplus.extension.kotlin.AbstractKtWrapper
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.Predicate
import kotlin.reflect.KMutableProperty1

/**
 * mybatis plus 对应对象的包装, 用来适配一些 kotlin dao方法.
 *
 * @author tangli
 * @since 2023/5/25 15:32
 */
public open class TonyKtQueryWrapper<T : Any> :
    AbstractKtWrapper<T, TonyKtQueryWrapper<T>>,
    Query<TonyKtQueryWrapper<T>, T, KMutableProperty1<T, *>> {

    /**
     * 查询字段
     */
    private var sqlSelect: SharedString = SharedString()

    public constructor(entity: T) {
        this.entity = entity
        super.initNeed()
    }

    public constructor(entityClass: Class<T>) {
        this.entityClass = entityClass
        super.initNeed()
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

    override fun select(condition: Boolean, columns: MutableList<KMutableProperty1<T, *>>): TonyKtQueryWrapper<T> {
        if (condition && columns.isNotEmpty()) {
            this.sqlSelect.stringValue = columnsToString(false, columns)
        }
        return typedThis
    }

    override fun select(entityClass: Class<T>, predicate: Predicate<TableFieldInfo>): TonyKtQueryWrapper<T> {
        this.entityClass = entityClass
        this.sqlSelect.stringValue = TableInfoHelper.getTableInfo(getEntityClass()).chooseSelect(predicate)
        return typedThis
    }

    @SafeVarargs
    public fun select(vararg columns: String): TonyKtQueryWrapper<T> {
        if (ArrayUtils.isNotEmpty(columns)) {
            sqlSelect.stringValue = columns.joinToString(separator = StringPool.COMMA)
        }
        return typedThis
    }

    override fun getSqlSelect(): String? = sqlSelect.stringValue

    /**
     * 用于生成嵌套 sql
     *
     * 故 sqlSelect 不向下传递
     */
    override fun instance(): TonyKtQueryWrapper<T> {
        return TonyKtQueryWrapper(
            entity, entityClass, sqlSelect, paramNameSeq, paramNameValuePairs, columnMap,
            SharedString.emptyString(), SharedString.emptyString(), SharedString.emptyString()
        )
    }

    override fun clear() {
        super.clear()
        sqlSelect.toNull()
    }
}
