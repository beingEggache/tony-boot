package com.tony.demo.sys.po

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
 * 字典
 * @TableName sys_dict
 */
@TableName("sys_dict")
class Dict {
    /**
     *
     */
    @TableId
    var dictId: String = ""

    /**
     * 字典类型id
     */
    @TableField(
        updateStrategy = FieldStrategy.NOT_EMPTY
    )
    var dictTypeId: String = ""

    /**
     * 字典标签
     */
    @TableField(
        updateStrategy = FieldStrategy.NOT_EMPTY
    )
    var dictName: String = ""

    /**
     * 字典值
     */
    @TableField(
        updateStrategy = FieldStrategy.NOT_EMPTY
    )
    var dictValue: String = ""

    /**
     * 字典编码
     */
    @TableField(
        updateStrategy = FieldStrategy.NOT_EMPTY
    )
    var dictCode: String = ""

    /**
     * 字典meta
     */
    @TableField(
        updateStrategy = FieldStrategy.NOT_EMPTY
    )
    var dictMeta: String = "{}"

    /**
     * 系统内建,不可删除
     */
    @TableField(
        insertStrategy = FieldStrategy.NEVER,
        updateStrategy = FieldStrategy.NEVER
    )
    var buildIn: Boolean? = null

    /**
     * 排序
     */
    @OrderBy
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
     * 删除时间
     */
    @TableLogic
    var deleteTime: LocalDateTime? = null
}
