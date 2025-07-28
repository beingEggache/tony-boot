package tony.core.model;

import org.jetbrains.annotations.NotNull;

/**
 * 全局响应统一结构.
 *
 * @param <T> 响应体对象类型. 不支持 [Boolean] ,[CharSequence], [Number], [Enum].
 * @author tangli
 * @date 2021/12/6 10:51
 */
public record ApiResultImpl<T>(
    T data,
    int code,
    String message
) implements ApiResult<T> {
    @Override
    public T getData() {
        return data;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public @NotNull String getMessage() {
        return message;
    }
}
