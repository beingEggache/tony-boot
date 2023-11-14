package com.tony.fus.db.mapper

import com.tony.fus.db.po.FusTaskActor
import com.tony.mybatis.dao.BaseDao

/**
 * 任务参与者 Mapper
 * @author Tang Li
 * @date 2023/09/28 17:32
 * @since 1.0.0
 */
internal interface FusTaskActorMapper : BaseDao<FusTaskActor> {
    /**
     * 通过任务ID获取参与者列表
     * @param [taskId] 任务id
     * @return [List<FusTaskActor>]
     * @author Tang Li
     * @date 2023/09/28 17:48
     * @since 1.0.0
     */
    fun selectListByTaskId(taskId: String?): List<FusTaskActor> =
        ktQuery()
            .eq(FusTaskActor::taskId, taskId)
            .list()

    /**
     * 通过任务ID删除参与者
     * @param [taskId] 任务ID
     * @return [Boolean]
     * @author Tang Li
     * @date 2023/09/28 17:42
     * @since 1.0.0
     */
    fun deleteByTaskId(taskId: String?): Boolean =
        ktUpdate()
            .eq(FusTaskActor::taskId, taskId)
            .remove()

    /**
     * 通过任务ID删除参与者
     * @param [taskIds] 任务ID
     * @return [Boolean]
     * @author Tang Li
     * @date 2023/09/28 17:42
     * @since 1.0.0
     */
    fun deleteByTaskIds(taskIds: Collection<String>): Boolean =
        ktUpdate()
            .`in`(FusTaskActor::taskId, taskIds)
            .remove()
}
