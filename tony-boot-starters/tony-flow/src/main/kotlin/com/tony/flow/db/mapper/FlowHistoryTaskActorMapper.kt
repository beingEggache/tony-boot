package com.tony.flow.db.mapper

import com.tony.flow.db.po.FlowHistoryTaskActor
import com.tony.flow.db.po.FlowTaskActor
import com.tony.mybatis.dao.BaseDao

/**
 * 历史任务参与者 Mapper
 * @author Tang Li
 * @date 2023/09/28 17:31
 * @since 1.0.0
 */
internal interface FlowHistoryTaskActorMapper : BaseDao<FlowHistoryTaskActor> {

    /**
     * 通过任务ID获取参与者列表
     * @param [taskId] 任务id
     * @return [List<FlowTaskActor>]
     * @author Tang Li
     * @date 2023/09/28 17:40
     * @since 1.0.0
     */
    fun selectListByTaskId(taskId: Long): List<FlowTaskActor> =
        ktQuery()
            .eq(FlowHistoryTaskActor::taskActorId, taskId)
            .list()

    /**
     * 通过任务ID删除参与者
     * @param [taskIds] 任务ID
     * @return [Boolean]
     * @author Tang Li
     * @date 2023/09/28 17:42
     * @since 1.0.0
     */
    fun deleteByTaskIds(taskIds: Collection<Long>): Boolean =
        delete(ktQuery().`in`(FlowHistoryTaskActor::taskId, taskIds)) > 0
}




