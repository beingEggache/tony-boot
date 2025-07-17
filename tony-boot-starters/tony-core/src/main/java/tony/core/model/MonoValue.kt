package tony.core.model

import com.fasterxml.jackson.annotation.JsonUnwrapped
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull

/**
 * 简单值请求统一结构。
 *
 * 避免重复定义简单值请求包装类.
 * @author tangli
 * @date 2025/07/17 09:32
 */
@Schema(name = "简单值请求统一结构")
public class MonoValue<out T> {
    @get:Valid
    @get:JsonUnwrapped
    @get:NotNull(message = "请输入")
    public val value: T? = null
}
