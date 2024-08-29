/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine.dao

import com.aizuda.bpm.engine.entity.FlwExtInstance
import com.tony.mybatis.dao.BaseDao

/**
 * 扩展流程实例数据访问层接口
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public interface FlwExtInstanceDao : BaseDao<FlwExtInstance> {
    public fun deleteByProcessId(processId: Long?): Boolean =
        ktUpdate()
            .eq(FlwExtInstance::processId, processId)
            .remove()
}
