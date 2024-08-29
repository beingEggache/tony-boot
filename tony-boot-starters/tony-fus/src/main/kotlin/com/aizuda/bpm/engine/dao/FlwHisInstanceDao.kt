/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine.dao

import com.aizuda.bpm.engine.entity.FlwHisInstance
import com.tony.mybatis.dao.BaseDao

/**
 * 历史流程实例数据访问层接口
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public interface FlwHisInstanceDao : BaseDao<FlwHisInstance> {
    public fun deleteByProcessId(processId: Long?): Boolean =
        ktUpdate()
            .eq(FlwHisInstance::processId, processId)
            .remove()

    public fun selectListByProcessId(processId: Long?): List<FlwHisInstance> =
        ktQuery()
            .eq(FlwHisInstance::processId, processId)
            .list()
}
