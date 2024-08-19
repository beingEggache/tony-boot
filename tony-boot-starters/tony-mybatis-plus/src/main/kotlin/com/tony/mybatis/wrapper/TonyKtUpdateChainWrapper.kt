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
        SqlHelper.retBool(baseDao.physicalDelete(wrapper))
}
