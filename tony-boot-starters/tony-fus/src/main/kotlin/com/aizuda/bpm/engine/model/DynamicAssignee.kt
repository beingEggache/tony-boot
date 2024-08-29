/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine.model

/**
 * JSON BPM 节点处理人或角色动态分配对象
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public class DynamicAssignee {
    /**
     * 分配到任务的人或角色列表
     */
    public var assigneeList: List<NodeAssignee>? = null

    /**
     * 分配类型  0，用户 1，角色  2，部门 该属性决定 assigneeList 属性是分配到人还是角色
     */
    public var type: Int? = null

    public fun type(type: Int?): DynamicAssignee {
        this.type = type
        return this
    }

    public fun assigneeList(assigneeList: List<NodeAssignee>?): DynamicAssignee {
        this.assigneeList = assigneeList
        return this
    }

    public companion object {
        @JvmStatic
        public fun ofNodeModel(nodeModel: NodeModel): DynamicAssignee =
            builder().assigneeList(nodeModel.nodeAssigneeList).type(nodeModel.type)

        @JvmStatic
        public fun builder(): DynamicAssignee =
            DynamicAssignee()

        @JvmStatic
        public fun assigneeUserList(assigneeList: List<NodeAssignee>?): DynamicAssignee =
            builder().assigneeList(assigneeList).type(0)

        @JvmStatic
        public fun assigneeRoleList(assigneeList: List<NodeAssignee>?): DynamicAssignee =
            builder().assigneeList(assigneeList).type(1)

        @JvmStatic
        public fun assigneeDeptList(assigneeList: List<NodeAssignee>?): DynamicAssignee =
            builder().assigneeList(assigneeList).type(2)
    }
}
