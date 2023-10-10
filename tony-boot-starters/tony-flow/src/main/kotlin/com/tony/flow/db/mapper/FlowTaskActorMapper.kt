package com.tony.flow.db.mapper

import com.tony.flow.db.po.FlowTaskActor
import com.tony.mybatis.dao.BaseDao

/**
 * 任务参与者 Mapper
 * @author Tang Li
 * @date 2023/09/28 17:32
 * @since 1.0.0
 */
internal interface FlowTaskActorMapper : BaseDao<FlowTaskActor> {

    /**
     * 通过任务ID获取参与者列表
     * @param [taskId] 任务id
     * @return [List<FlowTaskActor>]
     * @author Tang Li
     * @date 2023/09/28 17:48
     * @since 1.0.0
     */
    fun selectListByTaskId(taskId: Long): List<FlowTaskActor> = ktQuery()
        .eq(FlowTaskActor::taskId, taskId)
        .list()

    /**
     * 通过任务ID删除参与者
     * @param [taskId] 任务ID
     * @return [Boolean]
     * @author Tang Li
     * @date 2023/09/28 17:42
     * @since 1.0.0
     */
    fun deleteByTaskId(taskId: Long): Boolean = delete(ktQuery().eq(FlowTaskActor::taskId, taskId)) > 0

    /**
     * 通过任务ID删除参与者
     * @param [taskIds] 任务ID
     * @return [Boolean]
     * @author Tang Li
     * @date 2023/09/28 17:42
     * @since 1.0.0
     */
    fun deleteByTaskIds(taskIds: Collection<Long>): Boolean = delete(ktQuery().`in`(FlowTaskActor::taskId, taskIds)) > 0
}
