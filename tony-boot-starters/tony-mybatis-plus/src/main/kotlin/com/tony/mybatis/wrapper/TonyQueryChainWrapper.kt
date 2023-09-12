package com.tony.mybatis.wrapper

import com.baomidou.mybatisplus.core.conditions.query.Query
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo
import com.baomidou.mybatisplus.extension.conditions.AbstractChainWrapper
import com.tony.mybatis.dao.BaseDao
import com.tony.mybatis.dao.getEntityClass
import java.util.function.Predicate

/**
 * mybatis plus 对应对象的包装, 用来适配一些 kotlin dao方法.
 * 比如 [oneNotNull], [throwIfExists], [pageResult]
 *
 * @author Tang Li
 * @date 2023/5/25 15:26
 */
@Suppress("MemberVisibilityCanBePrivate")
public open class TonyQueryChainWrapper<T : Any>
    internal constructor(private val baseMapper: BaseDao<T>) :
    AbstractChainWrapper<T, String, TonyQueryChainWrapper<T>, TonyQueryWrapper<T>>(),
    TonyChainQuery<T>,
    Query<TonyQueryChainWrapper<T>, T, String> {

        init {
            wrapperChildren = TonyQueryWrapper(baseMapper.getEntityClass())
        }

        override fun select(
            condition: Boolean,
            columns: MutableList<String>,
        ): TonyQueryChainWrapper<T> {
            wrapperChildren.select(condition, columns)
            return typedThis
        }

        override fun select(entityClass: Class<T>, predicate: Predicate<TableFieldInfo>): TonyQueryChainWrapper<T> {
            wrapperChildren.select(entityClass, predicate)
            return typedThis
        }

        override fun select(vararg columns: String): TonyQueryChainWrapper<T> {
            wrapperChildren.select(*columns)
            return typedThis
        }

        override fun getBaseMapper(): BaseDao<T> = baseMapper
    }
