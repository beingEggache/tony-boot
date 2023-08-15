@file:JvmName("PageUtils")

/**
 * PageUtils
 *
 * @author tangli
 * @since 2022/7/13 15:18
 */

package com.tony.utils

import com.baomidou.mybatisplus.core.metadata.IPage
import com.baomidou.mybatisplus.core.metadata.OrderItem
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.tony.JPageQueryLike
import com.tony.PageResult
import com.tony.PageResultLike
import java.util.Collections

/**
 * Pageable对象转成mybatis的page对象
 * @receiver [JPageQueryLike]
 * @return mybatis 分页对象, 一般用来查询.
 */
public fun <T> JPageQueryLike<*>.toPage(): IPage<T> =
    Page<T>().also { page ->
        page.current = this.page.takeIf { it > 0 } ?: 1L
        page.size = this.size.takeIf { it > 0 } ?: 10L
        descs
            .filterNotNull()
            .filter { it.isNotBlank() }
            .takeIf { it.any() }
            ?.map { OrderItem.desc(it.camelToSnakeCase()) }
            ?.let { page.addOrder(it) }
        ascs
            .filterNotNull()
            .filter { it.isNotBlank() }
            .takeIf { it.any() }
            ?.map { OrderItem.asc(it.camelToSnakeCase()) }
            ?.let { page.addOrder(it) }
    }

/**
 * 将 mybatis-plus 的分页对象改为全局统一分页结构
 *
 * @receiver [IPage]
 * @param T
 * @return
 */
public fun <T> IPage<T>?.toPageResult(): PageResultLike<T> =
    if (this == null) {
        EMPTY_PAGE_RESULT
    } else {
        PageResult(records, current, size, pages, total, current < pages)
    }.asToNotNull()

/**
 * 空分页结构.
 * @param T
 * @return
 */
public fun <T> emptyPageResult(): PageResultLike<T> = EMPTY_PAGE_RESULT.asToNotNull()

private val EMPTY_PAGE_RESULT: PageResult<*> = PageResult<Nothing>(Collections.emptyList(), 1L, 0L, 0L, 0L, false)
