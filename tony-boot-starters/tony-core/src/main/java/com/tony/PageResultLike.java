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
     * @return 分页集合.
     */
    Collection<T> getItems();

    /**
     * current page.
     * @return current page.
     */
    long getPage();

    /**
     * per page item size.
     * @return page item size.
     */
    long getSize();

    /**
     * total pages
     * @return page total pages.
     */
    long getPages();

    /**
     * total item count
     * @return total item count.
     */
    long getTotal();

    /**
     * has next page
     * @return has next page.
     */
    boolean getHasNext();

    /**
     * map
     *
     * @param <R> transform to.
     * @param transform transform function.
     * @see [List.map]
     * @return this.
     */
    default <R> PageResultLike<R> map(Function<T, R> transform) {
        Collection<T> items = getItems();
        if (items == null || items.isEmpty()) {
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
     * @param action on each.
     * @see [List.onEach]
     * @return this.
     */
    default PageResultLike<T> onEach(Consumer<T> action) {
        Collection<T> items = getItems();
        if (items == null || items.isEmpty()) {
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

    /**
     * firstOrNull.
     * @param predicate predicate.
     * @return first item.
     */
    default T firstOrNull(Predicate<T> predicate) {
        Collection<T> items = getItems();
        if (items == null || items.isEmpty()) {
            items = Collections.emptyList();
        }
        return items.stream().filter(predicate).findFirst().orElse(null);
    }
}
