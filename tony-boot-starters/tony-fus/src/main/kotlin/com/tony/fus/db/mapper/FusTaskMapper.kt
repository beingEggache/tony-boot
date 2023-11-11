package com.tony.fus.db.mapper

import com.tony.fus.db.po.FusTask
import com.tony.mybatis.dao.BaseDao

/**
 * 任务 Mapper
 * @author Tang Li
 * @date 2023/09/28 17:33
 * @since 1.0.0
 */
internal interface FusTaskMapper : BaseDao<FusTask> {
    /**
     * 根据流程实例ID获取任务列表
     * @param [instanceId] 实例id
     * @return [List<FusTask>]
     * @author Tang Li
     * @date 2023/09/28 17:50
     * @since 1.0.0
     */
    fun selectListByInstanceId(instanceId: String?): List<FusTask> =
        ktQuery()
            .eq(FusTask::instanceId, instanceId)
            .list()
}
