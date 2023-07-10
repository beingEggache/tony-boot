package com.tony;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Global page response structure.
 *
 * @author tangli
 * @since 2021/12/6 10:51
 */
@SuppressWarnings("unused")
public interface PageResultLike<T> {

    /**
     * items
     */
    Collection<T> getItems();

    /**
     * current page
     */
    long getPage();

    /**
     * per page item size
     */
    long getSize();

    /**
     * total pages
     */
    long getPages();

    /**
     * total item count
     */
    long getTotal();

    /**
     * has next page
     */
    boolean getHasNext();

    /**
     * map
     *
     * @see [List.map]
     */
    default <R> PageResultLike<R> map(Function<T, R> transform) {
        Collection<T> items = getItems();
        if (items == null || items.size() == 0) {
            items = Collections.emptyList();
        }
        return new PageResult<>(
            items.stream().map(transform).collect(Collectors.toList()),
            getPage(),
            getSize(),
            getPages(),
            getTotal(),
            getHasNext()
        );
    }

    /**
     * onEach
     *
     * @see [List.onEach]
     */
    default PageResultLike<T> onEach(Consumer<T> action) {
        Collection<T> items = getItems();
        if (items == null || items.size() == 0) {
            items = Collections.emptyList();
        }

        return new PageResult<>(
            items.stream().peek(action).collect(Collectors.toList()),
            getPage(),
            getSize(),
            getPages(),
            getTotal(),
            getHasNext()
        );
    }

    default T firstOrNull(Predicate<T> predicate) {
        Collection<T> items = getItems();
        if (items == null || items.size() == 0) {
            items = Collections.emptyList();
        }
        return items.stream().filter(predicate).findFirst().orElse(null);
    }
}
