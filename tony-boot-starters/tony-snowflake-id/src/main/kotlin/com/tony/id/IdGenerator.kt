/**
 * IdGenerator
 *
 * @author tangli
 * @since 2022/7/12 14:58
 */
package com.tony.id

import com.github.yitter.contract.IdGeneratorOptions
import com.github.yitter.idgen.YitIdHelper
import com.tony.id.config.IdProperties

public class IdGenerator internal constructor(idProperties: IdProperties) {
    init {
        val options = IdGeneratorOptions()
        options.WorkerId = idProperties.workerId
        options.WorkerIdBitLength = idProperties.workerIdBitLength
        options.SeqBitLength = idProperties.seqBitLength
        options.MaxSeqNumber = idProperties.maxSeqNumber
        options.MinSeqNumber = idProperties.minSeqNumber
        options.TopOverCostCount = idProperties.topOverCostCount
        YitIdHelper.setIdGenerator(options)
    }

    public companion object {
        @Suppress("unused")
        @JvmStatic
        public fun nextIdStr(): String {
            return YitIdHelper.nextId().toString()
        }

        @Suppress("unused")
        @JvmStatic
        public fun nextId(): Number {
            return YitIdHelper.nextId()
        }
    }
}
