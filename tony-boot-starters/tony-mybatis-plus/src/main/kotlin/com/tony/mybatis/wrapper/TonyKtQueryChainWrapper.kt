package com.tony.mybatis.wrapper

import com.baomidou.mybatisplus.core.conditions.query.Query
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo
import com.baomidou.mybatisplus.extension.conditions.AbstractChainWrapper
import com.tony.mybatis.dao.BaseDao
import com.tony.mybatis.dao.getEntityClass
import java.util.function.Predicate
import kotlin.reflect.KMutableProperty1

/**
 * mybatis plus 对应对象的包装, 用来适配一些 kotlin dao方法.
 * 比如 [oneNotNull], [throwIfExists], [pageResult]
 * @author Tang Li
 * @date 2023/09/13 10:41
 * @since 1.0.0
 */
@Suppress("MemberVisibilityCanBePrivate")
public open class TonyKtQueryChainWrapper<T : Any>
    internal constructor(private val baseMapper: BaseDao<T>) :
    AbstractChainWrapper<T, KMutableProperty1<T, *>, TonyKtQueryChainWrapper<T>, TonyKtQueryWrapper<T>>(),
    TonyChainQuery<T>,
    Query<TonyKtQueryChainWrapper<T>, T, KMutableProperty1<T, *>> {

        init {
            wrapperChildren = TonyKtQueryWrapper(baseMapper.getEntityClass())
        }

        override fun select(
            condition: Boolean,
            columns: MutableList<KMutableProperty1<T, *>>,
        ): TonyKtQueryChainWrapper<T> {
            wrapperChildren.select(condition, columns)
            return typedThis
        }

        override fun select(entityClass: Class<T>, predicate: Predicate<TableFieldInfo>): TonyKtQueryChainWrapper<T> {
            wrapperChildren.select(entityClass, predicate)
            return typedThis
        }

        public fun select(vararg columns: String): TonyKtQueryChainWrapper<T> {
            wrapperChildren.select(*columns)
            return typedThis
        }

        override fun getBaseMapper(): BaseDao<T> = baseMapper
    }
