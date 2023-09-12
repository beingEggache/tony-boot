package com.tony;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Global page request structure.
 *
 * @author Tang Li
 * @date 2021/12/6 10:51
 */
public interface JPageQueryLike<T> {


    /**
     * query condition.
     *
     * @return query.
     */
    @Valid
    @JsonSetter(nulls = Nulls.AS_EMPTY, contentNulls = Nulls.AS_EMPTY)
    T getQuery();

    /**
     * current page.
     *
     * @return current page.
     */
    @Positive(message = "页码请输入正数")
    long getPage();

    /**
     * size per page.
     *
     * @return size per page.
     */
    @Positive(message = "每页数量请输入正数")
    long getSize();

    /**
     * asc fields.
     *
     * @return asc fields.
     */
    @NotNull
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    Collection<? extends CharSequence> getAscs();

    /**
     * desc fields.
     *
     * @return desc fields.
     */
    @NotNull
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    Collection<? extends CharSequence> getDescs();
}
