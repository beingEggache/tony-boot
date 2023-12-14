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
 * <p>
 * 用户
 * </p>
 *
 * @author Tang Li
 * @date 2020-11-15
 */
@TableName("sys_user")
class User {
    /**
     * 用户id
     */
    @TableId
    var userId: String = ""

    /**
     * 用户登录名
     */
    var userName: String = ""

    /**
     * 用户真实姓名
     */
    var realName: String = ""

    /**
     * 手机号
     */
    var mobile: String = ""

    /**
     * 密码
     */
    var pwd: String = ""

    /**
     * 备注
     */
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
        insertStrategy = FieldStrategy.NOT_EMPTY,
        updateStrategy = FieldStrategy.NEVER
    )
    var creatorId: String = ""

    /**
     * 创建人名称
     */
    @MybatisPlusMetaProperty(MetaColumn.USER_NAME)
    @TableField(
        fill = FieldFill.INSERT,
        insertStrategy = FieldStrategy.NOT_EMPTY,
        updateStrategy = FieldStrategy.NEVER
    )
    var creatorName: String = ""

    /**
     * 更新时间
     */
    @TableField(
        insertStrategy = FieldStrategy.NEVER,
        updateStrategy = FieldStrategy.NEVER
    )
    var updateTime: LocalDateTime = LocalDateTime.now()

    /**
     * 更新人
     */
    @MybatisPlusMetaProperty(MetaColumn.USER_ID)
    @TableField(
        fill = FieldFill.UPDATE,
        insertStrategy = FieldStrategy.NEVER,
        updateStrategy = FieldStrategy.NOT_EMPTY
    )
    var updatorId: String = ""

    /**
     * 更新人名称
     */
    @MybatisPlusMetaProperty(MetaColumn.USER_NAME)
    @TableField(
        fill = FieldFill.UPDATE,
        insertStrategy = FieldStrategy.NEVER,
        updateStrategy = FieldStrategy.NOT_EMPTY
    )
    var updatorName: String = ""

    /**
     * 状态：1-启用，0-禁用
     */
    var enabled: Boolean? = null

    /**
     * 删除标记：1-已删除，0-未删除
     */
    @TableLogic
    var deleted: Boolean? = null

    /**
     * 租户id
     */
    @MybatisPlusMetaProperty(MetaColumn.TENANT_ID)
    @TableField(
        fill = FieldFill.INSERT,
        insertStrategy = FieldStrategy.NOT_EMPTY,
        updateStrategy = FieldStrategy.NEVER
    )
    var tenantId: String = ""
}
