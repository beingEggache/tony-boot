package com.tony;

/**
 * EncryptApiResult is
 *
 * @author tangli
 * @since 2023/08/08 11:30
 */
public class EncryptApiResult implements ApiResultLike<String>{

    private String data;

    private int code;

    private CharSequence message;
    @Override
    public String getData() {
        return data;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public CharSequence getMessage() {
        return message;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(CharSequence message) {
        this.message = message;
    }
}
