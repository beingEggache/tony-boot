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
import com.tony.PageQueryLike
import com.tony.PageResultLike
import com.tony.mybatis.dao.BaseDao
import com.tony.mybatis.dao.getEntityClass
import com.tony.utils.throwIf
import com.tony.utils.throwIfNull
import java.util.function.Predicate

/**
 * mybatis plus 对应对象的包装, 用来适配一些 kotlin dao方法.
 *
 * @author tangli
 * @since 2023/5/25 15:33
 */
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
    @SafeVarargs
    override fun select(vararg columns: SFunction<T, *>): TonyLambdaQueryChainWrapper<T> = apply {
        wrapperChildren.select(*columns)
    }

    /**
     * 查出不可空的单个实体, 为空时抛错.
     * @param message 默认为 [ApiProperty.notFoundMessage]
     * @param code 默认为 [ApiProperty.notFoundCode]
     * @return
     */
    @JvmOverloads
    public fun oneNotNull(
        message: String = ApiProperty.notFoundMessage,
        code: Int = ApiProperty.notFoundCode,
    ): T = baseMapper.selectOne(wrapper).throwIfNull(message, code)

    /**
     * 查询某个条件是否存在, 存在就抛错
     * @param message
     * @param code 默认为 [ApiProperty.preconditionFailedCode]
     */
    @JvmOverloads
    public fun throwIfExists(
        message: String,
        code: Int = ApiProperty.notFoundCode,
    ) {
        throwIf(exists(), message, code)
    }

    /**
     * 查询某个条件是否存在, 不存在就抛错
     *
     * @param message
     * @param code 默认为 [ApiProperty.notFoundCode]
     */
    @JvmOverloads
    public fun throwIfNotExists(
        message: String,
        code: Int = ApiProperty.notFoundCode,
    ) {
        throwIf(!exists(), message, code)
    }

    /**
     * 分页查询出全局统一结构.
     *
     * @param E
     * @param page 全局统一请求分页结构.
     * @return
     */
    public fun <E : PageResultLike<T>> pageResult(page: PageQueryLike): E = baseMapper.selectPageResult(page, wrapper)

    override fun select(
        entityClass: Class<T>,
        predicate: Predicate<TableFieldInfo>,
    ): TonyLambdaQueryChainWrapper<T> = apply {
        wrapperChildren.select(entityClass, predicate)
    }

    override fun getEntityClass(): Class<T> = baseMapper.getEntityClass()
}
