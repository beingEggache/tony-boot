/**
 * tony-dependencies
 * Wrappers
 *
 * @author tangli
 * @since 2022/7/21 13:41
 */
package com.tony.mybatis.wrapper

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo
import com.baomidou.mybatisplus.core.toolkit.support.SFunction
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper
import com.tony.ApiProperty
import com.tony.PageResultLike
import com.tony.Pageable
import com.tony.mybatis.dao.BaseDao
import com.tony.utils.throwIf
import com.tony.utils.throwIfNull
import java.util.function.BiPredicate
import java.util.function.Consumer
import java.util.function.Predicate

open class TonyLambdaQueryChainWrapper<T : Any>(baseDao: BaseDao<T>) : LambdaQueryChainWrapper<T>(baseDao) {

    private val baseDao: BaseDao<T> = baseMapper as BaseDao<T>

    @JvmOverloads
    fun oneNotNull(
        message: String = ApiProperty.notFoundMessage,
        code: Int = ApiProperty.notFoundCode
    ): T = baseMapper.selectOne(wrapper).throwIfNull(message, code)

    @JvmOverloads
    fun throwIfExists(
        message: String,
        code: Int = ApiProperty.notFoundCode
    ) {
        throwIf(exists(), message, code)
    }

    @JvmOverloads
    fun throwIfNotExists(
        message: String,
        code: Int = ApiProperty.notFoundCode
    ) {
        throwIf(!exists(), message, code)
    }

    fun <E : PageResultLike<T>> pageResult(page: Pageable) = baseDao.selectPageResult<E>(page, wrapper)

    override fun <V : Any?> allEq(
        condition: Boolean,
        params: MutableMap<SFunction<T, *>, V>?,
        null2IsNull: Boolean
    ): TonyLambdaQueryChainWrapper<T> = apply {
        wrapper.allEq(condition, params, null2IsNull)
    }

    override fun <V : Any?> allEq(
        condition: Boolean,
        filter: BiPredicate<SFunction<T, *>, V>?,
        params: MutableMap<SFunction<T, *>, V>?,
        null2IsNull: Boolean
    ): TonyLambdaQueryChainWrapper<T> = apply {
        wrapper.allEq(condition, filter, params, null2IsNull)
    }

    override fun <V : Any?> allEq(params: MutableMap<SFunction<T, *>, V>?): TonyLambdaQueryChainWrapper<T> =
        allEq(true, params, true)

    override fun <V : Any?> allEq(
        params: MutableMap<SFunction<T, *>, V>?,
        null2IsNull: Boolean
    ): TonyLambdaQueryChainWrapper<T> = allEq(true, params, null2IsNull)

    override fun <V : Any?> allEq(
        filter: BiPredicate<SFunction<T, *>, V>?,
        params: MutableMap<SFunction<T, *>, V>?
    ): TonyLambdaQueryChainWrapper<T> = allEq(true, filter, params, true)

    override fun <V : Any?> allEq(
        filter: BiPredicate<SFunction<T, *>, V>?,
        params: MutableMap<SFunction<T, *>, V>?,
        null2IsNull: Boolean
    ): TonyLambdaQueryChainWrapper<T> = allEq(true, filter, params, null2IsNull)

    override fun eq(condition: Boolean, column: SFunction<T, *>?, `val`: Any?): TonyLambdaQueryChainWrapper<T> = apply {
        wrapper.eq(condition, column, `val`)
    }

    override fun eq(column: SFunction<T, *>?, `val`: Any?): TonyLambdaQueryChainWrapper<T> = eq(true, column, `val`)

    override fun ne(condition: Boolean, column: SFunction<T, *>?, `val`: Any?): TonyLambdaQueryChainWrapper<T> = apply {
        wrapper.ne(condition, column, `val`)
    }

    override fun ne(column: SFunction<T, *>?, `val`: Any?): TonyLambdaQueryChainWrapper<T> = ne(true, column, `val`)

    override fun gt(condition: Boolean, column: SFunction<T, *>?, `val`: Any?): TonyLambdaQueryChainWrapper<T> = apply {
        wrapper.gt(condition, column, `val`)
    }

    override fun gt(column: SFunction<T, *>?, `val`: Any?): TonyLambdaQueryChainWrapper<T> = gt(true, column, `val`)

    override fun ge(condition: Boolean, column: SFunction<T, *>?, `val`: Any?): TonyLambdaQueryChainWrapper<T> = apply {
        wrapper.ge(condition, column, `val`)
    }

    override fun ge(column: SFunction<T, *>?, `val`: Any?): TonyLambdaQueryChainWrapper<T> = ge(true, column, `val`)

    override fun lt(condition: Boolean, column: SFunction<T, *>?, `val`: Any?): TonyLambdaQueryChainWrapper<T> = apply {
        wrapper.lt(condition, column, `val`)
    }

    override fun lt(column: SFunction<T, *>?, `val`: Any?): TonyLambdaQueryChainWrapper<T> = lt(true, column, `val`)

    override fun le(condition: Boolean, column: SFunction<T, *>?, `val`: Any?): TonyLambdaQueryChainWrapper<T> = apply {
        wrapper.le(condition, column, `val`)
    }

    override fun le(column: SFunction<T, *>?, `val`: Any?): TonyLambdaQueryChainWrapper<T> = le(true, column, `val`)

    override fun between(
        condition: Boolean,
        column: SFunction<T, *>?,
        val1: Any?,
        val2: Any?
    ): TonyLambdaQueryChainWrapper<T> = apply {
        wrapper.between(condition, column, val1, val2)
    }

    override fun between(column: SFunction<T, *>?, val1: Any?, val2: Any?): TonyLambdaQueryChainWrapper<T> =
        between(true, column, val1, val2)

    override fun notBetween(
        condition: Boolean,
        column: SFunction<T, *>?,
        val1: Any?,
        val2: Any?
    ): TonyLambdaQueryChainWrapper<T> = apply {
        wrapper.notBetween(condition, column, val1, val2)
    }

    override fun notBetween(column: SFunction<T, *>?, val1: Any?, val2: Any?): TonyLambdaQueryChainWrapper<T> =
        notBetween(true, column, val1, val2)

    override fun like(condition: Boolean, column: SFunction<T, *>?, `val`: Any?): TonyLambdaQueryChainWrapper<T> =
        apply {
            wrapper.like(condition, column, `val`)
        }

    override fun like(column: SFunction<T, *>?, `val`: Any?): TonyLambdaQueryChainWrapper<T> =
        like(true, column, `val`)

    override fun notLike(condition: Boolean, column: SFunction<T, *>?, `val`: Any?): TonyLambdaQueryChainWrapper<T> =
        apply {
            wrapper.notLike(condition, column, `val`)
        }

    override fun notLike(column: SFunction<T, *>?, `val`: Any?): TonyLambdaQueryChainWrapper<T> =
        notLike(true, column, `val`)

    override fun likeLeft(condition: Boolean, column: SFunction<T, *>?, `val`: Any?): TonyLambdaQueryChainWrapper<T> =
        apply {
            wrapper.likeLeft(condition, column, `val`)
        }

    override fun likeLeft(column: SFunction<T, *>?, `val`: Any?): TonyLambdaQueryChainWrapper<T> =
        likeLeft(true, column, `val`)

    override fun likeRight(condition: Boolean, column: SFunction<T, *>?, `val`: Any?): TonyLambdaQueryChainWrapper<T> =
        apply {
            wrapper.likeRight(condition, column, `val`)
        }

    override fun likeRight(column: SFunction<T, *>?, `val`: Any?): TonyLambdaQueryChainWrapper<T> =
        likeRight(true, column, `val`)

    override fun isNull(condition: Boolean, column: SFunction<T, *>?): TonyLambdaQueryChainWrapper<T> = apply {
        wrapper.isNull(condition, column)
    }

    override fun isNull(column: SFunction<T, *>?): TonyLambdaQueryChainWrapper<T> = isNull(true, column)

    override fun isNotNull(condition: Boolean, column: SFunction<T, *>?): TonyLambdaQueryChainWrapper<T> = apply {
        wrapper.isNotNull(condition, column)
    }

    override fun isNotNull(column: SFunction<T, *>?): TonyLambdaQueryChainWrapper<T> = isNotNull(true, column)

    override fun `in`(
        condition: Boolean,
        column: SFunction<T, *>?,
        coll: MutableCollection<*>?
    ): TonyLambdaQueryChainWrapper<T> = apply {
        wrapper.`in`(condition, column, coll)
    }

    override fun `in`(
        condition: Boolean,
        column: SFunction<T, *>?,
        vararg values: Any?
    ): TonyLambdaQueryChainWrapper<T> = apply {
        wrapper.`in`(condition, column, *values)
    }

    override fun `in`(column: SFunction<T, *>?, coll: MutableCollection<*>?): TonyLambdaQueryChainWrapper<T> =
        `in`(true, column, coll)

    override fun `in`(column: SFunction<T, *>?, vararg values: Any?): TonyLambdaQueryChainWrapper<T> =
        apply {
            wrapper.`in`(true, column, *values)
        }

    override fun notIn(
        condition: Boolean,
        column: SFunction<T, *>?,
        coll: MutableCollection<*>?
    ): TonyLambdaQueryChainWrapper<T> = apply {
        wrapper.notIn(condition, column, coll)
    }

    override fun notIn(
        condition: Boolean,
        column: SFunction<T, *>?,
        vararg values: Any?
    ): TonyLambdaQueryChainWrapper<T> = apply {
        wrapper.notIn(condition, column, *values)
    }

    override fun notIn(column: SFunction<T, *>?, coll: MutableCollection<*>?): TonyLambdaQueryChainWrapper<T> =
        notIn(true, column, coll)

    override fun notIn(column: SFunction<T, *>?, vararg value: Any?): TonyLambdaQueryChainWrapper<T> = apply {
        wrapper.notIn(true, column, *value)
    }

    override fun inSql(condition: Boolean, column: SFunction<T, *>?, inValue: String?): TonyLambdaQueryChainWrapper<T> =
        apply {
            wrapper.inSql(condition, column, inValue)
        }

    override fun inSql(column: SFunction<T, *>?, inValue: String?): TonyLambdaQueryChainWrapper<T> =
        inSql(true, column, inValue)

    override fun gtSql(condition: Boolean, column: SFunction<T, *>?, inValue: String?): TonyLambdaQueryChainWrapper<T> =
        apply {
            wrapper.gtSql(condition, column, inValue)
        }

    override fun gtSql(column: SFunction<T, *>?, inValue: String?): TonyLambdaQueryChainWrapper<T> =
        gtSql(true, column, inValue)

    override fun geSql(condition: Boolean, column: SFunction<T, *>?, inValue: String?): TonyLambdaQueryChainWrapper<T> =
        apply {
            wrapper.geSql(condition, column, inValue)
        }

    override fun geSql(column: SFunction<T, *>?, inValue: String?): TonyLambdaQueryChainWrapper<T> =
        geSql(true, column, inValue)

    override fun ltSql(condition: Boolean, column: SFunction<T, *>?, inValue: String?): TonyLambdaQueryChainWrapper<T> =
        apply {
            wrapper.ltSql(condition, column, inValue)
        }

    override fun ltSql(column: SFunction<T, *>?, inValue: String?): TonyLambdaQueryChainWrapper<T> =
        ltSql(true, column, inValue)

    override fun leSql(condition: Boolean, column: SFunction<T, *>?, inValue: String?): TonyLambdaQueryChainWrapper<T> =
        apply {
            wrapper.leSql(condition, column, inValue)
        }

    override fun leSql(column: SFunction<T, *>?, inValue: String?): TonyLambdaQueryChainWrapper<T> =
        leSql(true, column, inValue)

    override fun notInSql(
        condition: Boolean,
        column: SFunction<T, *>?,
        inValue: String?
    ): TonyLambdaQueryChainWrapper<T> = apply {
        wrapper.notInSql(condition, column, inValue)
    }

    override fun notInSql(column: SFunction<T, *>?, inValue: String?): TonyLambdaQueryChainWrapper<T> =
        notInSql(true, column, inValue)

    override fun groupBy(
        condition: Boolean,
        column: SFunction<T, *>?,
        vararg columns: SFunction<T, *>?
    ): TonyLambdaQueryChainWrapper<T> = apply {
        wrapper.groupBy(condition, column, *columns)
    }

    override fun groupBy(condition: Boolean, column: SFunction<T, *>?): TonyLambdaQueryChainWrapper<T> = apply {
        wrapper.groupBy(condition, column)
    }

    override fun groupBy(condition: Boolean, columns: MutableList<SFunction<T, *>>?): TonyLambdaQueryChainWrapper<T> =
        apply {
            wrapper.groupBy(condition, columns)
        }

    override fun groupBy(column: SFunction<T, *>?): TonyLambdaQueryChainWrapper<T> = groupBy(true, column)

    override fun groupBy(columns: MutableList<SFunction<T, *>>?): TonyLambdaQueryChainWrapper<T> =
        groupBy(true, columns)

    override fun groupBy(column: SFunction<T, *>?, vararg columns: SFunction<T, *>?): TonyLambdaQueryChainWrapper<T> =
        apply {
            wrapper.groupBy(true, column, *columns)
        }

    override fun orderByAsc(condition: Boolean, column: SFunction<T, *>?): TonyLambdaQueryChainWrapper<T> = apply {
        wrapper.orderBy(condition, true, column)
    }

    override fun orderByAsc(column: SFunction<T, *>?): TonyLambdaQueryChainWrapper<T> = orderByAsc(true, column)

    override fun orderByAsc(
        condition: Boolean,
        columns: MutableList<SFunction<T, *>>?
    ): TonyLambdaQueryChainWrapper<T> = apply {
        wrapper.orderBy(condition, true, columns)
    }

    override fun orderByAsc(columns: MutableList<SFunction<T, *>>?): TonyLambdaQueryChainWrapper<T> =
        orderByAsc(true, columns)

    override fun orderByAsc(
        column: SFunction<T, *>?,
        vararg columns: SFunction<T, *>?
    ): TonyLambdaQueryChainWrapper<T> = apply {
        wrapper.orderBy(true, true, column, *columns)
    }

    override fun orderByAsc(
        condition: Boolean,
        column: SFunction<T, *>?,
        vararg columns: SFunction<T, *>?
    ): TonyLambdaQueryChainWrapper<T> = apply {
        wrapper.orderBy(condition, true, column, *columns)
    }

    override fun orderByDesc(condition: Boolean, column: SFunction<T, *>?): TonyLambdaQueryChainWrapper<T> = apply {
        wrapper.orderBy(condition, false, column)
    }

    override fun orderByDesc(column: SFunction<T, *>?): TonyLambdaQueryChainWrapper<T> = orderByDesc(true, column)

    override fun orderByDesc(
        condition: Boolean,
        columns: MutableList<SFunction<T, *>>?
    ): TonyLambdaQueryChainWrapper<T> = apply {
        wrapper.orderBy(condition, false, columns)
    }

    override fun orderByDesc(columns: MutableList<SFunction<T, *>>?): TonyLambdaQueryChainWrapper<T> =
        orderByDesc(true, columns)

    override fun orderByDesc(
        column: SFunction<T, *>?,
        vararg columns: SFunction<T, *>?
    ): TonyLambdaQueryChainWrapper<T> = apply {
        wrapper.orderBy(true, false, column, *columns)
    }

    override fun orderByDesc(
        condition: Boolean,
        column: SFunction<T, *>?,
        vararg columns: SFunction<T, *>?
    ): TonyLambdaQueryChainWrapper<T> = apply {
        wrapper.orderBy(condition, false, column, *columns)
    }

    override fun orderBy(
        condition: Boolean,
        isAsc: Boolean,
        column: SFunction<T, *>?,
        vararg columns: SFunction<T, *>?
    ): TonyLambdaQueryChainWrapper<T> = apply {
        wrapper.orderBy(condition, isAsc, column, *columns)
    }

    override fun orderBy(condition: Boolean, isAsc: Boolean, column: SFunction<T, *>?): TonyLambdaQueryChainWrapper<T> =
        apply {
            wrapper.orderBy(condition, isAsc, column)
        }

    override fun orderBy(
        condition: Boolean,
        isAsc: Boolean,
        columns: MutableList<SFunction<T, *>>?
    ): TonyLambdaQueryChainWrapper<T> = apply {
        wrapper.orderBy(condition, isAsc, columns)
    }

    override fun having(condition: Boolean, sqlHaving: String?, vararg params: Any?): TonyLambdaQueryChainWrapper<T> =
        apply {
            wrapper.having(condition, sqlHaving, *params)
        }

    override fun having(sqlHaving: String?, vararg params: Any?): TonyLambdaQueryChainWrapper<T> =
        having(true, sqlHaving, *params)

    fun func(
        condition: Boolean,
        consumer: Consumer<TonyLambdaQueryChainWrapper<T>>?
    ): TonyLambdaQueryChainWrapper<T> = apply {
        if (condition) {
            consumer?.accept(this)
        }
    }

    fun func(consumer: Consumer<TonyLambdaQueryChainWrapper<T>>?): TonyLambdaQueryChainWrapper<T> = func(consumer)

    override fun or(condition: Boolean): TonyLambdaQueryChainWrapper<T> = apply {
        wrapper.or(condition)
    }

    fun or(condition: Boolean, consumer: Consumer<TonyLambdaQueryChainWrapper<T>>?): TonyLambdaQueryChainWrapper<T> =
        or(condition, consumer)

    override fun or(): TonyLambdaQueryChainWrapper<T> = or(true)

    fun or(consumer: Consumer<TonyLambdaQueryChainWrapper<T>>?): TonyLambdaQueryChainWrapper<T> = or(consumer)

    override fun apply(condition: Boolean, applySql: String?, vararg values: Any?): TonyLambdaQueryChainWrapper<T> =
        apply {
            wrapper.apply(condition, applySql, *values)
        }

    override fun apply(applySql: String?, vararg values: Any?): TonyLambdaQueryChainWrapper<T> =
        apply(true, applySql, *values)

    override fun last(condition: Boolean, lastSql: String?): TonyLambdaQueryChainWrapper<T> = apply {
        wrapper.last(condition, lastSql)
    }

    override fun last(lastSql: String?): TonyLambdaQueryChainWrapper<T> = last(true, lastSql)

    override fun comment(condition: Boolean, comment: String?): TonyLambdaQueryChainWrapper<T> = apply {
        wrapper.comment(condition, comment)
    }

    override fun comment(comment: String?): TonyLambdaQueryChainWrapper<T> = comment(true, comment)

    override fun first(condition: Boolean, firstSql: String?): TonyLambdaQueryChainWrapper<T> = apply {
        wrapper.first(condition, firstSql)
    }

    override fun first(firstSql: String?): TonyLambdaQueryChainWrapper<T> = first(true, firstSql)

    override fun exists(condition: Boolean, existsSql: String?, vararg values: Any?): TonyLambdaQueryChainWrapper<T> =
        apply {
            wrapper.exists(condition, existsSql, *values)
        }

    override fun exists(existsSql: String?, vararg values: Any?): TonyLambdaQueryChainWrapper<T> =
        exists(true, existsSql, *values)

    override fun notExists(
        condition: Boolean,
        existsSql: String?,
        vararg values: Any?
    ): TonyLambdaQueryChainWrapper<T> = apply {
        wrapper.notExists(condition, existsSql, *values)
    }

    override fun notExists(existsSql: String?, vararg values: Any?): TonyLambdaQueryChainWrapper<T> =
        notExists(true, existsSql, *values)

    override fun and(condition: Boolean, consumer: Consumer<LambdaQueryWrapper<T>>?): TonyLambdaQueryChainWrapper<T> =
        apply {
            wrapper.and(condition, consumer)
        }

    override fun and(consumer: Consumer<LambdaQueryWrapper<T>>?): TonyLambdaQueryChainWrapper<T> = and(true, consumer)

    override fun nested(
        condition: Boolean,
        consumer: Consumer<LambdaQueryWrapper<T>>?
    ): TonyLambdaQueryChainWrapper<T> = apply {
        wrapper.nested(condition, consumer)
    }

    override fun nested(consumer: Consumer<LambdaQueryWrapper<T>>?): TonyLambdaQueryChainWrapper<T> =
        nested(true, consumer)

    override fun not(condition: Boolean, consumer: Consumer<LambdaQueryWrapper<T>>?): TonyLambdaQueryChainWrapper<T> =
        apply {
            wrapper.not(condition, consumer)
        }

    override fun not(consumer: Consumer<LambdaQueryWrapper<T>>?): TonyLambdaQueryChainWrapper<T> = not(true, consumer)

    override fun setEntity(entity: T): TonyLambdaQueryChainWrapper<T> = apply {
        wrapper.entity = entity
    }

    override fun setEntityClass(entityClass: Class<T>?): TonyLambdaQueryChainWrapper<T> = apply {
        wrapper.entityClass = entityClass
    }

    override fun select(entityClass: Class<T>?, predicate: Predicate<TableFieldInfo>?): TonyLambdaQueryChainWrapper<T> =
        apply {
            wrapperChildren.select(entityClass, predicate)
        }

    override fun select(predicate: Predicate<TableFieldInfo>?): TonyLambdaQueryChainWrapper<T> =
        select(null, predicate)
}
