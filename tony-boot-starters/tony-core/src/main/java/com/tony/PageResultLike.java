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

package com.tony;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.tony.utils.Cols;
import com.tony.utils.Objs;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Global page response structure.
 *
 * @author Tang Li
 * @date 2021/12/6 10:51
 */
@SuppressWarnings("unused")
@JsonPropertyOrder(value = {"page", "size", "total", "pages", "hasNext", "rows"})
public interface PageResultLike<T> {

    /**
     * rows.
     *
     * @return 分页集合.
     */
    Collection<T> getRows();

    /**
     * current page.
     *
     * @return current page.
     */
    long getPage();

    /**
     * per page item size.
     *
     * @return page item size.
     */
    long getSize();

    /**
     * total pages
     *
     * @return page total pages.
     */
    long getPages();

    /**
     * total item count
     *
     * @return total item count.
     */
    long getTotal();

    /**
     * has next page
     *
     * @return has next page.
     */
    boolean getHasNext();

    /**
     * map
     *
     * @param <R>       transform to.
     * @param transform transform function.
     * @return this.
     * @see [List.map]
     */
    default <R, E extends PageResultLike<R>> E map(final Function<T, R> transform) {
        final Collection<T> rows = Cols.ifEmpty(getRows(), Collections.emptyList());
        return Objs.asToNotNull(
            new PageResult<>(
                rows.stream().map(transform).toList(),
                getPage(),
                getSize(),
                getPages(),
                getTotal(),
                getHasNext()
            )
        );
    }

    /**
     * onEach
     *
     * @param action on each.
     * @return this.
     * @see [List.onEach]
     */
    default <E extends PageResultLike<T>> E onEach(final Consumer<T> action) {
        final Collection<T> rows = Cols.ifEmpty(getRows(), Collections.emptyList());
        return Objs.asToNotNull(
            new PageResult<>(
                rows.stream().peek(action).toList(),
                getPage(),
                getSize(),
                getPages(),
                getTotal(),
                getHasNext()
            )
        );
    }

    /**
     * firstOrNull.
     *
     * @param predicate predicate.
     * @return first item.
     */
    default T firstOrNull(final Predicate<T> predicate) {
        final Collection<T> rows = getRows();
        if (Cols.isNullOrEmpty(rows)) {
            return null;
        }
        return rows.stream().filter(predicate).findFirst().orElse(null);
    }
}
