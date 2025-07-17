package tony.core.model;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * 简单值请求统一结构。
 * 避免重复定义简单值请求包装类.
 *
 * @author tangli
 * @date 2025/07/17 09:32
 */
@SuppressWarnings("unused")
public class MonoValue<T> implements MonoValueLike<T> {

    @JsonUnwrapped
    private Object value;

    public MonoValue() {
    }

    public MonoValue(T value) {
        this.value = value;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T getValue() {
        return (T) value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
