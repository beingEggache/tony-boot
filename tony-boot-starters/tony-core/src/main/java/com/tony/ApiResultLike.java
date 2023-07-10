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
     */
    T getData();

    /**
     * 返回码
     */
    int getCode();

    /**
     * 返回消息
     */
    CharSequence getMessage();
}
