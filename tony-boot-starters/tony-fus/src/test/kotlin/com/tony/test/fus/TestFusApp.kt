package com.tony.test.fus

import com.tony.annotation.EnableTonyBoot
import com.tony.fus.FusTaskActorProvider
import com.tony.fus.db.enums.ActorType
import com.tony.fus.db.po.FusTaskActor
import com.tony.fus.model.FusExecution
import com.tony.fus.model.FusNode
import com.tony.fus.model.enums.ApproverType
import com.tony.fus.model.enums.NodeType
import com.tony.utils.getLogger
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.context.annotation.Bean

/**
 * TestFusApp is
 * @author tangli
 * @date 2023/11/13 15:15
 * @since 1.0.0
 */
@EnableTonyBoot
@SpringBootApplication
class TestFusApp

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
                getLogger().info("supervisor-task-actor-provider")
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
        }
    }
}
