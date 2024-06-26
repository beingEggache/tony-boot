package com.tony.mybatis.wrapper

import com.baomidou.mybatisplus.core.conditions.update.Update
import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.baomidou.mybatisplus.extension.conditions.AbstractChainWrapper
import com.baomidou.mybatisplus.extension.conditions.update.ChainUpdate
import com.baomidou.mybatisplus.extension.kotlin.KtUpdateWrapper
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper
import com.tony.mybatis.dao.BaseDao
import kotlin.reflect.KProperty1

/**
 * TonyKtUpdateChainWrapper is
 * @author tangli
 * @date 2024/06/26 13:21
 * @since 1.0.0
 */
public class TonyKtUpdateChainWrapper<T : Any>(
    internal val baseDao: BaseDao<T>,
) : AbstractChainWrapper<T, KProperty1<in T, *>, TonyKtUpdateChainWrapper<T>, KtUpdateWrapper<T>>(),
    ChainUpdate<T>,
    Update<TonyKtUpdateChainWrapper<T>, KProperty1<in T, *>> {
    public constructor(baseDao: BaseDao<T>, entityClass: Class<T>) : this(baseDao) {
        super.wrapperChildren = KtUpdateWrapper(entityClass)
    }

    override fun set(
        condition: Boolean,
        column: KProperty1<in T, *>,
        value: Any?,
        mapping: String?,
    ): TonyKtUpdateChainWrapper<T> {
        wrapperChildren.set(condition, column, value, mapping)
        return typedThis
    }

    override fun setSql(
        condition: Boolean,
        setSql: String,
        vararg params: Any,
    ): TonyKtUpdateChainWrapper<T> {
        wrapperChildren.setSql(condition, setSql, *params)
        return typedThis
    }

    override fun getBaseMapper(): BaseMapper<T> =
        baseDao

    override fun getEntityClass(): Class<T> =
        super.wrapperChildren.entityClass

    /**
     * 物理移除
     * @return [Boolean]
     * @author tangli
     * @date 2024/06/26 13:27
     * @since 1.0.0
     */
    public fun physicalRemove(): Boolean =
        SqlHelper.retBool(baseDao?.physicalDelete(wrapper))
}
