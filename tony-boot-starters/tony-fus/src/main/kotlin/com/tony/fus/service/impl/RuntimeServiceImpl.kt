package com.tony.fus.service.impl

import com.tony.fus.db.enums.InstanceState
import com.tony.fus.db.mapper.FusHistoryInstanceMapper
import com.tony.fus.db.mapper.FusInstanceMapper
import com.tony.fus.db.mapper.FusTaskMapper
import com.tony.fus.db.po.FusHistoryInstance
import com.tony.fus.db.po.FusInstance
import com.tony.fus.db.po.FusTask
import com.tony.fus.extension.fusSelectByIdNotNull
import com.tony.fus.listener.InstanceListener
import com.tony.fus.model.FusOperator
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
internal open class RuntimeServiceImpl
    @JvmOverloads
    constructor(
        private val instanceMapper: FusInstanceMapper,
        private val historyInstanceMapper: FusHistoryInstanceMapper,
        private val taskMapper: FusTaskMapper,
        private val taskService: TaskService,
        private val instanceListener: InstanceListener? = null,
    ) : RuntimeService {
        @Transactional(rollbackFor = [Throwable::class])
        override fun createInstance(
            processId: String,
            creator: FusOperator,
            variable: Map<String, Any?>?,
        ): FusInstance =
            saveInstance(
                FusInstance().apply {
                    creatorId = creator.operatorId
                    creatorName = creator.operatorName
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
                        this.instanceState = InstanceState.COMPLETE
                        this.endTime = LocalDateTime.now()
                    }
            instanceMapper.deleteById(instanceId)
            historyInstanceMapper.updateById(historyInstance)
            instanceListener?.notify(EventType.COMPLETE, historyInstance)
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
        override fun terminate(
            instanceId: String,
            operator: FusOperator,
        ) {
            val instance = instanceMapper.fusSelectByIdNotNull(instanceId)
            taskMapper
                .ktQuery()
                .eq(FusTask::instanceId, instanceId)
                .list()
                .forEach {
                    taskService.complete(it.taskId, operator)
                }

            val historyInstance =
                instance
                    .copyToNotNull(FusHistoryInstance())
                    .apply {
                        instanceState = InstanceState.TERMINATED
                        endTime = LocalDateTime.now()
                    }
            historyInstanceMapper.updateById(historyInstance)
            instanceMapper.deleteById(instanceId)
            instanceListener?.notify(EventType.TERMINATE, instance)
        }

        override fun updateInstance(instance: FusInstance) {
            instanceMapper.updateById(instance)
        }

        override fun cascadeRemoveByProcessId(processId: String) {
            TODO("Not yet implemented")
        }
    }
