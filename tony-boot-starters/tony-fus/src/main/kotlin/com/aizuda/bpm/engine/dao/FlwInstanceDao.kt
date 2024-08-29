/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine.dao

import com.aizuda.bpm.engine.entity.FlwInstance
import com.tony.mybatis.dao.BaseDao

/**
 * 流程实例数据访问层接口
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public interface FlwInstanceDao : BaseDao<FlwInstance> {
    public fun deleteByProcessId(processId: Long?): Boolean =
        ktUpdate()
            .eq(FlwInstance::processId, processId)
            .remove()

    public fun selectListByParentInstanceId(parentInstanceId: Long?): List<FlwInstance> =
        ktQuery()
            .eq(FlwInstance::parentInstanceId, parentInstanceId)
            .list()
}
