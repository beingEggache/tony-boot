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
import org.apache.ibatis.exceptions.TooManyResultsException
import java.util.function.Predicate
import kotlin.reflect.KProperty

/**
 * mybatis plus 对应对象的包装, 用来适配一些 kotlin dao方法.
 * 比如 [oneNotNull], [throwIfExists], [pageResult]
 *
 * @author tangli
 * @since 2023/5/25 15:26
 */
public open class TonyKtQueryChainWrapper<T : Any>(
    private val baseMapper: BaseDao<T>,
) : AbstractChainWrapper<T, KProperty<*>, TonyKtQueryChainWrapper<T>, TonyKtQueryWrapper<T>>(),
    ChainQuery<T>,
    Query<TonyKtQueryChainWrapper<T>, T, KProperty<*>> {

    init {
        super.wrapperChildren = TonyKtQueryWrapper(entityClass)
    }

    public constructor(baseMapper: BaseDao<T>, entityClass: Class<T>) : this(baseMapper) {
        super.wrapperChildren = TonyKtQueryWrapper(entityClass)
    }

    public constructor(baseMapper: BaseDao<T>, entity: T) : this(baseMapper) {
        super.wrapperChildren = TonyKtQueryWrapper(entity)
    }

    public fun select(vararg columns: String): TonyKtQueryChainWrapper<T> {
        wrapperChildren.select(*columns)
        return typedThis
    }

    override fun select(vararg columns: KProperty<*>): TonyKtQueryChainWrapper<T> {
        wrapperChildren.select(*columns)
        return typedThis
    }

    override fun select(entityClass: Class<T>, predicate: Predicate<TableFieldInfo>): TonyKtQueryChainWrapper<T> {
        wrapperChildren.select(entityClass, predicate)
        return typedThis
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
        code: Int = ApiProperty.preconditionFailedCode,
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
     * 根据 Wrapper 条件，查询全部记录
     * <p>注意： 只返回第一个字段的值</p>
     */
    public fun listObj(): List<Any?> = baseMapper.selectObjs(wrapper)

    /**
     * 根据 条件，查询一条记录.
     * 查询一条记录，限制取一条记录, 注意：多条数据会报异常
     */
    public fun oneObj(): Any? {
        val list: List<Any?> = listObj()
        // 抄自 DefaultSqlSession#selectOne
        return if (list.size == 1) {
            list[0]
        } else if (list.size > 1) {
            throw TooManyResultsException(
                "Expected one result (or null) to be returned by oneObj(), but found: ${list.size}",
            )
        } else {
            null
        }
    }

    /**
     * 根据 Wrapper 条件，查询全部记录
     */
    public fun listMap(): List<Map<String, Any?>> = baseMapper.selectMaps(wrapper)

    /**
     * 根据 条件，查询一条记录.
     * 查询一条记录，限制取一条记录, 注意：多条数据会报异常
     */
    public fun oneMap(): Map<String, Any?>? {
        val list = listMap()
        // 抄自 DefaultSqlSession#selectOne
        return if (list.size == 1) {
            list[0]
        } else if (list.size > 1) {
            throw TooManyResultsException(
                "Expected one result (or null) to be returned by oneObj(), but found: ${list.size}",
            )
        } else {
            null
        }
    }

    /**
     * 分页查询出全局统一结构.
     *
     * @param E
     * @param page 全局统一请求分页结构.
     * @return
     */
    public fun <E : PageResultLike<T>> pageResult(page: Pageable): E = baseMapper.selectPageResult(page, wrapper)

    override fun getBaseMapper(): BaseDao<T> = baseMapper

    override fun getEntityClass(): Class<T> = baseMapper.getEntityClass()
}
