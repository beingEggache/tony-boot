package com.tony.flow.service.impl

import com.tony.flow.db.po.FlowHistoryInstance
import com.tony.flow.db.po.FlowHistoryTask
import com.tony.flow.db.po.FlowHistoryTaskActor
import com.tony.flow.db.po.FlowInstance
import com.tony.flow.db.po.FlowTask
import com.tony.flow.db.po.FlowTaskActor
import com.tony.flow.service.QueryService

/**
 * QueryServiceImpl is
 * @author tangli
 * @date 2023/10/26 15:44
 * @since 1.0.0
 */
internal class QueryServiceImpl:QueryService {
    override fun getInstance(instanceId: Long): FlowInstance {
        TODO("Not yet implemented")
    }

    override fun getHistoryInstance(instanceId: Long): FlowHistoryInstance {
        TODO("Not yet implemented")
    }

    override fun getTask(taskId: Long): FlowTask {
        TODO("Not yet implemented")
    }

    override fun listTask(instanceId: Long, taskNames: Collection<String>): List<FlowTask> {
        TODO("Not yet implemented")
    }

    override fun listTaskByInstanceId(instanceId: Long?): List<FlowTask> {
        TODO("Not yet implemented")
    }

    override fun getHistoryTask(taskId: Long): FlowHistoryTask {
        TODO("Not yet implemented")
    }

    override fun listHistoryTask(instanceId: Long): List<FlowHistoryTask> {
        TODO("Not yet implemented")
    }

    override fun listHistoryTaskByName(instanceId: Long, taskName: String): List<FlowHistoryTask> {
        TODO("Not yet implemented")
    }

    override fun listTaskActorByTaskId(taskId: Long): List<FlowTaskActor> {
        TODO("Not yet implemented")
    }

    override fun listHistoryTaskActorByTaskId(taskId: Long): List<FlowHistoryTaskActor> {
        TODO("Not yet implemented")
    }
}
