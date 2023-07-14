package com.tony;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Collection;

/**
 * Global page request structure.
 *
 * @author tangli
 * @since 2021/12/6 10:51
 */
public interface PageQueryLike<T> {


    /**
     * query condition.
     *
     * @return query.
     */
    @Valid
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
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    Collection<? extends CharSequence> getAscs();

    /**
     * desc fields.
     *
     * @return desc fields.
     */
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    Collection<? extends CharSequence> getDescs();
}
