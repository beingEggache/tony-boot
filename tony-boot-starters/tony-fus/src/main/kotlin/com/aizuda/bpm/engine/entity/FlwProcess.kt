/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine.entity

import com.aizuda.bpm.engine.FlowConstants
import com.aizuda.bpm.engine.ProcessModelCache
import com.aizuda.bpm.engine.assist.Assert.illegal
import com.aizuda.bpm.engine.assist.Assert.isFalse
import com.aizuda.bpm.engine.assist.Assert.isNull
import com.aizuda.bpm.engine.assist.Assert.isTrue
import com.aizuda.bpm.engine.assist.DateUtils.currentDate
import com.aizuda.bpm.engine.core.Execution
import com.aizuda.bpm.engine.core.FlowCreator
import com.aizuda.bpm.engine.core.FlowLongContext
import com.aizuda.bpm.engine.core.enums.FlowState
import com.aizuda.bpm.engine.model.ModelHelper.checkDuplicateNodeKeys
import com.aizuda.bpm.engine.model.NodeModel
import com.aizuda.bpm.engine.model.ProcessModel
import com.tony.utils.jsonToObj
import com.tony.utils.toJsonString
import java.util.Optional
import java.util.function.Function

/**
 * 流程定义实体类
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public class FlwProcess :
    FlowEntity(),
    ProcessModelCache {
    /**
     * 流程定义 key 唯一标识
     */
    public var processKey: String? = null

    /**
     * 流程定义名称
     */
    public var processName: String? = null

    /**
     * 流程图标地址
     */
    public var processIcon: String? = null

    /**
     * 流程定义类型（预留字段）
     */
    public var processType: String? = null

    /**
     * 流程版本
     */
    public var processVersion: Int? = null

    /**
     * 当前流程的实例url（一般为流程第一步的url）
     * 该字段可以直接打开流程申请的表单
     */
    public var instanceUrl: String? = null

    /**
     * 备注说明
     */
    public var remark: String? = null

    /**
     * 使用范围 0，全员 1，指定人员（业务关联） 2，均不可提交
     */
    public var useScope: Int? = null

    /**
     * 流程状态 0，不可用 1，可用 2，历史版本
     */
    public var processState: Int? = null

    /**
     * 流程模型定义JSON内容
     */
    override var modelContent: String? = null

    /**
     * 排序
     */
    public var sort: Int? = null

    public fun setFlowState(flowState: FlowState) {
        this.processState = flowState.value
    }

    override fun modelCacheKey(): String =
        FlowConstants.PROCESS_CACHE_KEY + this.id

    /**
     * 执行开始模型
     *
     * @param flowLongContext 流程引擎上下文
     * @param flowCreator     流程实例任务创建者
     * @param function        流程执行对象处理函数
     * @return 流程实例
     */
    public fun executeStartModel(
        flowLongContext: FlowLongContext,
        flowCreator: FlowCreator?,
        function: Function<NodeModel?, Execution>,
    ): Optional<FlwInstance> {
        var flwInstance: FlwInstance? = null
        if (null != this.modelContent) {
            val nodeModel = model()!!.nodeConfig
            isNull(
                nodeModel!!,
                "流程定义[processName=" + this.processName + ", processVersion=" + this.processVersion + "]没有开始节点"
            )
            isFalse(flowLongContext.taskActorProvider!!.isAllowed(nodeModel, flowCreator), "No permission to execute")
            isTrue(checkDuplicateNodeKeys(nodeModel), "There are duplicate node keys present")
            // 回调执行创建实例
            val execution = function.apply(nodeModel)
            // 重新渲染逻辑节点
            // 创建首个审批任务
            flowLongContext.createTask(execution, execution.processModel.nodeConfig!!)
            // 当前执行实例
            flwInstance = execution.flwInstance
        }
        return Optional.ofNullable(flwInstance)
    }

    /**
     * 流程状态验证
     *
     * @return 流程定义实体
     */
    public fun checkState(): FlwProcess {
        if (0 == this.processState) {
            illegal("指定的流程定义[id=" + this.id + ",processVersion=" + this.processVersion + "]为非活动状态")
        }
        return this
    }

    /**
     * 格式化 JSON 模型内容
     *
     * @param modelContent JSON 模型内容
     * @return 流程定义实体
     */
    public fun formatModelContent(modelContent: String?): FlwProcess =
        setModelContent2Json(modelContent?.jsonToObj())

    /**
     * 设置 JSON 模型内容
     *
     * @param processModel 模型内容
     * @return 流程定义实体
     */
    public fun setModelContent2Json(processModel: ProcessModel?): FlwProcess {
        this.modelContent = processModel?.toJsonString()
        return this
    }

    /**
     * 下一个流程版本
     *
     * @return 下一个流程版本
     */
    public fun nextProcessVersion(): Int =
        processVersion!! + 1

    override fun toString(): String =
        "FlwProcess(processKey=" + this.processKey + ", processName=" + this.processName + ", processIcon=" +
            this.processIcon +
            ", processType=" +
            this.processType +
            ", processVersion=" +
            this.processVersion +
            ", instanceUrl=" +
            this.instanceUrl +
            ", remark=" +
            this.remark +
            ", useScope=" +
            this.useScope +
            ", processState=" +
            this.processState +
            ", modelContent=" +
            this.modelContent +
            ", sort=" +
            this.sort +
            ")"

    public companion object {
        public fun of(
            flowCreator: FlowCreator,
            processModel: ProcessModel,
            processVersion: Int,
            jsonString: String?,
        ): FlwProcess {
            val process = FlwProcess()
            process.processVersion = processVersion
            process.setFlowState(FlowState.ACTIVE)
            process.processKey = processModel.key
            process.processName = processModel.name
            process.instanceUrl = processModel.instanceUrl
            process.useScope = 0
            process.sort = 0
            process.setFlowCreator(flowCreator)
            process.createTime = currentDate
            return process.formatModelContent(jsonString)
        }
    }
}
