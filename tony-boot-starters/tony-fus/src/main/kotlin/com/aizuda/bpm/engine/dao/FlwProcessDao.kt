/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine.dao

import com.aizuda.bpm.engine.entity.FlwProcess
import com.tony.mybatis.dao.BaseDao

/**
 * 流程定义数据访问层接口
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public interface FlwProcessDao : BaseDao<FlwProcess> {
    public fun updateByProcessKey(
        process: FlwProcess,
        tenantId: String?,
        processKey: String?,
    ): Boolean =
        ktUpdate()
            .eq(null != tenantId, FlwProcess::tenantId, tenantId)
            .eq(FlwProcess::processKey, processKey)
            .update(process)

    public fun selectListByProcessKeyAndVersion(
        tenantId: String?,
        processKey: String?,
        version: Int?,
    ): List<FlwProcess> =
        ktQuery()
            .eq(null != tenantId, FlwProcess::tenantId, tenantId)
            .eq(FlwProcess::processKey, processKey)
            .apply {
                if (version != null) {
                    eq(FlwProcess::processVersion, version)
                } else {
                    orderByDesc(FlwProcess::processVersion)
                }
            }.list()
}
