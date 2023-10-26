package com.tony.flow.service.impl

import com.tony.flow.db.po.FlowInstance
import com.tony.flow.db.po.FlowProcess
import com.tony.flow.model.FlowOperator
import com.tony.flow.service.RuntimeService

/**
 * RuntimServiceImpl is
 * @author tangli
 * @date 2023/10/26 15:44
 * @since 1.0.0
 */
internal class RuntimeServiceImpl:RuntimeService {
    override fun createInstance(
        flowProcess: FlowProcess,
        flowCreator: FlowOperator,
        variable: Map<String, Any?>?
    ): FlowInstance {
        TODO("Not yet implemented")
    }

    override fun createInstance(flowProcess: FlowProcess, flowCreator: FlowOperator): FlowInstance {
        TODO("Not yet implemented")
    }

    override fun complete(instanceId: Long?) {
        TODO("Not yet implemented")
    }

    override fun saveInstance(flowInstance: FlowInstance) {
        TODO("Not yet implemented")
    }

    override fun terminate(instanceId: Long, flowOperator: FlowOperator) {
        TODO("Not yet implemented")
    }

    override fun updateInstance(flowInstance: FlowInstance) {
        TODO("Not yet implemented")
    }

    override fun cascadeRemoveByProcessId(processId: Long) {
        TODO("Not yet implemented")
    }
}
