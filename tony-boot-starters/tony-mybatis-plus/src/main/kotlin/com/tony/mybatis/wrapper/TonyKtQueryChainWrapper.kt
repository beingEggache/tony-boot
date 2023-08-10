package com.tony.mybatis.wrapper

import com.baomidou.mybatisplus.core.conditions.query.Query
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo
import com.baomidou.mybatisplus.extension.conditions.AbstractChainWrapper
import com.baomidou.mybatisplus.extension.conditions.query.ChainQuery
import com.tony.ApiProperty
import com.tony.JPageQueryLike
import com.tony.PageResultLike
import com.tony.mybatis.dao.BaseDao
import com.tony.mybatis.dao.getEntityClass
import com.tony.utils.asToNotNull
import com.tony.utils.throwIf
import com.tony.utils.throwIfNull
import java.util.function.Predicate
import kotlin.reflect.KMutableProperty1
import org.apache.ibatis.exceptions.TooManyResultsException

/**
 * mybatis plus 对应对象的包装, 用来适配一些 kotlin dao方法.
 * 比如 [oneNotNull], [throwIfExists], [pageResult]
 *
 * @author tangli
 * @since 2023/5/25 15:26
 */
@Suppress("MemberVisibilityCanBePrivate")
public open class TonyKtQueryChainWrapper<T : Any>(
    private val baseMapper: BaseDao<T>,
) : AbstractChainWrapper<T, KMutableProperty1<T, *>, TonyKtQueryChainWrapper<T>, TonyKtQueryWrapper<T>>(),
    ChainQuery<T>,
    Query<TonyKtQueryChainWrapper<T>, T, KMutableProperty1<T, *>> {

        init {
            super.wrapperChildren = TonyKtQueryWrapper(baseMapper.getEntityClass())
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

        override fun getEntityClass(): Class<T> = baseMapper.getEntityClass()

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
         * 分页查询出全局统一结构.
         *
         * @param E
         * @param page 全局统一请求分页结构.
         * @return
         */
        public fun <E : PageResultLike<T>> pageResult(
            page: JPageQueryLike<*>,
        ): E = baseMapper.selectPageResult(page, wrapper)

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

        public fun <E> oneObjNotNull(
            message: String = ApiProperty.notFoundMessage,
            code: Int = ApiProperty.notFoundCode,
        ): E = oneObj<E>().throwIfNull(message, code)

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

        public fun oneMapNotNull(
            message: String = ApiProperty.notFoundMessage,
            code: Int = ApiProperty.notFoundCode,
        ): Map<String, Any?> = oneMap().throwIfNull(message, code)
    }
