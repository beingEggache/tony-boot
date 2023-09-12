package com.tony;

import java.util.Collection;

/**
 * Global collection wrapper.
 *
 * @author Tang Li
 * @date 2021/12/6 10:51
 */
@SuppressWarnings("unused")
public interface ItemsWrapper<T> {

    /**
     * 返回包装集合对象.
     *
     * @return 集合对象
     */
    Collection<? extends T> getItems();
}
