/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine

import com.aizuda.bpm.engine.assist.Assert.isEmpty
import com.aizuda.bpm.engine.assist.ObjectUtils.isNotEmpty
import com.aizuda.bpm.engine.core.Execution
import com.aizuda.bpm.engine.core.FlowCreator
import com.aizuda.bpm.engine.core.enums.NodeSetType
import com.aizuda.bpm.engine.core.enums.TaskType
import com.aizuda.bpm.engine.entity.FlwTaskActor
import com.aizuda.bpm.engine.model.NodeAssignee
import com.aizuda.bpm.engine.model.NodeModel

/**
 * 任务参与者提供处理接口
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public interface TaskActorProvider {
    /**
     * 流程创建者是否允许操作执行当前节点
     *
     * @param nodeModel   当前执行节点
     * @param flowCreator 流程创建者
     * @return true 允许 false 不被允许
     */
    public fun isAllowed(
        nodeModel: NodeModel,
        flowCreator: FlowCreator?,
    ): Boolean {
        val nodeAssigneeList = nodeModel.nodeAssigneeList
        if (NodeSetType.SPECIFY_MEMBERS.eq(nodeModel.setType) && isNotEmpty(nodeAssigneeList)) {
            return nodeAssigneeList!!.stream().anyMatch { t: NodeAssignee? -> t!!.id == flowCreator?.createId }
        }

        if (TaskType.MAJOR.eq(nodeModel.type) && !NodeSetType.INITIATOR_SELECTED.eq(nodeModel.setType)) {
            // 发起人且非自选情况
            return true
        }

        // 角色判断必须要求子类实现
        isEmpty(nodeAssigneeList, "Please implement the interface TaskActorProvider method isAllow")
        return true
    }

    /**
     * 根据Task模型的assignee、assignmentHandler属性以及运行时数据，确定参与者
     *
     * @param nodeModel 节点模型
     * @param execution 执行对象
     * @return 参与者数组
     */
    public fun getTaskActors(
        nodeModel: NodeModel?,
        execution: Execution?,
    ): List<FlwTaskActor>?
}
