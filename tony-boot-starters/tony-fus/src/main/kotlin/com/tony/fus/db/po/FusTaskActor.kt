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
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.tony.fus.db.enums.ActorType
import com.tony.mybatis.MetaColumn
import com.tony.mybatis.MybatisPlusMetaProperty

/**
 * 任务参与者表
 * @author Tang Li
 * @date 2023/09/29 19:14
 * @since 1.0.0
 */
@TableName
public open class FusTaskActor {
    /**
     * 主键 ID
     */
    @TableId
    public var taskActorId: String = ""

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
     * 流程实例ID
     */
    @TableField(
        updateStrategy = FieldStrategy.NEVER
    )
    public var instanceId: String = ""

    /**
     * 任务ID
     */
    @TableField(
        updateStrategy = FieldStrategy.NEVER
    )
    public var taskId: String = ""

    /**
     * 参与者ID
     */
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
    public var actorType: ActorType = ActorType.USER

    /**
     * 票签权重
     */
    public var weight: Int = 0
}
