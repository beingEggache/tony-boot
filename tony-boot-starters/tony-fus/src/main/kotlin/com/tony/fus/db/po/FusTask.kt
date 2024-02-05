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

package com.tony.fus.db.po

import com.baomidou.mybatisplus.annotation.FieldFill
import com.baomidou.mybatisplus.annotation.FieldStrategy
import com.baomidou.mybatisplus.annotation.OrderBy
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.tony.fus.db.enums.PerformType
import com.tony.fus.db.enums.TaskType
import com.tony.mybatis.MetaColumn
import com.tony.mybatis.MybatisPlusMetaProperty
import java.time.LocalDateTime

/**
 * 任务表
 * @TableName fus_task
 */
@TableName
public open class FusTask {
    /**
     * 主键ID
     */
    @TableId
    public var taskId: String = ""

    /**
     * 租户ID
     */
    @MybatisPlusMetaProperty(MetaColumn.TENANT_ID)
    @TableField(
        fill = FieldFill.INSERT,
        updateStrategy = FieldStrategy.NEVER
    )
    public var tenantId: String = ""

    /**
     * 创建人ID
     */
    @MybatisPlusMetaProperty(MetaColumn.USER_ID)
    @TableField(
        fill = FieldFill.INSERT,
        updateStrategy = FieldStrategy.NEVER
    )
    public var creatorId: String = ""

    /**
     * 创建人
     */
    @MybatisPlusMetaProperty(MetaColumn.USER_NAME)
    @TableField(
        fill = FieldFill.INSERT,
        updateStrategy = FieldStrategy.NEVER
    )
    public var creatorName: String = ""

    /**
     * 创建时间
     */
    @OrderBy
    @TableField(
        updateStrategy = FieldStrategy.NEVER
    )
    public var createTime: LocalDateTime = LocalDateTime.now()

    /**
     * 流程实例ID
     */
    @TableField(
        updateStrategy = FieldStrategy.NEVER
    )
    public var instanceId: String = ""

    /**
     * 父任务ID
     */
    @TableField(
        updateStrategy = FieldStrategy.NEVER
    )
    public var parentTaskId: String = ""

    /**
     * 任务名称
     */
    @TableField(
        insertStrategy = FieldStrategy.NOT_EMPTY,
        updateStrategy = FieldStrategy.NOT_EMPTY
    )
    public var taskName: String = ""

    /**
     * 任务类型: 1.主办, 2.审批, 3.抄送, 4.条件审批, 5.条件分支, 6.子流程, 7.定时器, 8.触发器, 11.转办, 12.委派, 13.委派归还
     */
    public var taskType: TaskType? = null

    /**
     * 参与类型: 1.发起、其它, 2.按顺序依次审批, 3.会签, 4.或签, 5.票签, 10.抄送
     */
    public var performType: PerformType? = null

    /**
     * 变量json
     */
    @TableField(
        insertStrategy = FieldStrategy.NOT_EMPTY,
        updateStrategy = FieldStrategy.NOT_EMPTY
    )
    public var variable: String = ""

    /**
     * 委托人ID
     */
    public var assignorId: String = ""

    /**
     * 委托人
     */
    public var assignorName: String = ""

    /**
     * 任务期望完成时间
     */
    public var expireTime: LocalDateTime? = null

    /**
     * 提醒时间
     */
    public var remindTime: LocalDateTime? = null

    /**
     * 提醒次数
     */
    public var remindRepeat: Int? = null

    /**
     * 已阅: 0.否 1.是
     */
    public var viewed: Boolean? = null

    /**
     * 是否开始节点
     */
    public val atStartNode: Boolean
        get() = parentTaskId == ""
}
