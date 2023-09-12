package com.tony;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;

/**
 * @param <T>
 * @author Tang Li
 * @see JPageQueryLike
 * @date 2023/07/11 09:21
 */
@SuppressWarnings("unused")
public class JPageQuery<T> implements JPageQueryLike<T> {

    private static final Logger logger = LoggerFactory.getLogger(JPageQuery.class);
    private Object query;

    private long page = 1L;

    private long size = 10L;

    private Collection<String> ascs = Collections.emptyList();

    private Collection<String> descs = Collections.emptyList();

    public JPageQuery() {
    }

    public JPageQuery(
        long page
    ) {
        this.page = page;
    }

    public JPageQuery(
        long page,
        long size
    ) {
        this.page = page;
        this.size = size;
    }

    public JPageQuery(
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

    public JPageQuery(
        long page,
        Collection<String> ascs,
        Collection<String> descs
    ) {
        this.page = page;
        this.ascs = ascs;
        this.descs = descs;
    }

    public JPageQuery(T query, long page, long size, Collection<String> ascs, Collection<String> descs) {
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
