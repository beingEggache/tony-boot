/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine.model

import com.aizuda.bpm.engine.ModelInstance
import com.aizuda.bpm.engine.TaskTrigger
import com.aizuda.bpm.engine.assist.Assert
import com.aizuda.bpm.engine.assist.ObjectUtils
import com.aizuda.bpm.engine.core.Execution
import com.aizuda.bpm.engine.core.FlowLongContext
import com.aizuda.bpm.engine.core.enums.NodeSetType
import com.aizuda.bpm.engine.core.enums.PerformType
import com.aizuda.bpm.engine.core.enums.TaskType
import com.aizuda.bpm.engine.entity.FlwProcess
import java.io.Serializable
import java.util.Optional
import java.util.function.Function

/**
 * JSON BPM 节点
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public class NodeModel public constructor() :
    ModelInstance,
    Serializable {
        /**
         * 节点名称
         */
        public var nodeName: String? = null

        /**
         * 节点 key
         */
        public var nodeKey: String? = null

        /**
         * 调用外部流程 [FlwProcess]
         *
         *
         * 实际业务存储格式 processId:processName 流程ID名称冒号拼接内容
         *
         *
         *
         * 不存在冒号为 processKey 内容用于测试等场景
         *
         */
        public var callProcess: String? = null

        /**
         * 任务关联的表单url
         */
        public var actionUrl: String? = null

        /**
         * 节点类型
         *
         *
         * -1，结束节点 0，发起人 1，审批人 2，抄送人 3，条件审批 4，条件分支 5，办理子流程 6，定时器任务 7，触发器任务 8，并发分支 9，包容分支
         *
         */
        public var type: Int? = null

        /**
         * 审核人类型
         *
         *
         * 1，指定成员 2，主管 3，角色 4，发起人自选 5，发起人自己 6，连续多级主管 7，部门
         *
         */
        public var setType: Int? = null

        /**
         * 审核分配到任务的处理者，过 setType 区分个人角色或部门
         */
        public var nodeAssigneeList: List<NodeAssignee>? = null

        /**
         * 指定主管层级
         */
        public var examineLevel: Int? = null

        /**
         * 自定义连续主管审批层级
         */
        public var directorLevel: Int? = null

        /**
         * 发起人自选类型
         *
         *
         * 1，自选一个人 2，自选多个人
         *
         */
        public var selectMode: Int? = null

        /**
         * 超时自动审批
         */
        public var termAuto: Boolean? = null

        /**
         * 审批期限（小时）
         */
        public var term: Int? = null

        /**
         * 审批期限超时后执行类型
         *
         *
         * 0，自动通过 1，自动拒绝
         *
         */
        public var termMode: Int? = null

        /**
         * 多人审批时审批方式 [PerformType]
         *
         *
         * 1，按顺序依次审批 2，会签 (可同时审批，每个人必须审批通过) 3，或签 (有一人审批通过即可) 4，票签 (总权重大于 50% 表示通过)
         *
         */
        public var examineMode: Int? = null

        /**
         * 连续主管审批方式
         *
         *
         * 0，直到最上级主管 1，自定义审批终点
         *
         */
        public var directorMode: Int? = null

        /**
         * 审批类型 1，人工审批 2，自动通过 3，自动拒绝
         */
        public var typeOfApprove: Int? = null

        /**
         * 通过权重（ 所有分配任务权重之和大于该值即通过，默认 50 ）
         */
        public var passWeight: Int? = null

        /**
         * 条件节点列表
         */
        public var conditionNodes: List<ConditionNode>? = null

        /**
         * 并行节点
         *
         * 相当于并行网关
         */
        public var parallelNodes: List<NodeModel>? = null

        /**
         * 包容节点
         *
         * 相当于包容网关
         */
        public var inclusiveNodes: List<ConditionNode>? = null

        /**
         * 审批提醒
         */
        public var remind: Boolean? = null

        /**
         * 允许发起人自选抄送人
         */
        public var allowSelection: Boolean? = null

        /**
         * 允许转交
         */
        public var allowTransfer: Boolean? = null

        /**
         * 允许加签/减签
         */
        public var allowAppendNode: Boolean? = null

        /**
         * 允许回退
         */
        public var allowRollback: Boolean? = null

        /**
         * 审批人与提交人为同一人时 [NodeApproveSelf]
         *
         *
         * 0，由发起人对自己审批 1，自动跳过 2，转交给直接上级审批 3，转交给部门负责人审批
         *
         */
        public var approveSelf: Int? = null

        /**
         * 扩展配置，用于存储表单权限、操作权限 等控制参数配置
         *
         *
         * 定时器任务：自定义参数 time 触发时间
         *
         *
         *
         * 例如：一小时后触发 {"time": "1:h"} 单位【 d 天 h 时 m 分 】
         *
         *
         *
         * 发起后一小时三十分后触发 {"time": "01:30:00"}
         *
         */
        public var extendConfig: Map<String, Any>? = null

        /**
         * 子节点
         */
        public var childNode: NodeModel? = null

        /**
         * 父节点，模型 json 不存在该属性、属于逻辑节点
         */
        public var parentNode: NodeModel? = null

        /**
         * 触发器类型 1，立即执行 2，延迟执行
         */
        public var triggerType: String? = null

        /**
         * 延时处理类型 1，固定时长 2，自动计算
         *
         *
         * 固定时长 "time": "1:m"
         *
         *
         *
         * 自动计算 "time": "17:02:53"
         *
         */
        public var delayType: String? = null

        /**
         * 执行节点
         *
         * @param flowLongContext 流程引擎上下文
         * @param execution       执行对象
         * @return 执行结果 true 成功 false 失败
         */
        override fun execute(
            flowLongContext: FlowLongContext,
            execution: Execution,
        ): Boolean {
            if (ObjectUtils.isNotEmpty(conditionNodes)) {
            /*
             * 执行条件分支
             */
                flowLongContext.flowConditionHandler
                    .getConditionNode(flowLongContext, execution, this)
                    .let {
                        this.executeConditionNode(flowLongContext, execution, it)
                    }
                return true
            }

            if (ObjectUtils.isNotEmpty(parallelNodes)) {
            /*
             * 执行并行分支
             */
                for (parallelNode in parallelNodes!!) {
                    if (TaskType.CONDITION_NODE.eq(parallelNode.type)) {
                        parallelNode.childNode!!.execute(flowLongContext, execution)
                    } else {
                        parallelNode.execute(flowLongContext, execution)
                    }
                }
                return true
            }

            if (ObjectUtils.isNotEmpty(inclusiveNodes)) {
            /*
             * 执行包容分支
             */
                flowLongContext.flowConditionHandler
                    .getInclusiveNodes(flowLongContext, execution, this)
                    .forEach { s ->
                        this.executeConditionNode(flowLongContext, execution, s)
                    }

                return true
            }

        /*
         * 执行 1、审批任务 2、创建抄送 5、办理子流程 6、定时器任务 7、触发器任务
         */
            if (TaskType.APPROVAL.eq(this.type) ||
                TaskType.CC.eq(this.type) ||
                TaskType.CALL_PROCESS.eq(this.type) ||
                TaskType.TIMER.eq(this.type) ||
                TaskType.TRIGGER.eq(this.type)
            ) {
                flowLongContext.createTask(execution, this)
            } else if (TaskType.END.eq(this.type)) {
                return execution.endInstance(this)
            }

        /*
         * 不存在子节点，不存在其它分支节点，当前执行节点为最后节点 并且当前节点不是审批节点
         * 执行结束流程处理器
         */
            if (null == this.childNode && null == this.conditionNodes) {
                if (!nextNode().isPresent && !TaskType.APPROVAL.eq(this.type)) {
                    execution.endInstance(this)
                }
            }
            return true
        }

        /**
         * 执行条件节点分支
         *
         * @param flowLongContext [FlowLongContext]
         * @param execution       [Execution]
         * @param conditionNode   [ConditionNode]
         */
        public fun executeConditionNode(
            flowLongContext: FlowLongContext,
            execution: Execution,
            conditionNode: ConditionNode?,
        ) {
            var childNode = conditionNode?.childNode
            if (null == childNode) {
                // 当前条件节点无执行节点，进入当前执行条件节点的下一个节点
                childNode = this.childNode
            }
            childNode?.execute(flowLongContext, execution)
                ?: // 查看是否存在其他的节点 fix https://gitee.com/aizuda/flowlong/issues/I9O8GV
                if (nextNode().isPresent) {
                    nextNode().ifPresent { nodeModel: NodeModel? -> nodeModel!!.execute(flowLongContext, execution) }
                } else {
                    // 不存在任何子节点结束流程
                    execution.endInstance(this)
                }
        }

        /**
         * 获取process定义的指定节点key的节点模型
         *
         * @param nodeKey 节点key
         * @return 模型节点
         */
        public fun getNode(nodeKey: String?): NodeModel? {
            if (this.nodeKey == nodeKey) {
                return this
            }

            // 条件分支
            val fromConditionNode = this.getFromConditionNodes(nodeKey, conditionNodes)
            if (fromConditionNode != null) {
                return fromConditionNode
            }

            // 并行分支
            val fromParallelNode = this.getFromNodeModels(nodeKey, parallelNodes)
            if (fromParallelNode != null) {
                return fromParallelNode
            }

            // 包容分支
            val fromInclusiveNode = this.getFromConditionNodes(nodeKey, inclusiveNodes)
            if (fromInclusiveNode != null) {
                return fromInclusiveNode
            }

            // 条件节点中没有找到 那么去它的同级子节点中继续查找
            if (null != childNode) {
                return childNode!!.getNode(nodeKey)
            }
            return null
        }

        /**
         * 从节点列表中获取指定key节点信息
         *
         * @param nodeKey    节点 key
         * @param nodeModels 节点模型列表
         * @return 模型节点
         */
        private fun getFromNodeModels(
            nodeKey: String?,
            nodeModels: List<NodeModel>?,
        ): NodeModel? {
            if (null != nodeModels) {
                for (nodeModel in nodeModels) {
                    val selectNode = nodeModel.getNode(nodeKey)
                    if (null != selectNode) {
                        return selectNode
                    }
                }
            }
            return null
        }

        /**
         * 从条件节点中获取节点
         *
         * @param nodeKey        节点 key
         * @param conditionNodes 条件节点模型列表
         * @return 模型节点
         */
        private fun getFromConditionNodes(
            nodeKey: String?,
            conditionNodes: List<ConditionNode>?,
        ): NodeModel? {
            if (null != conditionNodes) {
                for (conditionNode in conditionNodes) {
                    val conditionChildNode = conditionNode.childNode
                    if (null != conditionChildNode) {
                        val nodeModel = conditionChildNode.getNode(nodeKey)
                        if (null != nodeModel) {
                            return nodeModel
                        }
                    }
                }
            }
            return null
        }

        /**
         * 下一个执行节点
         *
         * @return 模型节点
         */
        public fun nextNode(): Optional<NodeModel> =
            nextNode(null)

        /**
         * 下一个执行节点
         *
         * @param currentTask 当前任务
         * @return 模型节点
         */
        public fun nextNode(currentTask: List<String?>?): Optional<NodeModel> {
            var nextNode = this.childNode
            if (null == nextNode) {
                // 如果当前节点完成，并且该节点为条件节点，找到主干执行节点继续执行
                nextNode = ModelHelper.findNextNode(this, currentTask)
            }
            return Optional.ofNullable(nextNode)
        }

        /**
         * 判断是否为条件节点
         *
         * @return true 是 false 否
         */
        public fun conditionNode(): Boolean =
            TaskType.CONDITION_NODE.eq(type) || TaskType.CONDITION_BRANCH.eq(type)

        /**
         * 判断是否为抄送节点
         *
         * @return true 是 false 否
         */
        public fun ccNode(): Boolean =
            TaskType.CC.eq(type)

        /**
         * 判断是否为并行节点
         *
         * @return true 是 false 否
         */
        public fun parallelNode(): Boolean =
            TaskType.PARALLEL_BRANCH.eq(type)

        /**
         * 判断是否为包容节点
         *
         * @return true 是 false 否
         */
        public fun inclusiveNode(): Boolean =
            TaskType.INCLUSIVE_BRANCH.eq(type)

        /**
         * 参与者类型 0，用户 1，角色 2，部门
         */
        public fun actorType(): Int {
            if (NodeSetType.ROLE.eq(setType)) {
                return 1
            }
            if (NodeSetType.DEPARTMENT.eq(setType)) {
                return 2
            }
            return 0
        }

        /**
         * 执行触发器
         *
         * @param execution [Execution]
         * @param function  执行默认触发器执行函数
         */
        public fun executeTrigger(
            execution: Execution?,
            function: Function<Exception?, Boolean>?,
        ) {
            var flag = false
            val extendConfig = this.extendConfig
            if (null != extendConfig) {
                val pTrigger = extendConfig["trigger"]
                if (null != pTrigger) {
                    try {
                        val triggerClass = Class.forName(pTrigger as String)
                        if (TaskTrigger::class.java.isAssignableFrom(triggerClass)) {
                            val taskTrigger = ObjectUtils.newInstance(triggerClass) as TaskTrigger
                            flag = taskTrigger.execute(this, execution)
                        }
                    } catch (e: Exception) {
                        // 使用默认触发器
                        if (null != function) {
                            flag = function.apply(e)
                        }
                    }
                }
            }
            Assert.isFalse(flag, "trigger execute error")
        }
    }
