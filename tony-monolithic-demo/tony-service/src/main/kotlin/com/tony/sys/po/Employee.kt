package com.tony.sys.po

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
 * 员工
 *
 * @author tangli
 * @date 2020-11-15
 */
@TableName("sys_employee")
class Employee {
    /**
     * 员工id
     */
    @TableId
    var employeeId: String = ""

    /**
     * 员工登录名
     */
    @TableField(
        updateStrategy = FieldStrategy.NOT_EMPTY
    )
    var account: String = ""

    /**
     * 员工真实姓名
     */
    @TableField(
        updateStrategy = FieldStrategy.NOT_EMPTY
    )
    var realName: String = ""

    /**
     * 员工手机号
     */
    @TableField(
        updateStrategy = FieldStrategy.NOT_EMPTY
    )
    var employeeMobile: String = ""

    /**
     * 密码
     */
    @TableField(
        updateStrategy = FieldStrategy.NOT_EMPTY
    )
    var pwd: String = ""

    /**
     * 盐
     */
    @TableField(
        updateStrategy = FieldStrategy.NEVER
    )
    var salt: String = ""

    /**
     * 帐户状态
     */
    var accountState: com.tony.sys.dto.enums.AccountState? = null

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
}
