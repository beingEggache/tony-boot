package com.tony;

import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * RowsWrapper is
 *
 * @author tangli
 * @date 2024/07/02 09:03
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public class RowsWrapper<T> implements RowsWrapperLike<T>{

    private Collection<@Valid T> rows;

    public void setRows(Collection<T> rows) {
        this.rows = rows;
    }

    @NotNull
    @Override
    public Collection<? extends T> getRows() {
        return rows;
    }
}
