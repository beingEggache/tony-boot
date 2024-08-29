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

package com.tony.fus

import com.fasterxml.jackson.core.type.TypeReference
import com.tony.fus.cache.DefaultFusCache
import com.tony.fus.cache.FusCache
import com.tony.fus.extension.fusThrowIfNull
import com.tony.fus.extension.fusThrowIfNullOrEmpty
import com.tony.fus.model.FusProcessModel
import com.tony.utils.jsonToObj

/**
 * 流程模型解析器
 * @author tangli
 * @date 2023/11/02 19:15
 * @since 1.0.0
 */
internal data object FusProcessModelParser {
    @JvmStatic
    private val cache: FusCache = DefaultFusCache()

    /**
     * 流程模型 JSON 解析
     * @param [content] 模型内容
     * @param [key] 键
     * @param [redeploy] 重新部署
     * @return [FusProcessModel]
     * @author tangli
     * @date 2023/11/02 19:16
     * @since 1.0.0
     */
    @JvmSynthetic
    @JvmStatic
    internal fun parse(
        content: String,
        key: String,
        redeploy: Boolean,
    ): FusProcessModel {
        if (redeploy) {
            return cache.put(key, parse(content))
        }
        return cache
            .getOrPut(
                key,
                object : TypeReference<FusProcessModel>() {}
            ) {
                parse(content)
            }
    }

    /**
     * 流程模型 JSON 解析
     * @param [content] 模型内容
     * @return [FusProcessModel]
     * @author tangli
     * @date 2024/01/23 16:02
     * @since 1.0.0
     */
    @JvmSynthetic
    @JvmStatic
    internal fun parse(content: String): FusProcessModel =
        content
            .fusThrowIfNullOrEmpty("model content empty")
            .jsonToObj<FusProcessModel>()
            .fusThrowIfNull("fus processModel model parse error")
            .buildParentNode()

    /**
     * 使缓存失效
     * @param [key] 钥匙
     * @author tangli
     * @date 2024/01/24 11:00
     * @since 1.0.0
     */
    @JvmSynthetic
    @JvmStatic
    internal fun invalidate(key: String) {
        cache.remove(key)
    }
}
