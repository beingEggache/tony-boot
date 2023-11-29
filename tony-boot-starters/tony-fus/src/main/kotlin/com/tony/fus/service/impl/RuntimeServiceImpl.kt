package com.tony.fus.service.impl

import com.tony.fus.db.enums.InstanceState
import com.tony.fus.db.enums.TaskState
import com.tony.fus.db.mapper.FusHistoryInstanceMapper
import com.tony.fus.db.mapper.FusInstanceMapper
import com.tony.fus.db.mapper.FusTaskMapper
import com.tony.fus.db.po.FusHistoryInstance
import com.tony.fus.db.po.FusInstance
import com.tony.fus.db.po.FusTask
import com.tony.fus.extension.fusSelectByIdNotNull
import com.tony.fus.listener.InstanceListener
import com.tony.fus.model.enums.EventType
import com.tony.fus.service.RuntimeService
import com.tony.fus.service.TaskService
import com.tony.utils.copyToNotNull
import com.tony.utils.toJsonString
import java.time.LocalDateTime
import org.springframework.transaction.annotation.Transactional

/**
 * RuntimeServiceImpl is
 * @author tangli
 * @date 2023/10/26 15:44
 * @since 1.0.0
 */
internal open class RuntimeServiceImpl(
    private val instanceMapper: FusInstanceMapper,
    private val historyInstanceMapper: FusHistoryInstanceMapper,
    private val taskMapper: FusTaskMapper,
    private val taskService: TaskService,
    private val instanceListener: InstanceListener? = null,
) : RuntimeService {
    @Transactional(rollbackFor = [Throwable::class])
    override fun createInstance(
        processId: String,
        userId: String,
        variable: Map<String, Any?>?,
    ): FusInstance =
        saveInstance(
            FusInstance().apply {
                this.creatorId = userId
                this.processId = processId
                this.variable = variable?.toJsonString() ?: "{}"
            }
        )

    @Transactional(rollbackFor = [Throwable::class])
    override fun complete(instanceId: String) {
        val historyInstance =
            historyInstanceMapper
                .fusSelectByIdNotNull(instanceId)
                .apply {
                    this.instanceId = instanceId
                    this.instanceState = InstanceState.COMPLETED
                    this.endTime = LocalDateTime.now()
                }
        instanceMapper.deleteById(instanceId)
        historyInstanceMapper.updateById(historyInstance)
        instanceListener?.notify(EventType.COMPLETED, historyInstance)
    }

    @Transactional(rollbackFor = [Throwable::class])
    override fun saveInstance(instance: FusInstance): FusInstance {
        instanceMapper.insert(instance)
        val historyInstance =
            instance.copyToNotNull(FusHistoryInstance()).apply {
                this.instanceState = InstanceState.ACTIVE
            }
        historyInstanceMapper.insert(historyInstance)
        instanceListener?.notify(EventType.CREATE, instance)
        return instance
    }

    @Transactional(rollbackFor = [Throwable::class])
    override fun reject(
        instanceId: String,
        userId: String,
    ) {
        forceComplete(
            instanceId,
            userId,
            InstanceState.REJECTED,
            EventType.REJECTED
        )
    }

    @Transactional(rollbackFor = [Throwable::class])
    override fun terminate(
        instanceId: String,
        userId: String,
    ) {
        forceComplete(
            instanceId,
            userId,
            InstanceState.TERMINATED,
            EventType.TERMINATED
        )
    }

    @Transactional(rollbackFor = [Throwable::class])
    override fun revoke(
        instanceId: String,
        userId: String,
    ) {
        forceComplete(
            instanceId,
            userId,
            InstanceState.REVOKED,
            EventType.REVOKED
        )
    }

    @Transactional(rollbackFor = [Throwable::class])
    override fun expire(instanceId: String) {
        forceComplete(
            instanceId,
            "ADMIN",
            InstanceState.EXPIRED,
            EventType.EXPIRED
        )
    }

    override fun updateInstance(instance: FusInstance) {
        instanceMapper.updateById(instance)
    }

    override fun cascadeRemoveByProcessId(processId: String) {
        TODO("Not yet implemented")
    }

    private fun forceComplete(
        instanceId: String,
        userId: String,
        instanceState: InstanceState,
        eventType: EventType,
    ) {
        instanceMapper
            .fusSelectByIdNotNull(
                instanceId,
                "instance[$instanceId] not exists"
            ).also { instance ->
                taskMapper
                    .ktQuery()
                    .eq(FusTask::instanceId, instanceId)
                    .list()
                    .forEach { task ->
                        taskService.execute(
                            task.taskId,
                            userId,
                            TaskState.of(instanceState),
                            eventType,
                            null
                        )
                    }

                val historyInstance =
                    instance
                        .copyToNotNull(FusHistoryInstance())
                        .apply {
                            this.instanceState = instanceState
                            this.endTime = LocalDateTime.now()
                        }
                historyInstanceMapper.updateById(historyInstance)
                instanceMapper.deleteById(instanceId)
                instanceListener?.notify(eventType, historyInstance)
            }
    }
}
