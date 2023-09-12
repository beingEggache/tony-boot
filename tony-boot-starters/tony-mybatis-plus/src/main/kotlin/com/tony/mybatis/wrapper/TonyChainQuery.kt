package com.tony.mybatis.wrapper

import com.baomidou.mybatisplus.extension.conditions.query.ChainQuery
import com.tony.ApiProperty
import com.tony.JPageQueryLike
import com.tony.PageResultLike
import com.tony.mybatis.dao.BaseDao
import com.tony.mybatis.dao.getEntityClass
import com.tony.utils.asToNotNull
import com.tony.utils.throwIf
import com.tony.utils.throwIfNull
import org.apache.ibatis.exceptions.TooManyResultsException

/**
 * TonyChainQuery is
 * @author Tang Li
 * @date 2023/08/11 09:18
 */
public interface TonyChainQuery<T : Any> : ChainQuery<T> {

    override fun getBaseMapper(): BaseDao<T>

    /**
     * 查出不可空的单个实体, 为空时抛错.
     * @param message 默认为 [ApiProperty.notFoundMessage]
     * @return
     */
    public fun oneNotNull(message: String): T =
        baseMapper
            .selectOne(wrapper)
            .throwIfNull(message, ApiProperty.notFoundCode)

    /**
     * 查出不可空的单个实体, 为空时抛错.
     * @return
     */
    public fun oneNotNull(): T =
        baseMapper
            .selectOne(wrapper)
            .throwIfNull(ApiProperty.notFoundMessage, ApiProperty.notFoundCode)

    /**
     * 查询某个条件是否存在, 存在就抛错
     * @param message
     */
    public fun throwIfExists(message: String) {
        throwIf(exists(), message, ApiProperty.notFoundCode)
    }

    /**
     * 查询某个条件是否不存在, 不存在就抛错
     *
     * @param message
     */
    public fun throwIfNotExists(message: String) {
        throwIf(!exists(), message, ApiProperty.notFoundCode)
    }

    /**
     * 分页查询出全局统一结构.
     *
     * @param page 全局统一请求分页结构.
     * @return
     */
    public fun <E : PageResultLike<T>> pageResult(page: JPageQueryLike<*>): E =
        baseMapper.selectPageResult(page, wrapper)

    /**
     * 根据 Wrapper 条件，查询全部记录.
     *
     * 注意： 只返回第一个字段的值.
     *
     * @param E
     * @return
     */
    public fun <E> listObj(): List<E?> = baseMapper.selectObjs(wrapper).asToNotNull()

    /**
     * 根据 条件，查询一条记录.
     *
     * 查询一条记录，限制取一条记录, 注意：多条数据会报异常.
     *
     * 注意： 只返回第一个字段的值.
     *
     * @param E
     * @return
     */
    public fun <E> oneObj(): E? {
        val list: List<E?> = listObj<E>()
        // 抄自 DefaultSqlSession#selectOne
        return if (list.size == 1) {
            list[0]
        } else if (list.size > 1) {
            throw TooManyResultsException(
                "Expected one result (or null) to be returned by oneObj(), but found: ${list.size}"
            )
        } else {
            null
        }
    }

    public fun <E> oneObjNotNull(message: String): E =
        oneObj<E>().throwIfNull(message, ApiProperty.notFoundCode)

    public fun <E> oneObjNotNull(): E =
        oneObj<E>().throwIfNull(ApiProperty.notFoundMessage, ApiProperty.notFoundCode)

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
                "Expected one result (or null) to be returned by oneObj(), but found: ${list.size}"
            )
        } else {
            null
        }
    }

    public fun oneMapNotNull(message: String): Map<String, Any?> =
        oneMap().throwIfNull(message, ApiProperty.notFoundCode)

    public fun oneMapNotNull(): Map<String, Any?> =
        oneMap().throwIfNull(ApiProperty.notFoundMessage, ApiProperty.notFoundCode)

    override fun getEntityClass(): Class<T> = baseMapper.getEntityClass()
}
