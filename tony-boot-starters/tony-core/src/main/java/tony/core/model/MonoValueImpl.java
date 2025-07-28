package tony.core.model;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * MonoValueImpl is
 *
 * @author tangli
 * @date 2025/07/28 14:01
 */
record MonoValueImpl<T>(T value) implements MonoValue<T> {

    @JsonUnwrapped
    @Override
    public T getValue() {
        return value;
    }
}
