package com.tony.flow.db.mapper

import com.tony.flow.db.po.FlowTask
import com.tony.mybatis.dao.BaseDao

/**
 * 任务 Mapper
 * @author Tang Li
 * @date 2023/09/28 17:33
 * @since 1.0.0
 */
internal interface FlowTaskMapper : BaseDao<FlowTask> {
    /**
     * 根据流程实例ID获取任务列表
     * @param [instanceId] 实例id
     * @return [List<FlowTask>]
     * @author Tang Li
     * @date 2023/09/28 17:50
     * @since 1.0.0
     */
    fun selectListByInstanceId(instanceId: String): List<FlowTask> =
        ktQuery()
            .eq(FlowTask::instanceId, instanceId)
            .list()
}
