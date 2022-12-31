/**
 * tony-dependencies
 * Wrappers
 *
 * @author tangli
 * @since 2022/7/21 13:41
 */
package com.tony.mybatis.wrapper

import com.baomidou.mybatisplus.core.conditions.query.Query
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo
import com.baomidou.mybatisplus.core.toolkit.support.SFunction
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

@Suppress("unused")
public open class TonyLambdaQueryChainWrapper<T : Any>(private val baseMapper: BaseDao<T>) :
    AbstractChainWrapper<T, SFunction<T, *>, TonyLambdaQueryChainWrapper<T>, TonyLambdaQueryWrapper<T>>(),
    ChainQuery<T>,
    Query<TonyLambdaQueryChainWrapper<T>, T, SFunction<T, *>> {

    init {
        super.wrapperChildren = TonyLambdaQueryWrapper()
    }

    /**
     * 获取 BaseMapper
     *
     * @return BaseMapper
     */
    override fun getBaseMapper(): BaseDao<T> = baseMapper

    /**
     * ignore
     */
    @SuppressWarnings(value = ["varargs", "unchecked"])
    @SafeVarargs
    override fun select(vararg columns: SFunction<T, *>): TonyLambdaQueryChainWrapper<T> = apply {
        wrapperChildren.select(*columns)
    }

    @JvmOverloads
    public fun oneNotNull(
        message: String = ApiProperty.notFoundMessage,
        code: Int = ApiProperty.notFoundCode
    ): T = baseMapper.selectOne(wrapper).throwIfNull(message, code)

    @JvmOverloads
    public fun throwIfExists(
        message: String,
        code: Int = ApiProperty.notFoundCode
    ) {
        throwIf(exists(), message, code)
    }

    @JvmOverloads
    public fun throwIfNotExists(
        message: String,
        code: Int = ApiProperty.notFoundCode
    ) {
        throwIf(!exists(), message, code)
    }

    public fun <E : PageResultLike<T>> pageResult(page: Pageable): E = baseMapper.selectPageResult(page, wrapper)

    override fun select(
        entityClass: Class<T>,
        predicate: Predicate<TableFieldInfo>
    ): TonyLambdaQueryChainWrapper<T> = apply {
        wrapperChildren.select(entityClass, predicate)
    }

    override fun getEntityClass(): Class<T> = baseMapper.getEntityClass()
}
