package com.tony.mybatis.wrapper

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper
import com.baomidou.mybatisplus.core.conditions.SharedString
import com.baomidou.mybatisplus.core.conditions.query.Query
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils
import com.baomidou.mybatisplus.core.toolkit.StringPool
import com.baomidou.mybatisplus.core.toolkit.sql.SqlInjectionUtils
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.Predicate

/**
 * mybatis plus 对应对象的包装, 用来适配一些 kotlin dao方法.
 *
 * @author tangli
 * @since 2023/5/25 15:32
 */

public open class TonyQueryWrapper<T : Any> :
    AbstractWrapper<T, String, TonyQueryWrapper<T>>,
    Query<TonyQueryWrapper<T>, T, String> {
    /**
     * 查询字段
     */
    private val sqlSelect: SharedString = SharedString()

    public constructor(entity: T) {
        super.setEntity(entity)
        super.initNeed()
    }

    public constructor(entityClass: Class<T>) {
        super.setEntityClass(entityClass)
        super.initNeed()
    }

    public constructor(entity: T, vararg columns: String) {
        super.setEntity(entity)
        super.initNeed()
        this.select(*columns)
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
        super.setEntity(entity)
        super.setEntityClass(entityClass)
        this.paramNameSeq = paramNameSeq
        this.paramNameValuePairs = paramNameValuePairs
        expression = mergeSegments
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
    public fun checkSqlInjection(): TonyQueryWrapper<T> {
        checkSqlInjection = true
        return this
    }

    protected override fun columnToString(column: String): String {
        if (checkSqlInjection && SqlInjectionUtils.check(column)) {
            throw MybatisPlusException("Discovering SQL injection column: $column")
        }
        return column
    }

    override fun select(condition: Boolean, columns: List<String>): TonyQueryWrapper<T> {
        if (condition && CollectionUtils.isNotEmpty(columns)) {
            sqlSelect.setStringValue(java.lang.String.join(StringPool.COMMA, columns))
        }
        return typedThis
    }

    public override fun select(entityClass: Class<T>, predicate: Predicate<TableFieldInfo>): TonyQueryWrapper<T> {
        super.setEntityClass(entityClass)
        sqlSelect.setStringValue(TableInfoHelper.getTableInfo(getEntityClass()).chooseSelect(predicate))
        return typedThis
    }

    override fun getSqlSelect(): String {
        return sqlSelect.stringValue
    }

    /**
     * 返回一个支持 lambda 函数写法的 wrapper
     */
    public fun lambda(): TonyLambdaQueryWrapper<T> {
        return TonyLambdaQueryWrapper(
            entity, entityClass, sqlSelect, paramNameSeq, paramNameValuePairs,
            expression, paramAlias, lastSql, sqlComment, sqlFirst
        )
    }

    /**
     * 用于生成嵌套 sql
     *
     *
     * 故 sqlSelect 不向下传递
     *
     */
    override fun instance(): TonyQueryWrapper<T> {
        return TonyQueryWrapper(
            entity, entityClass, paramNameSeq, paramNameValuePairs, MergeSegments(),
            paramAlias, SharedString.emptyString(), SharedString.emptyString(), SharedString.emptyString()
        )
    }

    override fun clear() {
        super.clear()
        sqlSelect.toNull()
    }
}
