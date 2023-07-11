package com.tony;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.util.Collection;
import java.util.Collections;

/**
 * 将 query 放到根节点.
 * @see com.tony.PageQueryLike
 * @param <T>
 * @author tangli
 * @since 2023/07/11 09:21
 */
@SuppressWarnings("unused")
public class FlattenPageQuery<T> implements PageQueryLike<T> {

    @JsonUnwrapped
    private T query;

    private long page = 1L;

    private long size = 10L;

    private Collection<String> ascs = Collections.emptyList();

    private Collection<String> descs = Collections.emptyList();


    public FlattenPageQuery() {
    }

    public FlattenPageQuery(
        long page
    ) {
        this.page = page;
    }

    public FlattenPageQuery(
        long page,
        long size
    ) {
        this.page = page;
        this.size = size;
    }

    public FlattenPageQuery(
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

    public FlattenPageQuery(
        long page,
        Collection<String> ascs,
        Collection<String> descs
    ) {
        this.page = page;
        this.ascs = ascs;
        this.descs = descs;
    }

    public FlattenPageQuery(T query, long page, long size, Collection<String> ascs, Collection<String> descs) {
        this.query = query;
        this.page = page;
        this.size = size;
        this.ascs = ascs;
        this.descs = descs;
    }

    @Override
    public T getQuery() {
        return query;
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

    @Override
    public Collection<String> getAscs() {
        return ascs;
    }

    public void setAscs(Collection<String> ascs) {
        this.ascs = ascs;
    }

    @Override
    public Collection<String> getDescs() {
        return descs;
    }

    public void setDescs(Collection<String> descs) {
        this.descs = descs;
    }
}
