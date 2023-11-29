package com.tony.fus.service

import com.tony.fus.db.po.FusInstance

/**
 * 流程实例运行业务类
 * @author tangli
 * @since 2023/10/10 09:49
 */
public interface RuntimeService {
    /**
     * 创建实例
     * @param [processId] 流程id
     * @param [userId] 操作人id
     * @param [variable] 流程参数
     * @return [FusInstance]
     * @author Tang Li
     * @date 2023/10/10 09:51
     * @since 1.0.0
     */
    public fun createInstance(
        processId: String,
        userId: String,
        variable: Map<String, Any?>?,
    ): FusInstance

    /**
     * 流程实例正常完成 （审批通过）
     * @param [instanceId] 实例id
     * @author Tang Li
     * @date 2023/10/10 10:02
     * @since 1.0.0
     */
    public fun complete(instanceId: String)

    /**
     * 保存流程实例
     * @param [instance] 流程实例
     * @author Tang Li
     * @date 2023/10/10 10:03
     * @since 1.0.0
     */
    public fun saveInstance(instance: FusInstance): FusInstance

    /**
     * 流程实例拒绝审批强制终止（用于后续审核人员认为该审批不再需要继续，拒绝审批强行终止）
     * @param [instanceId] 实例id
     * @param [userId] 操作人id
     * @author Tang Li
     * @date 2023/11/24 14:47
     * @since 1.0.0
     */
    public fun reject(
        instanceId: String,
        userId: String,
    )

    /**
     * 流程实例强制终止
     * @param [instanceId] 实例id
     * @param [userId] 操作人id
     * @author Tang Li
     * @date 2023/10/10 10:06
     * @since 1.0.0
     */
    public fun terminate(
        instanceId: String,
        userId: String,
    )

    /**
     * 流程实例撤销（用于错误发起审批申请，发起人主动撤销）
     * @param [instanceId] 实例id
     * @param [userId] 操作人id
     * @author Tang Li
     * @date 2023/11/24 11:41
     * @since 1.0.0
     */
    public fun revoke(
        instanceId: String,
        userId: String,
    )

    /**
     * 流程实例超时（设定审批时间超时，自动结束）
     * @param [instanceId] 实例id
     * @author Tang Li
     * @date 2023/11/24 11:42
     * @since 1.0.0
     */
    public fun expire(instanceId: String)

    /**
     * 流程实例强制终止
     * @param [instanceId] 实例id
     * @author Tang Li
     * @date 2023/10/10 10:10
     * @since 1.0.0
     */
    public fun terminate(instanceId: String) {
        terminate(instanceId, "ADMIN")
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
     * @param [processId] 流程id
     * @author Tang Li
     * @date 2023/10/10 10:15
     * @since 1.0.0
     */
    public fun cascadeRemoveByProcessId(processId: String)
}
