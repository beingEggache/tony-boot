package com.tony.flow.db.po

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableName
import com.tony.flow.enums.TaskState

/**
 * 历史任务
 * @author Tang Li
 * @date 2023/09/29 16:03
 * @since 1.0.0
 */

@TableName(value = "flow_history_task")
public class FlowHistoryTask : FlowTask() {

    /**
     * 任务状态: 0-活动, 1-完成, 2-拒绝, 3-超时, 4-终止
     */
    @TableField(value = "task_state")
    public var taskState: TaskState? = null
}
