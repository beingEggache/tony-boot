/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.spring.autoconfigure

import com.aizuda.bpm.engine.Expression
import com.aizuda.bpm.engine.FlowLongEngine
import com.aizuda.bpm.engine.ProcessModelParser
import com.aizuda.bpm.engine.ProcessService
import com.aizuda.bpm.engine.QueryService
import com.aizuda.bpm.engine.RuntimeService
import com.aizuda.bpm.engine.TaskAccessStrategy
import com.aizuda.bpm.engine.TaskActorProvider
import com.aizuda.bpm.engine.TaskCreateInterceptor
import com.aizuda.bpm.engine.TaskReminder
import com.aizuda.bpm.engine.TaskService
import com.aizuda.bpm.engine.TaskTrigger
import com.aizuda.bpm.engine.cache.FlowCache
import com.aizuda.bpm.engine.core.FlowLongContext
import com.aizuda.bpm.engine.core.FlowLongEngineImpl
import com.aizuda.bpm.engine.dao.FlwExtInstanceDao
import com.aizuda.bpm.engine.dao.FlwHisInstanceDao
import com.aizuda.bpm.engine.dao.FlwHisTaskActorDao
import com.aizuda.bpm.engine.dao.FlwHisTaskDao
import com.aizuda.bpm.engine.dao.FlwInstanceDao
import com.aizuda.bpm.engine.dao.FlwProcessDao
import com.aizuda.bpm.engine.dao.FlwTaskActorDao
import com.aizuda.bpm.engine.dao.FlwTaskDao
import com.aizuda.bpm.engine.handler.ConditionNodeHandler
import com.aizuda.bpm.engine.handler.CreateTaskHandler
import com.aizuda.bpm.engine.impl.GeneralAccessStrategy
import com.aizuda.bpm.engine.impl.GeneralTaskActorProvider
import com.aizuda.bpm.engine.impl.ProcessServiceImpl
import com.aizuda.bpm.engine.impl.QueryServiceImpl
import com.aizuda.bpm.engine.impl.RuntimeServiceImpl
import com.aizuda.bpm.engine.impl.TaskServiceImpl
import com.aizuda.bpm.engine.listener.InstanceListener
import com.aizuda.bpm.engine.listener.TaskListener
import com.aizuda.bpm.engine.scheduling.JobLock
import com.aizuda.bpm.engine.scheduling.LocalLock
import com.aizuda.bpm.spring.adaptive.SpelExpression
import com.aizuda.bpm.spring.event.EventInstanceListener
import com.aizuda.bpm.spring.event.EventTaskListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

/**
 * spring boot starter 启动自动配置处理类
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
@Configuration
@Import(FlowLongMybatisPlusConfiguration::class)
@EnableConfigurationProperties(FlowLongProperties::class)
public class FlowLongAutoConfiguration public constructor() {
    @Bean
    @ConditionalOnMissingBean
    public fun taskService(
        @Autowired(required = false) taskAccessStrategy: TaskAccessStrategy?,
        @Autowired(required = false) taskListener: TaskListener?,
        @Autowired(required = false) taskTrigger: TaskTrigger?,
        instanceDao: FlwInstanceDao,
        extInstanceDao: FlwExtInstanceDao,
        hisInstanceDao: FlwHisInstanceDao,
        taskDao: FlwTaskDao,
        taskActorDao: FlwTaskActorDao,
        hisTaskDao: FlwHisTaskDao,
        hisTaskActorDao: FlwHisTaskActorDao,
    ): TaskService =
        TaskServiceImpl(
            taskAccessStrategy,
            taskListener,
            taskTrigger,
            instanceDao,
            extInstanceDao,
            hisInstanceDao,
            taskDao,
            taskActorDao,
            hisTaskDao,
            hisTaskActorDao
        )

    @Bean
    @ConditionalOnMissingBean
    public fun queryService(
        instanceDao: FlwInstanceDao,
        hisInstanceDao: FlwHisInstanceDao,
        taskDao: FlwTaskDao,
        taskActorDao: FlwTaskActorDao,
        hisTaskDao: FlwHisTaskDao,
        hisTaskActorDao: FlwHisTaskActorDao,
    ): QueryService =
        QueryServiceImpl(instanceDao, hisInstanceDao, taskDao, taskActorDao, hisTaskDao, hisTaskActorDao)

    @Bean
    @ConditionalOnMissingBean
    public fun runtimeService(
        @Autowired(required = false) instanceListener: InstanceListener?,
        queryService: QueryService,
        taskService: TaskService,
        instanceDao: FlwInstanceDao,
        hisInstanceDao: FlwHisInstanceDao,
        extInstanceDao: FlwExtInstanceDao,
    ): RuntimeService =
        RuntimeServiceImpl(instanceListener, queryService, taskService, instanceDao, hisInstanceDao, extInstanceDao)

    @Bean
    @ConditionalOnMissingBean
    public fun processService(
        runtimeService: RuntimeService,
        processDao: FlwProcessDao,
    ): ProcessService =
        ProcessServiceImpl(runtimeService, processDao)

    @Bean
    @ConditionalOnMissingBean
    public fun jobLock(): JobLock =
        LocalLock()

    @Bean
    @ConditionalOnMissingBean
    public fun expression(): Expression =
        SpelExpression()

    @Bean
    @ConditionalOnMissingBean
    public fun taskAccessStrategy(): TaskAccessStrategy =
        GeneralAccessStrategy()

    @Bean
    @ConditionalOnMissingBean
    public fun taskActorProvider(): TaskActorProvider =
        GeneralTaskActorProvider()

    @Bean
    @ConditionalOnMissingBean
    public fun flowLongEngine(): FlowLongEngine =
        FlowLongEngineImpl()

    @Bean
    @ConditionalOnMissingBean
    public fun flowLongContext(
        processService: ProcessService?,
        queryService: QueryService?,
        runtimeService: RuntimeService?,
        taskService: TaskService?,
        expression: Expression?,
        taskAccessStrategy: TaskAccessStrategy?,
        taskActorProvider: TaskActorProvider?,
        flowLongEngine: FlowLongEngine,
        @Autowired(required = false) flowCache: FlowCache?,
        @Autowired(required = false) processModelParser: ProcessModelParser?,
        @Autowired(required = false) conditionNodeHandler: ConditionNodeHandler?,
        @Autowired(required = false) taskCreateInterceptor: TaskCreateInterceptor?,
        @Autowired(required = false) createTaskHandler: CreateTaskHandler?,
        @Autowired(required = false) taskReminder: TaskReminder?,
        @Autowired(required = false) taskTrigger: TaskTrigger?,
    ): FlowLongContext {
        // 注入 FlowLong 上下文
        val flc = FlowLongContext(flowCache, processModelParser)
        flc.processService = processService
        flc.queryService = queryService
        flc.runtimeService = runtimeService
        flc.taskService = taskService
        flc.expression = expression
        flc.taskAccessStrategy = taskAccessStrategy
        flc.taskActorProvider = taskActorProvider
        flc.conditionNodeHandler = conditionNodeHandler
        flc.taskCreateInterceptor = taskCreateInterceptor
        flc.createTaskHandler = createTaskHandler
        flc.taskReminder = taskReminder
        flc.taskTrigger = taskTrigger
        return flc.build(flowLongEngine)
    }

    /**
     * 注入自定义 TaskListener 实现该方法不再生效
     *
     * @param eventPublisher [ApplicationEventPublisher]
     * @return [EventTaskListener]
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "flowlong", name = ["eventing.task"], havingValue = "true")
    public fun taskListener(eventPublisher: ApplicationEventPublisher): EventTaskListener =
        EventTaskListener(eventPublisher)

    /**
     * 注入自定义 InstanceListener 实现该方法不再生效
     *
     * @param eventPublisher [ApplicationEventPublisher]
     * @return [EventInstanceListener]
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "flowlong", name = ["eventing.instance"], havingValue = "true")
    public fun instanceListener(eventPublisher: ApplicationEventPublisher): EventInstanceListener =
        EventInstanceListener(eventPublisher)
}
