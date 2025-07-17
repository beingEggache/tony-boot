/*
 * MIT License
 *
 * Copyright (c) 2023-present, tangli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package tony.core.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import tony.core.utils.Cols;
import tony.core.utils.Objs;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Global page response structure.
 *
 * @param <T> rows 的实际类型.
 * @author tangli
 * @date 2021/12/6 10:51
 */
@SuppressWarnings("unused")
@Schema(name = "全局分页响应统一结构")
@JsonPropertyOrder(value = {"page", "size", "total", "pages", "hasNext", "rows"})
public interface PageResultLike<T> extends RowsWrapperLike<T> {

    /**
     * current page.
     *
     * @return current page.
     */
    @Schema(description = "当前页")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    long getPage();

    /**
     * per page item size.
     *
     * @return page item size.
     */
    @Schema(description = "每页条数")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    long getSize();

    /**
     * total pages
     *
     * @return page total pages.
     */
    @Schema(description = "总页数")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    default long getPages(){
        return Math.ceilDiv(getTotal(), getSize());
    }

    /**
     * total item count
     *
     * @return total item count.
     */
    @Schema(description = "总条数")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    long getTotal();

    /**
     * has next page
     *
     * @return has next page.
     */
    @Schema(description = "总条数")
    default boolean getHasNext(){
        return getRows().size() < getSize();
    }

    /**
     * map
     *
     * @param <R>       transform to.
     * @param transform transform function.
     * @return this.
     * @see [List.map]
     */
    @NotNull
    default <R, E extends PageResultLike<R>> E map(final Function<T, R> transform) {
        final Collection<? extends T> rows = Cols.ifEmpty(getRows(), Collections.emptyList());
        return Objs.asToNotNull(
            new PageResult<>(
                rows.stream().map(transform).toList(),
                getPage(),
                getSize(),
                getTotal()
            )
        );
    }

    /**
     * onEach
     *
     * @param action on each.
     * @return this.
     * @see [List.onEach]
     */
    @NotNull
    default <E extends PageResultLike<T>> E onEach(final Consumer<T> action) {
        final Collection<? extends T> rows = Cols.ifEmpty(getRows(), Collections.emptyList());
        return Objs.asToNotNull(
            new PageResult<>(
                rows.stream().peek(action).toList(),
                getPage(),
                getSize(),
                getTotal()
            )
        );
    }

    /**
     * firstOrNull.
     *
     * @param predicate predicate.
     * @return first item.
     */
    @Nullable
    default T firstOrNull(final Predicate<T> predicate) {
        final Collection<? extends T> rows = getRows();
        if (Cols.isNullOrEmpty(rows)) {
            return null;
        }
        return rows.stream().filter(predicate).findFirst().orElse(null);
    }
}
