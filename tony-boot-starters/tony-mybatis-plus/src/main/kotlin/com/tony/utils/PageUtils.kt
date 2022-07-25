@file:Suppress("unused", "FunctionName")
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
import com.tony.PageResult
import com.tony.PageResultLike
import com.tony.Pageable
import java.util.Collections

/**
 * Pageable对象转成mybatis的page对象
 */
fun <T> Pageable.toPage(): Page<T> =
    Page<T>().also { page ->
        getPage().also {
            if (it == null || it < 0) {
                page.current = 0L
            } else {
                page.current = it
            }
        }

        getSize().also {
            if (it == null || it <= 0) {
                page.size = 10L
            } else {
                page.size = it
            }
        }

        getDescs()?.forEach { desc ->
            val orderItem = OrderItem.desc(desc?.camelToSnakeCase())
            page.addOrder(orderItem)
        }

        getAscs()?.forEach { asc ->
            val orderItem = OrderItem.asc(asc?.camelToSnakeCase())
            page.addOrder(orderItem)
        }
    }

@Suppress("UNCHECKED_CAST")
fun <T, E : PageResultLike<T>> IPage<T>?.toPageResult(): E =
    if (this == null) {
        EMPTY_PAGE_RESULT as E
    } else {
        PageResult(records, current, size, pages, total, current < pages) as E
    }

@Suppress("UNCHECKED_CAST")
fun <T> emptyPageResult(): PageResult<T> = EMPTY_PAGE_RESULT as PageResult<T>

private val EMPTY_PAGE_RESULT: PageResult<*> = PageResult<Nothing>(Collections.emptyList(), 1L, 0L, 0L, 0L, false)
