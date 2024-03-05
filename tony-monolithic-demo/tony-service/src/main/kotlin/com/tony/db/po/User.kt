package com.tony.db.po

import com.baomidou.mybatisplus.annotation.FieldFill
import com.baomidou.mybatisplus.annotation.FieldStrategy
import com.baomidou.mybatisplus.annotation.OrderBy
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableLogic
import com.baomidou.mybatisplus.annotation.TableName
import java.time.LocalDateTime

/**
 * <p>
 * 用户
 * </p>
 *
 * @author tangli
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
    @TableField(
        fill = FieldFill.INSERT,
        insertStrategy = FieldStrategy.NEVER,
        updateStrategy = FieldStrategy.NEVER
    )
    var creatorId: String = ""

    /**
     * 创建人名称
     */
    @TableField(
        fill = FieldFill.INSERT,
        insertStrategy = FieldStrategy.NEVER,
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
    @TableField(
        fill = FieldFill.INSERT,
        updateStrategy = FieldStrategy.NEVER
    )
    var tenantId: String = ""
}
