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

package com.tony.test.mybatis.db.po

import com.baomidou.mybatisplus.annotation.FieldFill
import com.baomidou.mybatisplus.annotation.FieldStrategy
import com.baomidou.mybatisplus.annotation.OrderBy
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableLogic
import com.baomidou.mybatisplus.annotation.TableName
import com.tony.mybatis.MetaColumn
import com.tony.mybatis.MybatisPlusMetaProperty
import java.time.LocalDateTime

/**
 * 角色
 * @TableName sys_role
 */
@TableName("sys_role")
class Role {
    /**
     *
     */
    @TableId
    var roleId: String = ""

    /**
     * 角色名
     */
    @TableField(
        updateStrategy = FieldStrategy.NOT_EMPTY
    )
    var roleName: String = ""

    /**
     * 角色编码
     */
    @TableField(
        updateStrategy = FieldStrategy.NOT_EMPTY
    )
    var roleCode: String = ""

    /**
     * 系统内建,不可删除
     */
    @TableField(
        insertStrategy = FieldStrategy.NEVER,
        updateStrategy = FieldStrategy.NEVER
    )
    var buildIn: Boolean? = null

    /**
     * 状态：1-启用，0-禁用
     */
    var enabled: Boolean? = null

    /**
     * 备注
     */
    @TableField(
        updateStrategy = FieldStrategy.NOT_EMPTY
    )
    var remark: String = ""

    /**
     * 创建时间
     */
    @OrderBy
    @TableField(
        insertStrategy = FieldStrategy.NEVER,
        updateStrategy = FieldStrategy.NEVER
    )
    var createTime: LocalDateTime = LocalDateTime.now()

    /**
     * 创建人
     */
    @MybatisPlusMetaProperty(MetaColumn.USER_ID)
    @TableField(
        fill = FieldFill.INSERT,
        updateStrategy = FieldStrategy.NEVER
    )
    var creatorId: String = ""

    /**
     * 创建人名称
     */
    @MybatisPlusMetaProperty(MetaColumn.USER_NAME, relativeProp = "creatorId")
    @TableField(
        fill = FieldFill.INSERT,
        updateStrategy = FieldStrategy.NEVER
    )
    var creatorName: String = ""

    /**
     * 更新时间
     */
    @TableField(
        insertStrategy = FieldStrategy.NEVER,
        updateStrategy = FieldStrategy.NEVER,
        update = "CURRENT_TIMESTAMP"
    )
    var updateTime: LocalDateTime = LocalDateTime.now()

    /**
     * 更新人
     */
    @TableField(
        fill = FieldFill.UPDATE,
        insertStrategy = FieldStrategy.NEVER,
        updateStrategy = FieldStrategy.NEVER
    )
    var updatorId: String = ""

    /**
     * 更新人名称
     */
    @TableField(
        fill = FieldFill.UPDATE,
        insertStrategy = FieldStrategy.NEVER,
        updateStrategy = FieldStrategy.NEVER
    )
    var updatorName: String = ""

    /**
     * 删除时间
     */
    @TableLogic
    var deleteTime: LocalDateTime? = null

    /**
     * 租户id
     */
    @TableField(
        fill = FieldFill.INSERT,
        updateStrategy = FieldStrategy.NEVER
    )
    var tenantId: String = ""
}
