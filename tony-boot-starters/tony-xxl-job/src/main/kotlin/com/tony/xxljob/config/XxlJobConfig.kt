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

/**
 * XxlJobConfig
 *
 * @author Tang Li
 * @date 2022/6/13 14:15
 */
package com.tony.xxljob.config

import com.tony.utils.localIp
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@ConditionalOnExpression("\${xxl.job.enabled:true}")
@Configuration
@EnableConfigurationProperties(XxlJobProperties::class)
internal class XxlJobConfig(
    private val xxlJobProperties: XxlJobProperties,
) {
    private val logger: Logger = LoggerFactory.getLogger(XxlJobConfig::class.java)

    @Bean
    fun xxlJobExecutor(): XxlJobSpringExecutor {
        logger.info(">>>>>>>>>>> xxl-job config init.")
        return XxlJobSpringExecutor().apply {
            setAdminAddresses(xxlJobProperties.adminAddresses)
            setAppname(xxlJobProperties.executorAppName)
            setAddress(xxlJobProperties.executorAddress)

            if (xxlJobProperties.executorIp.isNullOrBlank()) {
                logger.info(">>>>>>>>>>> xxl-job given executorIp is null or blank,now executor ip is $localIp")
                setIp(localIp)
            } else {
                logger.info(">>>>>>>>>>> xxl-job executor ip is ${xxlJobProperties.executorIp}")
                setIp(xxlJobProperties.executorIp)
            }

            xxlJobProperties.executorPort?.let {
                setPort(it)
                logger.info(">>>>>>>>>>> xxl-job port is:$it")
            }
            setAccessToken(xxlJobProperties.accessToken)
            setLogPath(xxlJobProperties.executorLogPath)
            xxlJobProperties.executorLogRetentionDays?.let {
                setLogRetentionDays(it)
                logger.info(">>>>>>>>>>> xxl-job executorLogRetentionDays is $it")
            }

            logger.info(">>>>>>>>>>> xxl-job executorLogPath is ${xxlJobProperties.executorLogPath}")
            logger.info(">>>>>>>>>>> xxl-job adminAddresses is ${xxlJobProperties.adminAddresses}.")
            logger.info(">>>>>>>>>>> xxl-job executorAppName is ${xxlJobProperties.executorAppName}.")
            logger.info(">>>>>>>>>>> xxl-job executorAddress is ${xxlJobProperties.executorAddress}.")
        }
    }
}

@ConfigurationProperties(prefix = "xxl.job")
internal data class XxlJobProperties
    @ConstructorBinding
    constructor(
        /**
         * 调度中心部署跟地址 选填：
         * 如调度中心集群部署存在多个地址则用逗号分隔。执行器将会使用该地址进行"执行器心跳注册"和"任务结果回调"；为空则关闭自动注册；
         */
        val adminAddresses: String?,
        /**
         * 执行器AppName 选填：执行器心跳注册分组依据；为空则关闭自动注册
         */
        val accessToken: String?,
        /**
         * 执行器AppName 选填：执行器心跳注册分组依据；为空则关闭自动注册
         */
        val executorAppName: String?,
        /**
         * 执行器注册 选填：
         * 优先使用该配置作为注册地址，为空时使用内嵌服务 ”IP:PORT“ 作为注册地址。从而更灵活的支持容器类型执行器动态IP和动态映射端口问题。
         */
        val executorAddress: String?,
        /**
         * 执行器IP 选填：
         * 默认为空表示自动获取IP，多网卡时可手动设置指定IP，该IP不会绑定Host仅作为通讯实用；地址信息用于 "执行器注册" 和 "调度中心请求并触发任务"；
         */
        val executorIp: String?,
        /**
         * 执行器端口号 选填：
         * 小于等于0则自动获取；默认端口为9999，单机部署多个执行器时，注意要配置不同执行器端口；
         */
        val executorPort: Int?,
        /**
         * 执行器运行日志文件存储磁盘路径 选填 ：
         * 需要对该路径拥有读写权限；为空则使用默认路径；
         */
        val executorLogPath: String?,
        /**
         * 执行器日志文件保存天数 选填 ：
         * 过期日志自动清理, 限制值大于等于3时生效; 否则, 如-1, 关闭自动清理功能；
         */
        val executorLogRetentionDays: Int?,
    )
