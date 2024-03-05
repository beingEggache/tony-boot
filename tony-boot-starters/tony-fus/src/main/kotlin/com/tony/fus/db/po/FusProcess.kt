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
import com.tony.fus.model.FusModel
import com.tony.mybatis.MetaColumn
import com.tony.mybatis.MybatisPlusMetaProperty
import java.time.LocalDateTime

/**
 * 流程定义表
 * @author tangli
 * @date 2023/09/29 19:13
 * @since 1.0.0
 */
@TableName
public class FusProcess : FusModel {
    /**
     * 主键ID
     */
    @TableId
    public var processId: String = ""

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
    public val createTime: LocalDateTime = LocalDateTime.now()

    /**
     * 流程名称
     */
    public var processName: String = ""

    /**
     * 流程定义 key 唯一标识
     */
    public var processKey: String = ""

    /**
     * 流程类型
     */
    public var processType: String = ""

    /**
     * 流程版本，默认 1
     */
    public var processVersion: Int = 0

    /**
     * 流程状态: 0.不可用, 1.可用
     */
    public var enabled: Boolean = true

    /**
     * 流程模型定义JSON内容
     */
    override var modelContent: String = "{}"

    /**
     * 排序
     */
    public var sort: Int = 0

    override val modelKey: String
        get() = "FUS_PROCESS_MODEL:$processId"
}
