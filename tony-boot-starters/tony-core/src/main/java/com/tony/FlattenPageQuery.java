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

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

/**
 * 将 query 放到根节点.
 *
 * @param <T>
 * @author Tang Li
 * @see PageQueryLike
 * @date 2023/07/11 19:21
 */
@SuppressWarnings("unused")
public class FlattenPageQuery<T> implements PageQueryLike<T> {

    @JsonUnwrapped
    private Object query;

    private long page = 1L;

    private long size = 10L;

    private Collection<String> ascs = Collections.emptyList();

    private Collection<String> descs = Collections.emptyList();


    public FlattenPageQuery() {
    }

    public FlattenPageQuery(
            final long page
    ) {
        this.page = page;
    }

    public FlattenPageQuery(
            final long page,
            final long size
    ) {
        this.page = page;
        this.size = size;
    }

    public FlattenPageQuery(
            final long page,
            final long size,
            final Collection<String> ascs,
            final Collection<String> descs
    ) {
        this.page = page;
        this.size = size;
        this.ascs = ascs;
        this.descs = descs;
    }

    public FlattenPageQuery(
            final long page,
            final Collection<String> ascs,
            final Collection<String> descs
    ) {
        this.page = page;
        this.ascs = ascs;
        this.descs = descs;
    }

    public FlattenPageQuery(final T query, final long page, final long size, final Collection<String> ascs, final Collection<String> descs) {
        this.query = query;
        this.page = page;
        this.size = size;
        this.ascs = ascs;
        this.descs = descs;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T getQuery() {
        return (T) query;
    }

    public void setQuery(final T query) {
        this.query = query;
    }

    @Override
    public long getPage() {
        return page;
    }

    public void setPage(final long page) {
        this.page = page;
    }

    @Override
    public long getSize() {
        return size;
    }

    public void setSize(final long size) {
        this.size = size;
    }

    @NotNull
    @Override
    public Collection<String> getAscs() {
        return ascs;
    }

    public void setAscs(final Collection<String> ascs) {
        this.ascs = ascs;
    }

    @NotNull
    @Override
    public Collection<String> getDescs() {
        return descs;
    }

    public void setDescs(final Collection<String> descs) {
        this.descs = descs;
    }

    @Override
    public String toString() {
        return String.format("FlattenPageQuery(page=%s, size=%s, ascs=%s, descs=%s, query=%s)", page, size, ascs, descs, query);
    }
}
