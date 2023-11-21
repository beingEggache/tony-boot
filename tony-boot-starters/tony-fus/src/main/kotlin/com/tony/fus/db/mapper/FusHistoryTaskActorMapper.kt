package com.tony.fus.db.mapper

import com.tony.fus.db.po.FusHistoryTaskActor
import com.tony.mybatis.dao.BaseDao

/**
 * 历史任务参与者 Mapper
 * @author Tang Li
 * @date 2023/09/28 17:31
 * @since 1.0.0
 */
internal interface FusHistoryTaskActorMapper : BaseDao<FusHistoryTaskActor> {
    /**
     * 通过任务ID删除参与者
     * @param [taskIds] 任务ID
     * @return [Boolean]
     * @author Tang Li
     * @date 2023/09/28 17:42
     * @since 1.0.0
     */
    fun deleteByTaskIds(taskIds: Collection<String>): Boolean =
        delete(ktQuery().`in`(FusHistoryTaskActor::taskId, taskIds)) > 0
}
