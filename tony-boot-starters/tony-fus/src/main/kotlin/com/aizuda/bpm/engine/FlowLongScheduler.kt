package com.aizuda.bpm.engine

import com.aizuda.bpm.engine.assist.Assert.isNull
import com.aizuda.bpm.engine.assist.DateUtils.after
import com.aizuda.bpm.engine.assist.DateUtils.currentDate
import com.aizuda.bpm.engine.assist.ObjectUtils.isNotEmpty
import com.aizuda.bpm.engine.core.enums.TaskType
import com.aizuda.bpm.engine.entity.FlwTask
import com.aizuda.bpm.engine.scheduling.JobLock
import com.aizuda.bpm.engine.scheduling.RemindParam
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * 定时任务实现流程提醒超时处理类
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public abstract class FlowLongScheduler {
    /**
     * FlowLong流程引擎接口
     */
    public var flowLongEngine: FlowLongEngine? = null

    /**
     * 任务锁，可注入分布式锁实现
     */
    public var jobLock: JobLock? = null

    /**
     * 提醒参数
     */
    public var remindParam: RemindParam? = null
        set(remindParam) {
            var remindParam = remindParam
            if (null == remindParam) {
                /*
                 * 未配置定时任务提醒参数，默认 cron 为5秒钟执行一次
                 */
                remindParam = RemindParam()
                remindParam.cron = "*/5 * * * * ?"
            }
            field = remindParam
        }

    /**
     * 流程提醒处理
     */
    public fun remind() {
        try {
            if (!jobLock!!.tryLock()) {
                log.info("[FlowLong] Scheduling is already running, just return.")
                return
            }
            val context = flowLongEngine!!.context
            val taskService = context!!.taskService
            val flwTaskList = taskService!!.timeoutOrRemindTasks
            if (isNotEmpty(flwTaskList)) {
                val currentDate = currentDate
                for (flwTask in flwTaskList) {
                    if (null != flwTask.remindTime) {
                        /*
                         * 任务提醒
                         */
                        if (after(flwTask.remindTime, currentDate) && flwTask.remindRepeat!! > 0) {
                            // 1，更新提醒次数加 1 次
                            val temp = FlwTask()
                            temp.id = flwTask.id
                            temp.remindRepeat = flwTask.remindRepeat!! + 1
                            // 2，调用提醒接口
                            val taskReminder = context.taskReminder
                            isNull(taskReminder, "Please make sure to implement the interface TaskReminder")
                            val nextRemindTime = taskReminder!!.remind(context, flwTask.instanceId, flwTask)
                            if (null != nextRemindTime) {
                                temp.remindTime = nextRemindTime
                            }
                            taskService.updateTaskById(temp, null)
                        }
                    } else if (null != flwTask.expireTime) {
                        // 定时器任务或触发器任务直接执行通过
                        if (TaskType.TIMER.eq(flwTask.taskType) || TaskType.TRIGGER.eq(flwTask.taskType)) {
                            if (!flowLongEngine!!.autoCompleteTask(flwTask.id)) {
                                log.info(
                                    "Scheduling [taskName={}] failed to execute autoCompleteTask",
                                    flwTask.taskName
                                )
                            }
                            continue
                        }

                        // 获取当前执行模型节点
                        val processModel =
                            flowLongEngine!!.runtimeService()!!.getProcessModelByInstanceId(
                                flwTask.instanceId
                            )
                        val nodeModel = processModel!!.getNode(flwTask.taskKey)
                        val termMode = nodeModel!!.termMode
                        if (null == termMode) {
                            // 执行超时
                            context.runtimeService!!.timeout(flwTask.instanceId)
                        } else if (termMode == 0) {
                            // 自动通过
                            if (!flowLongEngine!!.autoCompleteTask(flwTask.id)) {
                                log.info("Scheduling failed to execute autoCompleteTask")
                            }
                        } else if (termMode == 1) {
                            // 自动拒绝
                            if (!flowLongEngine!!.autoRejectTask(flwTask.id)) {
                                log.info("Scheduling failed to execute autoRejectTask")
                            }
                        }
                    }
                }
            }
        } finally {
            jobLock!!.unlock()
        }
    }

    public companion object {
        private val log: Logger = LoggerFactory.getLogger(FlowLongScheduler::class.java)
    }
}
