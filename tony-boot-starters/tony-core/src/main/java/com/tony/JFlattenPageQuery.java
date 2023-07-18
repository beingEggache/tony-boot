package com.tony;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

/**
 * 将 query 放到根节点.
 *
 * @param <T>
 * @author tangli
 * @see JPageQueryLike
 * @since 2023/07/11 09:21
 */
@SuppressWarnings("unused")
public class JFlattenPageQuery<T> implements JPageQueryLike<T> {

    @JsonUnwrapped
    private Object query;

    private long page = 1L;

    private long size = 10L;

    private Collection<String> ascs = Collections.emptyList();

    private Collection<String> descs = Collections.emptyList();


    public JFlattenPageQuery() {
    }

    public JFlattenPageQuery(
        long page
    ) {
        this.page = page;
    }

    public JFlattenPageQuery(
        long page,
        long size
    ) {
        this.page = page;
        this.size = size;
    }

    public JFlattenPageQuery(
        long page,
        long size,
        Collection<String> ascs,
        Collection<String> descs
    ) {
        this.page = page;
        this.size = size;
        this.ascs = ascs;
        this.descs = descs;
    }

    public JFlattenPageQuery(
        long page,
        Collection<String> ascs,
        Collection<String> descs
    ) {
        this.page = page;
        this.ascs = ascs;
        this.descs = descs;
    }

    public JFlattenPageQuery(T query, long page, long size, Collection<String> ascs, Collection<String> descs) {
        this.query = query;
        this.page = page;
        this.size = size;
        this.ascs = ascs;
        this.descs = descs;
    }

    @NotNull
    @SuppressWarnings("unchecked")
    @Override
    public T getQuery() {
        return (T) query;
    }

    public void setQuery(T query) {
        this.query = query;
    }

    @Override
    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }

    @Override
    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @NotNull
    @Override
    public Collection<String> getAscs() {
        return ascs;
    }

    public void setAscs(Collection<String> ascs) {
        this.ascs = ascs;
    }

    @NotNull
    @Override
    public Collection<String> getDescs() {
        return descs;
    }

    public void setDescs(Collection<String> descs) {
        this.descs = descs;
    }
}
