package com.tony.fus.config

import com.tony.fus.DefaultFusTaskActorProvider
import com.tony.fus.DefaultOperatorNameProvider
import com.tony.fus.DefaultTaskPermission
import com.tony.fus.FusContext
import com.tony.fus.FusEngine
import com.tony.fus.FusEngineImpl
import com.tony.fus.FusInterceptor
import com.tony.fus.FusOperatorNameProvider
import com.tony.fus.FusTaskActorProvider
import com.tony.fus.FusTaskPermission
import com.tony.fus.db.mapper.FusHistoryInstanceMapper
import com.tony.fus.db.mapper.FusHistoryTaskActorMapper
import com.tony.fus.db.mapper.FusHistoryTaskMapper
import com.tony.fus.db.mapper.FusInstanceMapper
import com.tony.fus.db.mapper.FusProcessMapper
import com.tony.fus.db.mapper.FusTaskActorMapper
import com.tony.fus.db.mapper.FusTaskCcMapper
import com.tony.fus.db.mapper.FusTaskMapper
import com.tony.fus.listener.InstanceListener
import com.tony.fus.listener.TaskListener
import com.tony.fus.model.FusExpressionEvaluator
import com.tony.fus.model.SpelExpressionEvaluator
import com.tony.fus.service.ProcessService
import com.tony.fus.service.QueryService
import com.tony.fus.service.RuntimeService
import com.tony.fus.service.TaskService
import com.tony.fus.service.impl.ProcessServiceImpl
import com.tony.fus.service.impl.QueryServiceImpl
import com.tony.fus.service.impl.RuntimeServiceImpl
import com.tony.fus.service.impl.TaskServiceImpl
import org.mybatis.spring.annotation.MapperScan
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * FusConfig is
 * @author tangli
 * @date 2023/11/13 14:38
 * @since 1.0.0
 */
@MapperScan("com.tony.fus.db.mapper")
@Configuration
internal class FusConfig {
    @ConditionalOnMissingBean
    @Bean
    internal fun taskService(
        taskPermission: FusTaskPermission,
        taskMapper: FusTaskMapper,
        taskActorMapper: FusTaskActorMapper,
        taskCcMapper: FusTaskCcMapper,
        historyTaskMapper: FusHistoryTaskMapper,
        historyTaskActorMapper: FusHistoryTaskActorMapper,
        @Autowired(required = false)
        taskListener: TaskListener?,
    ): TaskService =
        TaskServiceImpl(
            taskPermission,
            taskMapper,
            taskActorMapper,
            taskCcMapper,
            historyTaskMapper,
            historyTaskActorMapper,
            taskListener
        )

    @ConditionalOnMissingBean
    @Bean
    internal fun queryService(
        instanceMapper: FusInstanceMapper,
        historyInstanceMapper: FusHistoryInstanceMapper,
        taskMapper: FusTaskMapper,
        taskActorMapper: FusTaskActorMapper,
        historyTaskMapper: FusHistoryTaskMapper,
        historyTaskActorMapper: FusHistoryTaskActorMapper,
    ): QueryService =
        QueryServiceImpl(
            instanceMapper,
            historyInstanceMapper,
            taskMapper,
            taskActorMapper,
            historyTaskMapper,
            historyTaskActorMapper
        )

    @ConditionalOnMissingBean
    @Bean
    internal fun runtimeService(
        instanceMapper: FusInstanceMapper,
        historyInstanceMapper: FusHistoryInstanceMapper,
        taskMapper: FusTaskMapper,
        taskService: TaskService,
        @Autowired(required = false)
        instanceListener: InstanceListener?,
    ): RuntimeService =
        RuntimeServiceImpl(
            instanceMapper,
            historyInstanceMapper,
            taskMapper,
            taskService,
            instanceListener
        )

    @ConditionalOnMissingBean
    @Bean
    internal fun processService(processMapper: FusProcessMapper): ProcessService =
        ProcessServiceImpl(processMapper)

    @ConditionalOnMissingBean
    @Bean
    internal fun taskPermission(): FusTaskPermission =
        DefaultTaskPermission()

    @ConditionalOnMissingBean
    @Bean
    internal fun expressionEvaluator(): FusExpressionEvaluator =
        SpelExpressionEvaluator()

    @ConditionalOnMissingBean
    @Bean
    internal fun taskActorProvider(): FusTaskActorProvider =
        DefaultFusTaskActorProvider()

    @ConditionalOnMissingBean
    @Bean
    internal fun operatorNameProvider(): FusOperatorNameProvider =
        DefaultOperatorNameProvider()

    @ConditionalOnMissingBean
    @Bean
    internal fun fusContext(
        processService: ProcessService,
        queryService: QueryService,
        runtimeService: RuntimeService,
        taskService: TaskService,
        expressionEvaluator: FusExpressionEvaluator,
        taskPermission: FusTaskPermission,
        interceptors: List<FusInterceptor>,
        taskActorProvider: FusTaskActorProvider,
    ): FusContext =
        FusContext(
            processService,
            queryService,
            runtimeService,
            taskService,
            expressionEvaluator,
            taskPermission,
            interceptors,
            taskActorProvider
        )

    @ConditionalOnMissingBean
    @Bean
    internal fun fusEngine(context: FusContext): FusEngine =
        FusEngineImpl(context)
}
