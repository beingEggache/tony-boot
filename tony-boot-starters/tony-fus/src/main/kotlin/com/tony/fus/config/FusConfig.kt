/*
 * MIT License
 *
 * Copyright (c) 2023-present, tangli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.tony.fus.config

import com.tony.fus.DefaultFusTaskActorProvider
import com.tony.fus.DefaultTaskPermission
import com.tony.fus.FusTaskActorProvider
import com.tony.fus.FusTaskPermission
import com.tony.fus.db.mapper.FusExtInstanceMapper
import com.tony.fus.db.mapper.FusHistoryInstanceMapper
import com.tony.fus.db.mapper.FusHistoryTaskActorMapper
import com.tony.fus.db.mapper.FusHistoryTaskMapper
import com.tony.fus.db.mapper.FusInstanceMapper
import com.tony.fus.db.mapper.FusProcessMapper
import com.tony.fus.db.mapper.FusTaskActorMapper
import com.tony.fus.db.mapper.FusTaskMapper
import com.tony.fus.listener.EventInstanceListener
import com.tony.fus.listener.InstanceListener
import com.tony.fus.listener.TaskListener
import com.tony.fus.service.ProcessService
import com.tony.fus.service.ProcessServiceImpl
import com.tony.fus.service.QueryService
import com.tony.fus.service.QueryServiceImpl
import com.tony.fus.service.RuntimeService
import com.tony.fus.service.RuntimeServiceImpl
import com.tony.fus.service.TaskService
import com.tony.fus.service.TaskServiceImpl
import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.lang.Nullable

/**
 * FusConfig is
 * @author tangli
 * @date 2023/11/13 19:38
 * @since 1.0.0
 */
@MapperScan("com.tony.fus.db.mapper")
@Configuration
internal class FusConfig {
    @ConditionalOnMissingBean(TaskService::class)
    @Bean
    internal fun taskService(
        taskPermission: FusTaskPermission,
        taskMapper: FusTaskMapper,
        taskActorMapper: FusTaskActorMapper,
        historyTaskMapper: FusHistoryTaskMapper,
        historyTaskActorMapper: FusHistoryTaskActorMapper,
        instanceMapper: FusInstanceMapper,
        historyInstanceMapper: FusHistoryInstanceMapper,
        @Nullable
        taskListener: TaskListener?,
    ): TaskService =
        TaskServiceImpl(
            taskPermission,
            taskMapper,
            taskActorMapper,
            historyTaskMapper,
            historyTaskActorMapper,
            instanceMapper,
            historyInstanceMapper,
            taskListener
        )

    @ConditionalOnMissingBean(QueryService::class)
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

    @ConditionalOnMissingBean(RuntimeService::class)
    @Bean
    internal fun runtimeService(
        instanceMapper: FusInstanceMapper,
        historyInstanceMapper: FusHistoryInstanceMapper,
        taskMapper: FusTaskMapper,
        extInstanceMapper: FusExtInstanceMapper,
        @Nullable
        instanceListener: InstanceListener?,
    ): RuntimeService =
        RuntimeServiceImpl(
            instanceMapper,
            historyInstanceMapper,
            taskMapper,
            extInstanceMapper,
            instanceListener
        )

    @ConditionalOnMissingBean(ProcessService::class)
    @Bean
    internal fun processService(processMapper: FusProcessMapper): ProcessService =
        ProcessServiceImpl(processMapper)

    @ConditionalOnMissingBean(FusTaskPermission::class)
    @Bean
    internal fun taskPermission(): FusTaskPermission =
        DefaultTaskPermission()

    @ConditionalOnMissingBean(FusTaskActorProvider::class)
    @Bean(autowireCandidate = false)
    internal fun taskActorProvider(): FusTaskActorProvider =
        DefaultFusTaskActorProvider()

    @ConditionalOnProperty(prefix = "fus", name = ["eventing.instance"], havingValue = "true")
    @ConditionalOnMissingBean
    @Bean
    internal fun eventInstanceListener(applicationEventPublisher: ApplicationEventPublisher): EventInstanceListener =
        EventInstanceListener(applicationEventPublisher)
}
