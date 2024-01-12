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
import com.tony.fus.db.enums.ActorType
import com.tony.fus.db.enums.TaskState
import com.tony.mybatis.MetaColumn
import com.tony.mybatis.MybatisPlusMetaProperty
import java.time.LocalDateTime

/**
 * 抄送任务表
 * @author Tang Li
 * @date 2023/09/29 19:14
 * @since 1.0.0
 */
@TableName
public class FusTaskCc {
    /**
     * 主键ID
     */
    @TableId
    public var taskCcId: String = ""

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
        insertStrategy = FieldStrategy.NEVER,
        updateStrategy = FieldStrategy.NEVER
    )
    public val createTime: LocalDateTime? = null

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
        updateStrategy = FieldStrategy.NEVER
    )
    public var taskName: String = ""

    /**
     * 参与者ID
     */
    @TableField(
        updateStrategy = FieldStrategy.NEVER
    )
    public var actorId: String = ""

    /**
     * 参与者名称
     */
    @TableField(
        updateStrategy = FieldStrategy.NEVER
    )
    public var actorName: String = ""

    /**
     * 参与者类型: 1.用户, 2.角色, 3.部门
     */
    @TableField(
        updateStrategy = FieldStrategy.NEVER
    )
    public var actorType: ActorType? = null

    /**
     * 任务状态: 1.结束 2.活动
     */
    public var taskState: TaskState? = null

    /**
     * 完成时间
     */
    public var finishTime: LocalDateTime? = null
}
