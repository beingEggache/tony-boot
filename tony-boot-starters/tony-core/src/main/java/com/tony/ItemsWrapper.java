package com.tony;

import java.util.Collection;

/**
 * Global collection wrapper.
 *
 * @author tangli
 * @since 2021/12/6 10:51
 */
@SuppressWarnings("unused")
public interface ItemsWrapper<T> {

    Collection<? extends T> getItems();
}
