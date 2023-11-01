/*
 * MIT License
 *
 * Copyright (c) 2023-present, tangli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
public open class TonyKtQueryChainWrapper<T : Any> internal constructor(private val baseMapper: BaseDao<T>) :
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

        override fun select(
            entityClass: Class<T>,
            predicate: Predicate<TableFieldInfo>,
        ): TonyKtQueryChainWrapper<T> {
            wrapperChildren.select(entityClass, predicate)
            return typedThis
        }

        public fun select(vararg columns: String): TonyKtQueryChainWrapper<T> {
            wrapperChildren.select(*columns)
            return typedThis
        }

        override fun getBaseMapper(): BaseDao<T> =
            baseMapper
    }
