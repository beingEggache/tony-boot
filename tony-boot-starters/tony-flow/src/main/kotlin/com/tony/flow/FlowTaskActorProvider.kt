package com.tony.flow

import com.tony.flow.db.po.FlowTaskActor
import com.tony.flow.model.FlowExecution
import com.tony.flow.model.FlowNode

/**
 * 任务参与者提供处理接口.
 * @author tangli
 * @date 2023/10/24 16:46
 * @since 1.0.0
 */
public fun interface FlowTaskActorProvider {
    /**
     * 获取任务参与者
     * @param [flowNode] 节点模型
     * @param [flowExecution] 执行对象
     * @return [List<FlowTaskActor>]
     * @author Tang Li
     * @date 2023/10/24 18:01
     * @since 1.0.0
     */
    public fun listTaskActors(
        flowNode: FlowNode?,
        flowExecution: FlowExecution,
    ): List<FlowTaskActor>
}
