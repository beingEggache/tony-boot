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

public object IdGenerator {

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

    @JvmStatic
    public fun nextIdStr(): String {
        return YitIdHelper.nextId().toString()
    }

    @JvmStatic
    public fun nextId(): Number {
        return YitIdHelper.nextId()
    }
}
