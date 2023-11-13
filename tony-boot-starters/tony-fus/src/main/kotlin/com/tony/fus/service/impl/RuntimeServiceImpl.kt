package com.tony.fus.service.impl

import com.tony.fus.db.enums.InstanceState
import com.tony.fus.db.mapper.FusHistoryInstanceMapper
import com.tony.fus.db.mapper.FusInstanceMapper
import com.tony.fus.db.mapper.FusTaskMapper
import com.tony.fus.db.po.FusHistoryInstance
import com.tony.fus.db.po.FusInstance
import com.tony.fus.db.po.FusProcess
import com.tony.fus.db.po.FusTask
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
 * RuntimServiceImpl is
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
        override fun createInstance(
            process: FusProcess,
            creator: FusOperator,
            variable: Map<String, Any?>?,
        ): FusInstance =
            FusInstance().apply {
                createTime = LocalDateTime.now()
                updateTime = createTime
                creatorId = creator.operatorId
                creatorName = creator.operatorName
                updatorId = creator.operatorId
                updatorName = creator.operatorName
                processId = process.processId
                this.variable = variable?.toJsonString() ?: "{}"
            }

        @Transactional(rollbackFor = [Throwable::class])
        override fun complete(instanceId: String?) {
            val historyInstance =
                FusHistoryInstance().apply {
                    this.instanceId = instanceId
                    this.instanceState = InstanceState.COMPLETE
                    this.endTime = LocalDateTime.now()
                }
            instanceMapper.deleteById(instanceId)
            historyInstanceMapper.updateById(historyInstance)
            instanceListener?.notify(EventType.COMPLETE, historyInstance)
        }

        @Transactional(rollbackFor = [Throwable::class])
        override fun saveInstance(instance: FusInstance) {
            instanceMapper.insert(instance)
            val historyInstance =
                instance.copyToNotNull(FusHistoryInstance()).apply {
                    this.instanceState = InstanceState.ACTIVE
                }
            historyInstanceMapper.insert(historyInstance)
            instanceListener?.notify(EventType.CREATE, instance)
        }

        @Transactional(rollbackFor = [Throwable::class])
        override fun terminate(
            instanceId: String,
            operator: FusOperator,
        ) {
            instanceMapper.selectById(instanceId)?.apply {
                taskMapper
                    .ktQuery()
                    .eq(FusTask::instanceId, instanceId)
                    .list()
                    .forEach {
                        taskService.complete(it.taskId, operator)
                    }

                val flowHistoryInstance =
                    this.copyToNotNull(FusHistoryInstance()).apply {
                        this.instanceState = InstanceState.TERMINATED
                        this.endTime = LocalDateTime.now()
                    }
                historyInstanceMapper.updateById(flowHistoryInstance)
                instanceMapper.deleteById(instanceId)

                instanceListener?.notify(EventType.TERMINATE, this)
            }
        }

        override fun updateInstance(instance: FusInstance) {
            instanceMapper.updateById(instance)
        }

        override fun cascadeRemoveByProcessId(processId: String) {
            TODO("Not yet implemented")
        }
    }
