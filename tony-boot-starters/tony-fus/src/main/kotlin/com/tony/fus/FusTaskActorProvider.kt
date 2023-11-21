package com.tony.fus

import com.tony.fus.db.enums.ActorType
import com.tony.fus.db.po.FusTaskActor
import com.tony.fus.model.FusExecution
import com.tony.fus.model.FusNode

/**
 * 任务参与者提供处理接口.
 * @author tangli
 * @date 2023/10/24 16:46
 * @since 1.0.0
 */
public fun interface FusTaskActorProvider {
    /**
     * 获取任务参与者
     * @param [node] 节点模型
     * @param [execution] 执行对象
     * @return [List<FusTaskActor>]
     * @author Tang Li
     * @date 2023/10/24 18:01
     * @since 1.0.0
     */
    public fun listTaskActors(
        node: FusNode?,
        execution: FusExecution,
    ): List<FusTaskActor>
}

internal class DefaultFusTaskActorProvider : FusTaskActorProvider {
    override fun listTaskActors(
        node: FusNode?,
        execution: FusExecution,
    ): List<FusTaskActor> =
        node
            ?.nodeUserList
            ?.map {
                FusTaskActor().apply {
                    actorId = it.id
                    actorName = it.name
                    actorType = ActorType.USER
                    weight = it.weight
                }
            }?.let {
                it.ifEmpty {
                    node
                        .nodeRoleList
                        .map {
                            FusTaskActor().apply {
                                actorId = it.id
                                actorName = it.name
                                actorType = ActorType.ROLE
                                weight = it.weight
                            }
                        }
                }
            }.orEmpty()
}
