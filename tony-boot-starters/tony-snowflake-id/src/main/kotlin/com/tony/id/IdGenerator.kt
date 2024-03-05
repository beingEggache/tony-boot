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

package com.tony.id

import com.github.yitter.contract.IdGeneratorOptions
import com.github.yitter.idgen.YitIdHelper
import com.tony.id.config.IdProperties

/**
 * id生成器
 * @author tangli
 * @date 2022/07/12 19:58
 * @since 1.0.0
 */
public data object IdGenerator {
    @JvmSynthetic
    internal fun init(idProperties: IdProperties) {
        val options = IdGeneratorOptions()
        options.WorkerId = idProperties.workerId
        options.WorkerIdBitLength = idProperties.workerIdBitLength
        options.SeqBitLength = idProperties.seqBitLength
        options.MaxSeqNumber = idProperties.maxSeqNumber
        options.MinSeqNumber = idProperties.minSeqNumber
        options.TopOverCostCount = idProperties.topOverCostCount
        YitIdHelper.setIdGenerator(options)
    }

    /**
     * 获取 雪花 id
     * @return [String]
     * @author tangli
     * @date 2023/09/28 19:59
     * @since 1.0.0
     */
    @JvmStatic
    public fun nextIdStr(): String =
        YitIdHelper.nextId().toString()

    /**
     * 获取 雪花 id
     * @return [Number]
     * @author tangli
     * @date 2023/09/28 19:59
     * @since 1.0.0
     */
    @JvmStatic
    public fun nextId(): Number =
        YitIdHelper.nextId()
}
