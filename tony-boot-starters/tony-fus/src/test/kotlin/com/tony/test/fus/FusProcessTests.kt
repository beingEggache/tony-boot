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

import com.tony.fus.FusContext
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

    fun test(reject: Boolean, day: Int) {
        val processService = FusContext.processService
        processService.getById(processId)

        val args = mutableMapOf<String, Any?>(
            "day" to day,
            "assignee" to testOperator1Id
        )
        FusContext.startInstanceById(
                processId,
                testOperator1Id
        ).let { instance ->
            val instanceId = instance.instanceId
            val taskList2 =
                FusContext
                    .queryService
                    .listTaskByInstanceId(instanceId)
            taskList2
                .forEach { task ->
                    FusContext.executeTask(task.taskId, testOperator1Id, args)
                }

            if (reject) {
                FusContext.runtimeService.reject(instanceId, testOperator1Id)
                return
            }

        }
    }
}
