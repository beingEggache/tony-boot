package com.tony.fus.db.po

import com.baomidou.mybatisplus.annotation.TableName

/**
 * 历史任务参与者
 * @author Tang Li
 * @date 2023/09/29 16:13
 * @since 1.0.0
 */
@TableName(value = "fus_history_task_actor")
public class FusHistoryTaskActor : FusTaskActor()
