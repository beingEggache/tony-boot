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

/**
 * mybatis plus 对应对象的包装, 用来适配一些 kotlin dao方法.
 * 比如 [oneNotNull], [throwIfExists], [pageResult]
 *
 * @author Tang Li
 * @date 2023/05/25 19:26
 */
public open class TonyQueryChainWrapper<T : Any> internal constructor(
    private val baseMapper: BaseDao<T>,
) : AbstractChainWrapper<T, String, TonyQueryChainWrapper<T>, TonyQueryWrapper<T>>(),
    TonyChainQuery<T>,
    Query<TonyQueryChainWrapper<T>, T, String> {
    init {
        wrapperChildren = TonyQueryWrapper(baseMapper.getEntityClass())
    }

    override fun select(
        condition: Boolean,
        columns: MutableList<String>,
    ): TonyQueryChainWrapper<T> {
        wrapperChildren.select(condition, columns)
        return typedThis
    }

    override fun select(
        entityClass: Class<T>,
        predicate: Predicate<TableFieldInfo>,
    ): TonyQueryChainWrapper<T> {
        wrapperChildren.select(entityClass, predicate)
        return typedThis
    }

    override fun select(vararg columns: String): TonyQueryChainWrapper<T> {
        wrapperChildren.select(*columns)
        return typedThis
    }

    override fun getBaseMapper(): BaseDao<T> =
        baseMapper
}
