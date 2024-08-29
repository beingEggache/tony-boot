/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine.impl

import com.aizuda.bpm.engine.QueryService
import com.aizuda.bpm.engine.dao.FlwHisInstanceDao
import com.aizuda.bpm.engine.dao.FlwHisTaskActorDao
import com.aizuda.bpm.engine.dao.FlwHisTaskDao
import com.aizuda.bpm.engine.dao.FlwInstanceDao
import com.aizuda.bpm.engine.dao.FlwTaskActorDao
import com.aizuda.bpm.engine.dao.FlwTaskDao
import com.aizuda.bpm.engine.entity.FlwHisInstance
import com.aizuda.bpm.engine.entity.FlwHisTask
import com.aizuda.bpm.engine.entity.FlwHisTaskActor
import com.aizuda.bpm.engine.entity.FlwInstance
import com.aizuda.bpm.engine.entity.FlwTask
import com.aizuda.bpm.engine.entity.FlwTaskActor
import java.util.Optional

/**
 * 查询服务实现类
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public class QueryServiceImpl public constructor(
    private val instanceDao: FlwInstanceDao,
    private val hisInstanceDao: FlwHisInstanceDao,
    private val taskDao: FlwTaskDao,
    private val taskActorDao: FlwTaskActorDao,
    private val hisTaskDao: FlwHisTaskDao,
    private val hisTaskActorDao: FlwHisTaskActorDao,
) : QueryService {
    override fun getInstance(instanceId: Long?): FlwInstance? =
        instanceDao.selectById(instanceId)

    override fun getTask(taskId: Long?): FlwTask? =
        taskDao.selectById(taskId)

    override fun getHistInstance(instanceId: Long?): FlwHisInstance? =
        hisInstanceDao.selectById(instanceId)

    override fun getHistTask(taskId: Long?): FlwHisTask? =
        hisTaskDao.selectById(taskId)

    override fun getHisTasksByName(
        instanceId: Long?,
        taskName: String?,
    ): Optional<List<FlwHisTask>> =
        Optional.ofNullable(hisTaskDao.selectListByInstanceIdAndTaskName(instanceId, taskName))

    override fun getTasksByInstanceId(instanceId: Long?): List<FlwTask> =
        taskDao.selectListByInstanceId(instanceId)

    override fun getTasksByInstanceIdAndTaskName(
        instanceId: Long?,
        taskName: String?,
    ): List<FlwTask> =
        taskDao.selectListByInstanceIdAndTaskName(instanceId, taskName)

    override fun getTasksByInstanceIdAndTaskKey(
        instanceId: Long?,
        taskKey: String?,
    ): List<FlwTask> =
        taskDao.selectListByInstanceIdAndTaskKey(instanceId, taskKey)

    override fun getActiveTaskActorsByInstanceId(instanceId: Long?): Optional<List<FlwTaskActor>> =
        Optional.ofNullable(taskActorDao.selectListByInstanceId(instanceId))

    override fun getTaskActorsByTaskId(taskId: Long?): List<FlwTaskActor> =
        taskActorDao.selectListByTaskId(taskId)

    override fun getTaskActorsByTaskIdAndActorId(
        taskId: Long?,
        actorId: String?,
    ): List<FlwTaskActor> =
        taskActorDao.selectListByTaskIdAndActorId(taskId, actorId)

    override fun getHisTaskActorsByTaskId(taskId: Long?): List<FlwHisTaskActor> =
        hisTaskActorDao.selectListByTaskId(taskId)

    override fun getHisTaskActorsByTaskIdAndActorId(
        taskId: Long?,
        actorId: String?,
    ): List<FlwHisTaskActor> =
        hisTaskActorDao.selectListByTaskIdAndActorId(taskId, actorId)

    override fun getActiveTasks(
        instanceId: Long?,
        taskNames: List<String?>?,
    ): List<FlwTask> =
        taskDao.selectListByInstanceIdAndTaskNames(instanceId, taskNames)

    override fun getHisTasksByInstanceId(instanceId: Long?): List<FlwHisTask> =
        hisTaskDao.selectListByInstanceId(instanceId)
}
