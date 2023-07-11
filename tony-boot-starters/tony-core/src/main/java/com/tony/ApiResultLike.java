package com.tony;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

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
     * @return data.
     */
    T getData();

    /**
     * 返回码
     * @return code.
     */
    int getCode();

    /**
     * 返回消息
     * @return message.
     */
    CharSequence getMessage();
}
