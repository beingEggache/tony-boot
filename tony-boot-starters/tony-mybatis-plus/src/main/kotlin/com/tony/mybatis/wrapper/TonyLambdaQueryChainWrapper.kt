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
import com.tony.mybatis.dao.BaseDao
import com.tony.mybatis.dao.getEntityClass
import java.util.function.Predicate

/**
 * mybatis plus 对应对象的包装, 用来适配一些 kotlin dao方法.
 *
 * @author tangli
 * @since 2023/5/25 15:33
 */
public open class TonyLambdaQueryChainWrapper<T : Any>(private val baseMapper: BaseDao<T>) :
    AbstractChainWrapper<T, SFunction<T, *>, TonyLambdaQueryChainWrapper<T>, TonyLambdaQueryWrapper<T>>(),
    TonyChainQuery<T>,
    Query<TonyLambdaQueryChainWrapper<T>, T, SFunction<T, *>> {

        init {
            super.wrapperChildren = TonyLambdaQueryWrapper(baseMapper.getEntityClass())
        }

        override fun select(condition: Boolean, columns: List<SFunction<T, *>?>): TonyLambdaQueryChainWrapper<T> {
            wrapperChildren.select(condition, columns)
            return typedThis
        }

        override fun select(
            entityClass: Class<T>,
            predicate: Predicate<TableFieldInfo>,
        ): TonyLambdaQueryChainWrapper<T> {
            wrapperChildren.select(entityClass, predicate)
            return typedThis
        }

        override fun getBaseMapper(): BaseDao<T> = baseMapper
    }
