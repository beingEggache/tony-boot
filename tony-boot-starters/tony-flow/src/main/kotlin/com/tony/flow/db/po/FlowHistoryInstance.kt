package com.tony.flow.db.po

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableName
import com.tony.flow.db.enums.InstanceState
import java.time.LocalDateTime

/**
 * 历史流程实例
 * @author Tang Li
 * @date 2023/09/29 16:13
 * @since 1.0.0
 */
@TableName("flow_history_instance")
public class FlowHistoryInstance : FlowInstance() {

    /**
     * 流程实例状态: 0-活动, 1-完成, 2-超时, 3-终止
     */
    @TableField(value = "instance_state")
    public var instanceState: InstanceState? = null

    /**
     * 结束时间
     */
    @TableField(value = "end_time")
    public var endTime: LocalDateTime? = null
}
