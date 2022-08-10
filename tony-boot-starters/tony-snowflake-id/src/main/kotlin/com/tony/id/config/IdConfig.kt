/**
 * IdConfig
 *
 * @author tangli
 * @since 2022/7/12 14:56
 */
package com.tony.id.config

import com.tony.id.IdGenerator
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.context.properties.bind.DefaultValue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(IdProperties::class)
class IdConfig(
    private val idProperties: IdProperties
) {

    @Bean
    fun idGenerator(): IdGenerator? {
        return IdGenerator(idProperties)
    }
}

@ConstructorBinding
@ConfigurationProperties(prefix = "snowflake")
data class IdProperties(
    /**
     * 机器码
     * 必须由外部设定，最大值 2^WorkerIdBitLength-1
     */
    @DefaultValue("0")
    val workerId: Short = 0,

    /**
     * 机器码位长
     * 默认值6，取值范围 [1, 15]（要求：序列数位长+机器码位长不超过22）
     */
    @DefaultValue("3")
    val workerIdBitLength: Byte = 3,
//    val workerIdBitLength: Byte = 6;

    /**
     * 序列数位长
     * 默认值6，取值范围 [3, 21]（要求：序列数位长+机器码位长不超过22）
     */
    @DefaultValue("3")
    val seqBitLength: Byte = 3,
//    val seqBitLength: Byte = 6;

    //    private byte seqBitLength = 6;
    /**
     * 最大序列数（含）
     * 设置范围 [MinSeqNumber, 2^SeqBitLength-1]，默认值0，表示最大序列数取最大值（2^SeqBitLength-1]）
     */
    @DefaultValue("0")
    val maxSeqNumber: Short = 0,

    /**
     * 最小序列数（含）
     * 默认值5，取值范围 [5, MaxSeqNumber]，每毫秒的前5个序列数对应编号是0-4是保留位，其中1-4是时间回拨相应预留位，0是手工新值预留位
     */
    @DefaultValue("5")
    val minSeqNumber: Short = 5,

    /**
     * 最大漂移次数（含）
     * 默认2000，推荐范围500-10000（与计算能力有关）
     */
    @DefaultValue("2000")
    val topOverCostCount: Short = 2000
)
