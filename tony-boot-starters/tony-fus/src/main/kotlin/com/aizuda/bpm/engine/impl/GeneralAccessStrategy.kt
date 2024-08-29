/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine.impl

import com.aizuda.bpm.engine.TaskAccessStrategy
import com.aizuda.bpm.engine.assist.ObjectUtils.isEmpty
import com.aizuda.bpm.engine.entity.FlwTaskActor
import com.tony.utils.getLogger

/**
 * 基于用户或组（角色、部门等）的访问策略类
 * 该策略类适合组作为参与者的情况
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public open class GeneralAccessStrategy : TaskAccessStrategy {
    private val logger = getLogger()

    /**
     * 如果创建人ID所属的组只要有一项存在于参与者集合中，则表示可访问
     */
    override fun isAllowed(
        userId: String?,
        taskActors: List<FlwTaskActor>,
    ): FlwTaskActor? {
        logger.info("isAllowed $userId $taskActors")
        if (null == userId || taskActors.isEmpty()) {
            return null
        }
        // 参与者 ID 默认非组，作为用户ID判断是否允许执行
        return taskActors.firstOrNull { it.actorId == userId }
    }
}
