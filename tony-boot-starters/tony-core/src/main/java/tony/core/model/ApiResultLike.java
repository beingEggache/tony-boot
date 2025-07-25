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

package tony.core.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.jetbrains.annotations.NotNull;
import tony.core.ApiProperty;

import java.util.Objects;

/**
 * 全局响应统一结构.
 *
 * @param <T> data 类型
 * @author tangli
 * @date 2021/12/6 10:51
 */
@JsonPropertyOrder(value = {"success", "code", "message", "data"})
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
    @NotNull
    CharSequence getMessage();

    /**
     * 是否成功
     *
     * @return boolean
     */
    default boolean getSuccess() {
        return Objects.equals(getCode(), ApiProperty.okCode());
    }
}
