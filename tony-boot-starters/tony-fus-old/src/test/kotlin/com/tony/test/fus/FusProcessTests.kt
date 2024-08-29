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
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.annotation.Rollback
import org.springframework.transaction.annotation.Transactional

/**
 * FusTests is
 * @author tangli
 * @date 2023/11/13 19:17
 * @since 1.0.0
 */
class FusProcessTests : FusTests() {

    override val processJson = "json/process.json"

    @BeforeEach
    override fun before() {
        processId = Fus.processService.deploy(getProcessModelJson(), false)
        Fus.processService.deploy(getProcessModelJson("json/workHandover.json"), false)
    }

    @Rollback
    @Transactional(rollbackFor = [Exception::class])
    @Test
    fun testComplete() {
        test(false, 8)
    }

    @Rollback
    @Transactional(rollbackFor = [Exception::class])
    @Test
    fun testReject() {
        test(true, 8)
    }

    @Rollback
    @Transactional(rollbackFor = [Exception::class])
    @Test
    fun testDay() {
        test(false, 3)
    }

    @Rollback
    @Transactional(rollbackFor = [Exception::class])
    @Test
    fun testCascadeRemove() {
        Fus.processService.cascadeRemove(processId)
    }

    fun test(reject: Boolean, day: Int) {
        val args = mutableMapOf<String, Any?>(
            "day" to day,
            "assignee" to testOperator1Id
        )
        Fus.startProcessById(
            processId,
            testOperator1Id,
            businessKey = "FusProcessTests.test${genRandomInt(6)}",
        ).let { instance ->
            val instanceId = instance.instanceId
            val taskList2 =
                Fus
                    .queryService
                    .listTaskByInstanceId(instanceId)
            taskList2
                .forEach { task ->
                    Fus.executeTask(task.taskId, testOperator1Id, args)
                }

            if (reject) {
                Fus.runtimeService.reject(instanceId, testOperator1Id)
            } else {
                Fus
                    .queryService
                    .listTaskByInstanceId(instanceId)
                    .forEach { task ->
                        Fus.executeTask(task.taskId, testOperator1Id, args)
                    }
            }

            Fus
                .queryService
                .listHistoryTask(instanceId)
                .forEach {  historyTask ->
                    val outInstanceId = historyTask.outInstanceId
                    if(outInstanceId.isNotBlank()){
                        Fus
                            .queryService
                            .listTaskByInstanceId(outInstanceId)
                            .forEach { task ->
                                Fus.executeTask(task.taskId, testOperator3Id)
                            }
                    }
                }

        }
    }
}
