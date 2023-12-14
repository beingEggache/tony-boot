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

package com.tony.fus.service

import com.tony.fus.db.po.FusInstance

/**
 * 流程实例运行业务类
 * 运行时服务
 * @author Tang Li
 * @date 2023/10/10 19:49
 * @since 1.0.0
 */
public interface RuntimeService {
    /**
     * 创建实例
     * @param [processId] 流程id
     * @param [userId] 操作人id
     * @param [variable] 流程参数
     * @return [FusInstance]
     * @author Tang Li
     * @date 2023/10/10 19:51
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
     * @date 2023/10/10 19:02
     * @since 1.0.0
     */
    public fun complete(instanceId: String)

    /**
     * 保存流程实例
     * @param [instance] 流程实例
     * @author Tang Li
     * @date 2023/10/10 19:03
     * @since 1.0.0
     */
    public fun saveInstance(instance: FusInstance): FusInstance

    /**
     * 流程实例拒绝审批强制终止（用于后续审核人员认为该审批不再需要继续，拒绝审批强行终止）
     * @param [instanceId] 实例id
     * @param [userId] 操作人id
     * @author Tang Li
     * @date 2023/11/24 19:47
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
     * @date 2023/10/10 19:06
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
     * @date 2023/11/24 19:41
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
     * @date 2023/11/24 19:42
     * @since 1.0.0
     */
    public fun expire(instanceId: String)

    /**
     * 流程实例强制终止
     * @param [instanceId] 实例id
     * @author Tang Li
     * @date 2023/10/10 19:10
     * @since 1.0.0
     */
    public fun terminate(instanceId: String) {
        terminate(instanceId, "ADMIN")
    }

    /**
     * 更新实例
     * @param [instance] 流程实例
     * @author Tang Li
     * @date 2023/10/10 19:15
     * @since 1.0.0
     */
    public fun updateInstance(instance: FusInstance)

    /**
     * 级联删除指定流程实例的所有数据
     * @param [processId] 流程id
     * @author Tang Li
     * @date 2023/10/10 19:15
     * @since 1.0.0
     */
    public fun cascadeRemoveByProcessId(processId: String)
}
