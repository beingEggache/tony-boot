package tony.core.model;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 简单值响应统一结构。
 * 避免重复定义简单值响应包装类.
 *
 * @author tangli
 * @date 2025/07/17 09:32
 */
public interface MonoResultLike<T> {
    @Schema(description = "结果")
    @JsonSetter(nulls = Nulls.AS_EMPTY, contentNulls = Nulls.AS_EMPTY)
    T getResult();
}
