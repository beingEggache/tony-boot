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

package tony.core.model;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Java Query请求包装
 *
 * @param <T> query 类型
 * @author tangli
 * @date 2023/07/11 19:21
 * @see PageQuery
 */
record PageQueryImpl<T>(
    Object query,
    long page,
    long size,
    Collection<String> ascs,
    Collection<String> descs
) implements PageQuery<T> {

    @SuppressWarnings("unchecked")
    @Override
    public T getQuery() {
        return (T) query;
    }

    @Override
    public long getPage() {
        return page;
    }

    @Override
    public long getSize() {
        return size;
    }

    @NotNull
    @Override
    public Collection<String> getAscs() {
        return ascs;
    }

    @NotNull
    @Override
    public Collection<String> getDescs() {
        return descs;
    }
}
