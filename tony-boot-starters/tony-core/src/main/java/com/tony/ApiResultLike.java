package com.tony;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Objects;

/**
 * 全局响应统一结构.
 *
 * @author tangli
 * @since 2021/12/6 10:51
 */
@JsonPropertyOrder(value = {"code", "message", "data"})
public interface ApiResultLike<T> {

    /**
     * 响应体
     *
     * @return data.
     */
    T getData();

    /**
     * 返回码
     *
     * @return code.
     */
    int getCode();

    /**
     * 返回消息
     *
     * @return message.
     */
    CharSequence getMessage();

    /**
     * 是否成功
     *
     * @return boolean
     */
    default boolean getSuccess() {
        return Objects.equals(getCode(), ApiProperty.getOkCode());
    }
}
