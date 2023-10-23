package com.tony.flow.service

import com.tony.flow.ADMIN
import com.tony.flow.FlowOperator
import com.tony.flow.db.po.FlowInstance
import com.tony.flow.db.po.FlowProcess

/**
 * 流程实例运行业务类
 * @author tangli
 * @since 2023/10/10 09:49
 */
public interface RuntimeService {

    /**
     * 创建实例
     * @param [flowProcess] 流程定义
     * @param [flowCreator] 流程创建者
     * @param [variable] 流程参数
     * @return [FlowInstance]
     * @author Tang Li
     * @date 2023/10/10 09:51
     * @since 1.0.0
     */
    public fun createInstance(
        flowProcess: FlowProcess,
        flowCreator: FlowOperator,
        variable: Map<String, Any?>?
    ): FlowInstance

    /**
     * 创建实例
     * @param [flowProcess] 流程定义
     * @param [flowCreator] 流程创建者
     * @return [FlowInstance]
     * @author Tang Li
     * @date 2023/10/10 09:51
     * @since 1.0.0
     */
    public fun createInstance(flowProcess: FlowProcess, flowCreator: FlowOperator): FlowInstance

    /**
     * 流程实例正常完成
     * @param [instanceId] 实例id
     * @author Tang Li
     * @date 2023/10/10 10:02
     * @since 1.0.0
     */
    public fun complete(instanceId: Long)

    /**
     * 保存流程实例
     * @param [flowInstance] 流程实例
     * @author Tang Li
     * @date 2023/10/10 10:03
     * @since 1.0.0
     */
    public fun saveInstance(flowInstance: FlowInstance)

    /**
     * 流程实例强制终止
     * @param [instanceId] 流程实例ID
     * @param [flowOperator] 处理人员
     * @author Tang Li
     * @date 2023/10/10 10:06
     * @since 1.0.0
     */
    public fun terminate(instanceId: Long, flowOperator: FlowOperator)

    /**
     * 流程实例强制终止
     * @param [instanceId] 流程实例ID
     * @author Tang Li
     * @date 2023/10/10 10:10
     * @since 1.0.0
     */
    public fun terminate(instanceId: Long) {
        terminate(instanceId, ADMIN)
    }

    /**
     * 更新实例
     * @param [flowInstance] 流程实例
     * @author Tang Li
     * @date 2023/10/10 10:15
     * @since 1.0.0
     */
    public fun updateInstance(flowInstance: FlowInstance)

    /**
     * 级联删除指定流程实例的所有数据
     * @param [processId] 进程id
     * @author Tang Li
     * @date 2023/10/10 10:15
     * @since 1.0.0
     */
    public fun cascadeRemoveByProcessId(processId: Long)
}
