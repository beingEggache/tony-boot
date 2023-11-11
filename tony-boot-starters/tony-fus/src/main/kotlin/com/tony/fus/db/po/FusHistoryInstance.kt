package com.tony.fus.db.po

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableName
import com.tony.fus.db.enums.InstanceState
import java.time.LocalDateTime

/**
 * 历史流程实例
 * @author Tang Li
 * @date 2023/09/29 16:13
 * @since 1.0.0
 */
@TableName("fus_history_instance")
public class FusHistoryInstance : FusInstance() {
    /**
     * 流程实例状态: 1-活动, 2-完成, 3-超时, 4-终止
     */
    @TableField(value = "instance_state")
    public var instanceState: InstanceState? = null

    /**
     * 结束时间
     */
    @TableField(value = "end_time")
    public var endTime: LocalDateTime? = null
}
