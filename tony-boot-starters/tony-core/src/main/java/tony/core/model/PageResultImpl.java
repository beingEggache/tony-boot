package tony.core.model;

import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * 全局响应统一分页结构.
 * @param <T>
 * @param rows 列表
 * @param page 当前页
 * @param size 每页数量
 * @param total 总个数
 *
 * @author tangli
 * @date 2021/12/6 10:51
 */
record PageResultImpl<T>(
    Collection<T> rows,
    long page,
    long size,
    long total
) implements PageResult<T> {
    @Override
    public long getPage() {
        return page;
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public long getTotal() {
        return total;
    }

    @Override
    public @NotNull Collection<@Valid T> getRows() {
        return rows;
    }
}
