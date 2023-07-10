package com.tony;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Collection;

/**
 * 全局统一请求分页结构.
 *
 * @author tangli
 * @since 2021/12/6 10:51
 */
public interface PageQueryLike<T> {


    @Valid
    T getQuery();

    /**
     * 页码,当前页
     */
    @Positive(message = "页码请输入正数")
    long getPage();

    /**
     * 每页数量
     */
    @Positive(message = "每页数量请输入正数")
    long getSize();

    /**
     * 升序字段
     */
    Collection<? extends CharSequence> getAscs();

    /**
     * 降序字段
     */
    Collection<? extends CharSequence> getDescs();
}
