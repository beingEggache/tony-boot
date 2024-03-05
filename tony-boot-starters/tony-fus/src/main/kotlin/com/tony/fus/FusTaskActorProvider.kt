/*
 * MIT License
 *
 * Copyright (c) 2023-present, tangli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.tony.fus

import com.tony.fus.db.enums.ActorType
import com.tony.fus.db.po.FusTaskActor
import com.tony.fus.extension.fusThrowIf
import com.tony.fus.model.FusExecution
import com.tony.fus.model.FusNode

/**
 * 任务参与者提供处理接口.
 * @author tangli
 * @date 2023/10/24 19:46
 * @since 1.0.0
 */
public fun interface FusTaskActorProvider {
    /**
     * 获取任务参与者
     * @param [node] 节点模型
     * @param [execution] 执行对象
     * @return [List<FusTaskActor>]
     * @author tangli
     * @date 2023/10/24 19:01
     * @since 1.0.0
     */
    public fun listTaskActors(
        node: FusNode?,
        execution: FusExecution,
    ): List<FusTaskActor>

    /**
     * 是否拥有权限
     * @param [node] 节点
     * @param [actorId] 参与者id
     * @return [Boolean]
     * @author tangli
     * @date 2024/02/18 17:43
     * @since 1.0.0
     */
    public fun hasPermission(
        node: FusNode,
        actorId: String,
    ): Boolean {
        if (node.nodeUserList.isNotEmpty()) {
            return node.nodeUserList.any { nodeAssignee ->
                nodeAssignee.id == actorId
            }
        }
        fusThrowIf(
            node.nodeRoleList.isNotEmpty(),
            "Please implement the interface FusTaskActorProvider method hasPermission"
        )
        return true
    }
}

internal class DefaultFusTaskActorProvider : FusTaskActorProvider {
    override fun listTaskActors(
        node: FusNode?,
        execution: FusExecution,
    ): List<FusTaskActor> =
        node
            ?.nodeUserList
            ?.map { nodeAssignee ->
                FusTaskActor().apply {
                    actorId = nodeAssignee.id
                    actorName = nodeAssignee.name
                    actorType = ActorType.USER
                    weight = nodeAssignee.weight
                }
            }?.ifEmpty {
                node
                    .nodeRoleList
                    .map { nodeAssignee ->
                        FusTaskActor().apply {
                            actorId = nodeAssignee.id
                            actorName = nodeAssignee.name
                            actorType = ActorType.ROLE
                            weight = nodeAssignee.weight
                        }
                    }
            }.orEmpty()
}
