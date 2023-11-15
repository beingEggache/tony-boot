package com.tony.fus.service

import com.tony.fus.ADMIN
import com.tony.fus.db.po.FusInstance
import com.tony.fus.db.po.FusProcess
import com.tony.fus.model.FusOperator

/**
 * 流程实例运行业务类
 * @author tangli
 * @since 2023/10/10 09:49
 */
public interface RuntimeService {
    /**
     * 创建实例
     * @param [process] 流程定义
     * @param [creator] 流程创建者
     * @param [variable] 流程参数
     * @return [FusInstance]
     * @author Tang Li
     * @date 2023/10/10 09:51
     * @since 1.0.0
     */
    public fun createInstance(
        process: FusProcess,
        creator: FusOperator,
        variable: Map<String, Any?>?,
    ): FusInstance

    /**
     * 流程实例正常完成
     * @param [instanceId] 实例id
     * @author Tang Li
     * @date 2023/10/10 10:02
     * @since 1.0.0
     */
    public fun complete(instanceId: String?)

    /**
     * 保存流程实例
     * @param [instance] 流程实例
     * @author Tang Li
     * @date 2023/10/10 10:03
     * @since 1.0.0
     */
    public fun saveInstance(instance: FusInstance): FusInstance

    /**
     * 流程实例强制终止
     * @param [instanceId] 流程实例ID
     * @param [operator] 处理人员
     * @author Tang Li
     * @date 2023/10/10 10:06
     * @since 1.0.0
     */
    public fun terminate(
        instanceId: String,
        operator: FusOperator,
    )

    /**
     * 流程实例强制终止
     * @param [instanceId] 流程实例ID
     * @author Tang Li
     * @date 2023/10/10 10:10
     * @since 1.0.0
     */
    public fun terminate(instanceId: String) {
        terminate(instanceId, ADMIN)
    }

    /**
     * 更新实例
     * @param [instance] 流程实例
     * @author Tang Li
     * @date 2023/10/10 10:15
     * @since 1.0.0
     */
    public fun updateInstance(instance: FusInstance)

    /**
     * 级联删除指定流程实例的所有数据
     * @param [processId] 进程id
     * @author Tang Li
     * @date 2023/10/10 10:15
     * @since 1.0.0
     */
    public fun cascadeRemoveByProcessId(processId: String)
}
