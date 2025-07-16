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

@file:JvmName("Pages")

/**
 * PageUtils
 *
 * @author tangli
 * @date 2022/07/13 19:18
 */

package tony.mybatis.utils

import com.baomidou.mybatisplus.core.metadata.IPage
import com.baomidou.mybatisplus.core.metadata.OrderItem
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import java.util.Collections
import tony.core.model.PageQueryLike
import tony.core.model.PageResult
import tony.core.utils.asToNotNull
import tony.core.utils.camelToSnakeCase

/**
 * Pageable对象转成mybatis的page对象
 * @return [IPage]<[T]>
 * @author tangli
 * @date 2023/09/28 19:55
 */
public fun <T> PageQueryLike<*>.toPage(): IPage<T> =
    Page<T>().also { page ->
        page.current = this.page.takeIf { it > 0 } ?: 1L
        page.size = this.size.takeIf { it > 0 } ?: 10L
        descs
            .filter { !it.isNullOrBlank() }
            .map { OrderItem.desc(it.camelToSnakeCase()) }
            .let { page.addOrder(it) }
        ascs
            .filter { !it.isNullOrBlank() }
            .map { OrderItem.asc(it.camelToSnakeCase()) }
            .let { page.addOrder(it) }
    }

/**
 * 将 mybatis-plus 的分页对象改为全局统一分页结构
 * @return [PageResult]
 * @author tangli
 * @date 2023/09/28 19:55
 */
public fun <T> IPage<T>?.toPageResult(): PageResult<T> =
    if (this == null) {
        EMPTY_PAGE_RESULT
    } else {
        PageResult(records, current, size, total)
    }.asToNotNull()

/**
 * 空分页结构.
 * @return
 */
private val EMPTY_PAGE_RESULT: PageResult<*> = PageResult<Nothing>(Collections.emptyList(), 1L, 0L, 0L)
