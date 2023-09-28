package com.tony.flow.db.po

import com.baomidou.mybatisplus.annotation.TableName

/**
 * 历史任务参与者表
 * @TableName flow_history_task_actor
 */
@TableName(value = "flow_history_task_actor")
public class FlowHistoryTaskActor : FlowTaskActor()
