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
import com.tony.test.fus.TestFusSupervisorApp.Companion.user1Id
import com.tony.test.fus.TestFusSupervisorApp.Companion.user2Id
import com.tony.test.fus.TestFusSupervisorApp.Companion.user3Id
import com.tony.test.fus.TestFusSupervisorApp.Companion.user4Id
import com.tony.utils.genRandomInt
import com.tony.utils.string
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.test.annotation.Rollback
import org.springframework.transaction.annotation.Transactional

/**
 * 连续主管审批顺签测试
 * @author tangli
 * @date 2023/11/13 19:17
 * @since 1.0.0
 */
@SpringBootTest(
    properties = [
        "supervisor-task-actor-provider=true",
    ],
    classes = [TestFusSupervisorApp::class], webEnvironment = SpringBootTest.WebEnvironment.NONE)
class FusSupervisorProcessTests {

    lateinit var processId: String

    @BeforeEach
    fun before() {
        val processModelJson = getProcessModelJson()
        processId = Fus.processService.deploy(processModelJson, false)
    }

    private fun getProcessModelJson() = PathMatchingResourcePatternResolver()
        .getResource(processJson)
        .inputStream
        .readAllBytes()
        .string()

    private val processJson = "json/supervisor.json"

    @Rollback
    @Transactional(rollbackFor = [Exception::class])
    @Test
    fun test() {
        val args =
            mapOf(
                "day" to 4
            )

        Fus.startProcessById(
            processId,
            user4Id,
            args,
            "FusSupervisorProcessTests.test${genRandomInt(6)}",
        ).let { instance ->
            val instanceId = instance.instanceId

            //四级部门审核
            val taskList2 =
                Fus
                    .queryService
                    .listTaskByInstanceId(instanceId)
            taskList2
                .forEach { task ->
                    Fus.executeTask(task.taskId, user4Id)
                }

            //三级部门审核
            val taskList3 =
                Fus
                    .queryService
                    .listTaskByInstanceId(instanceId)
            taskList3
                .forEach { task ->
                    Fus.executeTask(task.taskId, user3Id)
                }

            //二级部门审核
            val taskList4 =
                Fus
                    .queryService
                    .listTaskByInstanceId(instanceId)
            taskList4
                .forEach { task ->
                    Fus.executeTask(task.taskId, user2Id)
                }

            //一级部门审核
            val taskList5 =
                Fus
                    .queryService
                    .listTaskByInstanceId(instanceId)
            taskList5
                .forEach { task ->
                    Fus.executeTask(task.taskId, user1Id)
                }
        }
    }
}
