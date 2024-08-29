/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine.impl

import com.aizuda.bpm.engine.ProcessService
import com.aizuda.bpm.engine.RuntimeService
import com.aizuda.bpm.engine.assist.Assert
import com.aizuda.bpm.engine.assist.Assert.isFalse
import com.aizuda.bpm.engine.assist.Assert.isNull
import com.aizuda.bpm.engine.assist.Assert.isTrue
import com.aizuda.bpm.engine.assist.Assert.throwable
import com.aizuda.bpm.engine.assist.ObjectUtils
import com.aizuda.bpm.engine.assist.ObjectUtils.isNotEmpty
import com.aizuda.bpm.engine.core.FlowCreator
import com.aizuda.bpm.engine.core.FlowLongContext
import com.aizuda.bpm.engine.core.enums.FlowState
import com.aizuda.bpm.engine.dao.FlwProcessDao
import com.aizuda.bpm.engine.entity.FlwProcess
import java.util.function.Consumer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * 流程定义业务类
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public class ProcessServiceImpl(
    private val runtimeService: RuntimeService,
    private val processDao: FlwProcessDao,
) : ProcessService {
    /**
     * 更新process的类别
     */
    override fun updateType(
        id: Long?,
        processType: String?,
    ) {
        val process = FlwProcess()
        process.id = id
        process.processType = processType
        processDao.updateById(process)
    }

    /**
     * 根据id获取process对象
     * 先通过cache获取，如果返回空，就从数据库读取并put
     */
    override fun getProcessById(id: Long?): FlwProcess? {
        val process = processDao.selectById(id)
        isTrue(ObjectUtils.isEmpty(process), "process id [$id] does not exist")
        return process
    }

    /**
     * 根据流程名称或版本号查找流程定义对象
     *
     * @param processKey 流程定义key
     * @param version    版本号
     * @return [Process]
     */
    override fun getProcessByVersion(
        tenantId: String?,
        processKey: String?,
        version: Int?,
    ): FlwProcess {
        Assert.isEmpty(processKey)
        val processList = processDao.selectListByProcessKeyAndVersion(tenantId, processKey, version)
        isTrue(ObjectUtils.isEmpty(processList), "process key [$processKey] does not exist")
        return processList[0]
    }

    /**
     * 根据流程定义json字符串，部署流程定义
     *
     * @param processId   流程定义ID
     * @param jsonString  流程定义json字符串
     * @param flowCreator 流程任务部署者
     * @param repeat      是否重复部署 true 存在版本+1新增一条记录 false 存在流程直接返回
     * @param processSave 保存流程定义消费者函数
     * @return 流程ID
     */
    override fun deploy(
        processId: Long?,
        jsonString: String?,
        flowCreator: FlowCreator?,
        repeat: Boolean,
        processSave: Consumer<FlwProcess?>?,
    ): Long? {
        isNull(jsonString)
        try {
            val processModel = FlowLongContext.parseProcessModel(jsonString, null, false)
            var dbProcess: FlwProcess? = null
            if (null == processId) {
                /*
                 * 查询流程信息获取最后版本号
                 */
                val processList =
                    processDao.selectListByProcessKeyAndVersion(
                        flowCreator?.tenantId,
                        processModel.key,
                        null
                    )
                if (isNotEmpty(processList)) {
                    dbProcess = processList[0]
                }
            } else {
                dbProcess = processDao.selectById(processId)
            }

            var processVersion = 1
            if (null != dbProcess) {
                // 不允许重复部署，直接返回当前流程定义ID

                if (!repeat) {
                    return dbProcess.id
                }

                // 不允许历史状态部署
                isTrue(FlowState.HISTORY.eq(dbProcess.processState), "Not allowed status")

                /*
                 * 设置为历史流程
                 */
                val rows: Boolean
                val his = FlwProcess()
                his.setFlowState(FlowState.HISTORY)
                if (processModel.key == dbProcess.processKey) {
                    // 流程定义key未发生改变直接修改为历史即可
                    his.id = dbProcess.id
                    rows = processDao.updateById(his) > 0
                } else {
                    // 流程定义KEY被修改历史KEY修改为最新KEY并重置为历史状态
                    his.processKey = processModel.key
                    rows = processDao.updateByProcessKey(his, dbProcess.tenantId, dbProcess.processKey)
                }
                isFalse(rows, "Set as historical process failed")
                processVersion = dbProcess.nextProcessVersion()
            }

            /*
             * 添加一条新的流程记录
             */
            val process = FlwProcess.of(flowCreator!!, processModel, processVersion, jsonString)
            processSave?.accept(process)
            isFalse(processDao.insert(process) > 0, "Failed to save the deployment process")
            return process.id
        } catch (e: Exception) {
            log.error(e.message)
            throw throwable(e)
        }
    }

    /**
     * 卸载指定的定义流程，更新为未启用状态
     *
     * @param id 流程定义ID
     * @return true 成功 false 失败
     */
    override fun undeploy(id: Long?) {
        val process = FlwProcess()
        process.id = id
        process.setFlowState(FlowState.INACTIVE)
        processDao.updateById(process)
    }

    /**
     * 级联删除指定流程定义的所有数据
     */
    override fun cascadeRemove(id: Long?) {
        // 删除与流程相关的实例
        runtimeService.cascadeRemoveByProcessId(id)

        // 删除部署流程流程信息
        processDao.deleteById(id)
    }

    public companion object {
        private val log: Logger = LoggerFactory.getLogger(ProcessServiceImpl::class.java)
    }
}
