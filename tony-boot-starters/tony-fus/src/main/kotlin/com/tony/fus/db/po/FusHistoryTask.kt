package com.tony.fus.db.po

import com.baomidou.mybatisplus.annotation.TableName
import com.tony.fus.db.enums.TaskState
import com.tony.fus.model.FusOperator
import com.tony.utils.copyToNotNull

/**
 * 历史任务
 * @author Tang Li
 * @date 2023/09/29 16:03
 * @since 1.0.0
 */

@TableName
public class FusHistoryTask : FusTask() {
    /**
     * 任务状态: 1.活动, 2.完成, 3.拒绝, 4.超时, 5.终止
     */
    public var taskState: TaskState? = null

    public fun undo(operator: FusOperator): FusTask =
        copyToNotNull(FusTask()).apply {
            creatorId = operator.operatorId
            creatorName = operator.operatorName
        }
}
