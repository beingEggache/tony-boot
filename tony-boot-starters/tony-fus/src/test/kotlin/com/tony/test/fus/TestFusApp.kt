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

import com.tony.ApiSession
import com.tony.annotation.EnableTonyBoot
import com.tony.fus.FusTaskActorProvider
import com.tony.fus.db.enums.ActorType
import com.tony.fus.db.po.FusTaskActor
import com.tony.fus.model.FusExecution
import com.tony.fus.model.FusNode
import com.tony.fus.model.enums.ApproverType
import com.tony.fus.model.enums.NodeType
import com.tony.mybatis.DefaultMetaObjectHandler
import com.tony.mybatis.MetaColumn
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.context.annotation.Bean
import java.util.function.Function

/**
 * TestFusApp is
 * @author tangli
 * @date 2023/11/13 19:15
 * @since 1.0.0
 */
@EnableTonyBoot
@SpringBootApplication
class TestFusApp {

    @Bean
    fun metaObjectHandler() =
        DefaultMetaObjectHandler(
            NoopApiSession(),
            mapOf(
                MetaColumn.USER_NAME to Function<Any?, Any?> { "get name by $it" }
            )
        )
}

class NoopApiSession : ApiSession {
    override val userId: String
        get() = "1"
    override val userName: String
        get() = "aloha"
    override val tenantId: String
        get() = "1"
}

@EnableTonyBoot
@SpringBootApplication
class TestFusSupervisorApp {
    companion object {
        val user1Id = "1"
        val user1Name = "一级部门"
        val user2Id = "2"
        val user2Name = "二级部门"
        val user3Id = "3"
        val user3Name = "三级部门"
        val user4Id = "4"
        val user4Name = "四级部门"
    }

    @ConditionalOnExpression("\${supervisor-task-actor-provider:false}")
    @Bean
    fun taskActorProvider(): FusTaskActorProvider {

        return object : FusTaskActorProvider {
            override fun listTaskActors(node: FusNode?, execution: FusExecution): List<FusTaskActor> {
                if (node?.nodeType == NodeType.INITIATOR) {
                    return listOf(FusTaskActor().apply { actorId = execution.userId })
                }

                if (node?.nodeType == NodeType.APPROVER && node.approverType == ApproverType.MULTISTAGE_MANAGER) {
                    return listOf(
                        FusTaskActor().apply { actorId = user4Id; actorName = user4Name },
                        FusTaskActor().apply { actorId = user3Id; actorName = user3Name },
                        FusTaskActor().apply { actorId = user2Id; actorName = user2Name },
                        FusTaskActor().apply { actorId = user1Id; actorName = user1Name },
                    )
                }
                val nodeUserList = node?.nodeUserList
                if (!nodeUserList.isNullOrEmpty()) {
                    return nodeUserList
                        .map { FusTaskActor().apply { actorId = it.id; actorName = it.name } }
                }
                val nodeRoleList = node?.nodeRoleList
                if (!nodeRoleList.isNullOrEmpty()) {
                    return nodeRoleList
                        .map { FusTaskActor().apply { actorId = it.id; actorName = it.name; actorType = ActorType.ROLE } }
                }
                return emptyList()
            }

            override fun hasPermission(node: FusNode, userId: String): Boolean =
                true
        }
    }
}
