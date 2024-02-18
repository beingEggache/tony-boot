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

package com.tony.test.fus

import com.tony.fus.Fus
import com.tony.utils.genRandomInt
import org.junit.jupiter.api.Test
import org.springframework.test.annotation.Rollback
import org.springframework.transaction.annotation.Transactional

/**
 * 或签测试
 * @author tangli
 * @date 2023/11/13 19:17
 * @since 1.0.0
 */
class FusOrSignTests : FusTests() {

    override val processJson = "json/orSign.json"

    @Rollback
    @Transactional(rollbackFor = [Exception::class])
    @Test
    fun test() {
        Fus.startProcessById(
            processId,
            testOperator1Id,
            businessKey = "FusOrSignTests.test${genRandomInt(6)}",
        ).let { instance ->
            //驳回
            val taskList2 =
                Fus
                    .queryService
                    .listTaskByInstanceId(instance.instanceId)
            taskList2
                .forEach { task ->
                    Fus.taskService.rejectTask(
                        task.taskId,
                        testOperator3Id,
                        mapOf("reason" to "不符合要求")
                    )
                }

            // 调整, 再发起
            val taskList3 =
                Fus
                    .queryService
                    .listTaskByInstanceId(instance.instanceId)
            taskList3
                .forEach { task ->
                    Fus.executeTask(task.taskId, testOperator1Id)
                }

            val taskList4 =
                Fus
                    .queryService
                    .listTaskByInstanceId(instance.instanceId)
            taskList4
                .forEach { task ->
                    Fus.executeTask(task.taskId, testOperator3Id)
                }
        }
    }
}
