/*
 * MIT License
 *
 * Copyright (c) 2023-present, tangli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.tony;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * 压平的全局响应统一结构.
 *
 * @author tangli
 * @date 2023/09/27 19:06
 */
@SuppressWarnings("unused")
class FlattenApiResult<T> implements ApiResultLike<T> {

    @JsonUnwrapped
    private T data;

    private int code;

    private CharSequence message;

    public FlattenApiResult() {
    }

    public FlattenApiResult(final T data, final int code, final CharSequence message) {
        this.data = data;
        this.code = code;
        this.message = message;
    }

    @Override
    public T getData() {
        return data;
    }

    public void setData(final T data) {
        this.data = data;
    }

    @Override
    public int getCode() {
        return code;
    }

    public void setCode(final int code) {
        this.code = code;
    }

    @Override
    public CharSequence getMessage() {
        return message;
    }

    public void setMessage(final CharSequence message) {
        this.message = message;
    }
}
