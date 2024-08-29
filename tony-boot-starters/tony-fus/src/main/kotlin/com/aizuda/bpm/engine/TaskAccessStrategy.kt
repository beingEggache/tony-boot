/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine

import com.aizuda.bpm.engine.assist.Assert.isTrue
import com.aizuda.bpm.engine.core.FlowCreator
import com.aizuda.bpm.engine.entity.FlwTaskActor

/**
 * 任务访问策略类
 *
 *
 * 用于判断给定的创建人员是否允许执行某个任务
 *
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public interface TaskAccessStrategy {
    /**
     * 根据创建人ID、参与者集合判断是否允许访问所属任务
     *
     * @param userId     用户ID
     * @param taskActors 参与者列表
     * @return 被允许参与者 [FlwTaskActor]
     */
    public fun isAllowed(
        userId: String?,
        taskActors: List<FlwTaskActor>,
    ): FlwTaskActor?

    /**
     * 获取指定合法参与者对象
     *
     *
     * 被使用在：分配任务，解决委派任务 场景
     *
     *
     * @param taskId      当前任务ID
     * @param taskActors  通过任务ID查询到的任务参与者列表
     * @param flowCreator 任务参与者
     * @return [FlwTaskActor]
     */
    public fun getAllowedFlwTaskActor(
        taskId: Long?,
        flowCreator: FlowCreator?,
        taskActors: List<FlwTaskActor>?,
    ): FlwTaskActor {
        val taskActorOpt =
            taskActors!!
                .stream()
                .filter { t: FlwTaskActor ->
                    t.actorId == flowCreator?.createId
                }.findFirst()
        isTrue(!taskActorOpt.isPresent, "Not authorized to perform this task")
        return taskActorOpt.get()
    }
}
