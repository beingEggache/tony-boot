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

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Global page request structure.
 *
 * @param <T> query类型
 * @author tangli
 * @date 2021/12/6 10:51
 */
@Schema(name = "分页请求统一结构")
@JsonPropertyOrder(value = {"page", "size", "query", "ascs", "descs"})
public interface PageQueryLike<T> {

    /**
     * query condition.
     *
     * @return query.
     */
    @Schema(description = "查询对象、值")
    @Valid
    @JsonSetter(nulls = Nulls.AS_EMPTY, contentNulls = Nulls.AS_EMPTY)
    T getQuery();

    /**
     * current page.
     *
     * @return current page.
     */
    @Schema(description = "当前页")
    @Positive(message = "页码请输入正数")
    long getPage();

    /**
     * size per page.
     *
     * @return size per page.
     */
    @Schema(description = "每页条数")
    @Positive(message = "每页数量请输入正数")
    long getSize();

    /**
     * asc fields.
     *
     * @return asc fields.
     */
    @Schema(description = "升序排序字段")
    @NotNull
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    Collection<? extends CharSequence> getAscs();

    /**
     * desc fields.
     *
     * @return desc fields.
     */
    @Schema(description = "降序排序字段")
    @NotNull
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    Collection<? extends CharSequence> getDescs();
}
