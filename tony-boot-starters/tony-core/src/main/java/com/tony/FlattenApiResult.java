package com.tony;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

@SuppressWarnings("unused")
class FlattenApiResult<T> implements ApiResultLike<T> {

    @JsonUnwrapped
    private T data;

    private int code;

    private CharSequence message;

    public FlattenApiResult() {
    }

    public FlattenApiResult(T data, int code, CharSequence message) {
        this.data = data;
        this.code = code;
        this.message = message;
    }

    @Override
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public CharSequence getMessage() {
        return message;
    }

    public void setMessage(CharSequence message) {
        this.message = message;
    }
}
