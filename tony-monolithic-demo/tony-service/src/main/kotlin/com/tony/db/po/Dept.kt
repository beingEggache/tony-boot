package com.tony.db.po

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
 * 部门
 * @TableName sys_dept
 */
@TableName("sys_dept")
class Dept {
    /**
     *
     */
    @TableId
    var deptId: String = ""

    /**
     * 上级部门id
     */
    @TableField(
        updateStrategy = FieldStrategy.NOT_EMPTY
    )
    var parentDeptId: String = ""

    /**
     * 部门名
     */
    @TableField(
        updateStrategy = FieldStrategy.NOT_EMPTY
    )
    var deptName: String = ""

    /**
     * 部门编码
     */
    @TableField(
        updateStrategy = FieldStrategy.NEVER
    )
    var deptCode: String = ""

    /**
     * 部门编码序列
     */
    var deptCodeSeq: String = ""

    /**
     * 排序
     */
    var sort: Int = 0

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
