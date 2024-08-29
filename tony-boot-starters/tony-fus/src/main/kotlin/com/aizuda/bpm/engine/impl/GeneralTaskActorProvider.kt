/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine.impl

import com.aizuda.bpm.engine.TaskActorProvider
import com.aizuda.bpm.engine.assist.ObjectUtils.isEmpty
import com.aizuda.bpm.engine.assist.ObjectUtils.isNotEmpty
import com.aizuda.bpm.engine.core.Execution
import com.aizuda.bpm.engine.core.enums.NodeSetType
import com.aizuda.bpm.engine.entity.FlwTaskActor
import com.aizuda.bpm.engine.model.NodeModel

/**
 * 普遍的任务参与者提供处理类
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public class GeneralTaskActorProvider : TaskActorProvider {
    override fun getTaskActors(
        nodeModel: NodeModel?,
        execution: Execution?,
    ): List<FlwTaskActor>? {
        val flwTaskActors: MutableList<FlwTaskActor> = ArrayList()
        if (isNotEmpty(nodeModel?.nodeAssigneeList)) {
            val actorType = getActorType(nodeModel)
            if (null != actorType) {
                for (nodeAssignee in nodeModel?.nodeAssigneeList!!) {
                    flwTaskActors.add(FlwTaskActor.of(nodeAssignee, actorType))
                }
            }
        }
        return if (isEmpty(flwTaskActors)) null else flwTaskActors
    }

    public companion object {
        private fun getActorType(nodeModel: NodeModel?): Int? {
            // 0，用户
            if (NodeSetType.SPECIFY_MEMBERS.eq(nodeModel?.setType) ||
                NodeSetType.INITIATOR_THEMSELVES.eq(nodeModel?.setType) ||
                NodeSetType.INITIATOR_SELECTED.eq(nodeModel?.setType)
            ) {
                return 0
            }

            // 1，角色
            if (NodeSetType.ROLE.eq(nodeModel?.setType)) {
                return 1
            }

            // 2，部门
            if (NodeSetType.DEPARTMENT.eq(nodeModel?.setType)) {
                return 2
            }
            return null
        }
    }
}
