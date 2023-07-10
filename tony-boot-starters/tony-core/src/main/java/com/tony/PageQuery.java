package com.tony;

import java.util.Collection;
import java.util.Collections;

@SuppressWarnings("unused")
public class PageQuery<T> implements PageQueryLike<T> {

    private T query;

    private long page = 1L;

    private long size = 10L;

    private Collection<String> ascs = Collections.emptyList();

    private Collection<String> descs = Collections.emptyList();

    public PageQuery() {
    }

    public PageQuery(
        long page
    ) {
        this.page = page;
    }

    public PageQuery(
        long page,
        long size
    ) {
        this.page = page;
        this.size = size;
    }

    public PageQuery(
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

    public PageQuery(
        long page,
        Collection<String> ascs,
        Collection<String> descs
    ) {
        this.page = page;
        this.ascs = ascs;
        this.descs = descs;
    }

    public PageQuery(T query, long page, long size, Collection<String> ascs, Collection<String> descs) {
        this.query = query;
        this.page = page;
        this.size = size;
        this.ascs = ascs;
        this.descs = descs;
    }

    public T getQuery() {
        return query;
    }

    public void setQuery(T query) {
        this.query = query;
    }

    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Collection<String> getAscs() {
        return ascs;
    }

    public void setAscs(Collection<String> ascs) {
        this.ascs = ascs;
    }

    public Collection<String> getDescs() {
        return descs;
    }

    public void setDescs(Collection<String> descs) {
        this.descs = descs;
    }
}
