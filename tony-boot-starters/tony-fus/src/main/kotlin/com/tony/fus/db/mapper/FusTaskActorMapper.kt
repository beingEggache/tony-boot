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

package com.tony.fus.db.mapper

import com.tony.fus.db.po.FusTaskActor
import com.tony.mybatis.dao.BaseDao

/**
 * 任务参与者 Mapper
 * @author tangli
 * @date 2023/09/28 19:32
 * @since 1.0.0
 */
internal interface FusTaskActorMapper : BaseDao<FusTaskActor> {
    /**
     * 通过任务ID获取参与者列表
     * @param [taskId] 任务id
     * @return [List<FusTaskActor>]
     * @author tangli
     * @date 2023/09/28 19:48
     * @since 1.0.0
     */
    fun selectListByTaskId(taskId: String?): List<FusTaskActor> =
        ktQuery()
            .eq(FusTaskActor::taskId, taskId)
            .list()

    /**
     * 通过流程实例ID获取参与者列表
     * @param [instanceId] 流程实例ID
     * @return [List<FusTaskActor>]
     * @author tangli
     * @date 2023/09/28 19:48
     * @since 1.0.0
     */
    fun selectListByInstanceId(instanceId: String?): List<FusTaskActor> =
        ktQuery()
            .eq(FusTaskActor::instanceId, instanceId)
            .list()

    /**
     * 通过任务ID删除参与者
     * @param [taskIds] 任务ID
     * @return [Boolean]
     * @author tangli
     * @date 2023/09/28 19:42
     * @since 1.0.0
     */
    fun deleteByTaskIds(taskIds: Collection<String>): Boolean =
        ktUpdate()
            .`in`(FusTaskActor::taskId, taskIds)
            .remove()
}
