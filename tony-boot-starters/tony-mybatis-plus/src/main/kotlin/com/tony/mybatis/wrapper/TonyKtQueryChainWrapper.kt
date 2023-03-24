package com.tony.mybatis.wrapper

import com.baomidou.mybatisplus.core.conditions.query.Query
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo
import com.baomidou.mybatisplus.extension.conditions.AbstractChainWrapper
import com.baomidou.mybatisplus.extension.conditions.query.ChainQuery
import com.tony.ApiProperty
import com.tony.PageResultLike
import com.tony.Pageable
import com.tony.mybatis.dao.BaseDao
import com.tony.mybatis.dao.getEntityClass
import com.tony.utils.throwIf
import com.tony.utils.throwIfNull
import java.util.function.Predicate
import kotlin.reflect.KProperty

public open class TonyKtQueryChainWrapper<T : Any>(
    private val baseMapper: BaseDao<T>,
) : AbstractChainWrapper<T, KProperty<*>, TonyKtQueryChainWrapper<T>, TonyKtQueryWrapper<T>>(),
    ChainQuery<T>,
    Query<TonyKtQueryChainWrapper<T>, T, KProperty<*>> {

    public constructor(baseMapper: BaseDao<T>, entityClass: Class<T>) : this(baseMapper) {
        super.wrapperChildren = TonyKtQueryWrapper(entityClass)
    }

    public constructor(baseMapper: BaseDao<T>, entity: T) : this(baseMapper) {
        super.wrapperChildren = TonyKtQueryWrapper(entity)
    }

    override fun select(vararg columns: KProperty<*>): TonyKtQueryChainWrapper<T> {
        wrapperChildren.select(*columns)
        return typedThis
    }

    override fun select(entityClass: Class<T>, predicate: Predicate<TableFieldInfo>): TonyKtQueryChainWrapper<T> {
        wrapperChildren.select(entityClass, predicate)
        return typedThis
    }

    @JvmOverloads
    public fun oneNotNull(
        message: String = ApiProperty.notFoundMessage,
        code: Int = ApiProperty.notFoundCode,
    ): T = baseMapper.selectOne(wrapper).throwIfNull(message, code)

    @JvmOverloads
    public fun throwIfExists(
        message: String,
        code: Int = ApiProperty.notFoundCode,
    ) {
        throwIf(exists(), message, code)
    }

    @JvmOverloads
    public fun throwIfNotExists(
        message: String,
        code: Int = ApiProperty.notFoundCode,
    ) {
        throwIf(!exists(), message, code)
    }

    public fun <E : PageResultLike<T>> pageResult(page: Pageable): E = baseMapper.selectPageResult(page, wrapper)

    override fun getBaseMapper(): BaseDao<T> = baseMapper

    override fun getEntityClass(): Class<T> = baseMapper.getEntityClass()
}
