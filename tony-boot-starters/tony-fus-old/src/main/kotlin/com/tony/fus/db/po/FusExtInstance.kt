package com.tony.fus.db.po

import com.baomidou.mybatisplus.annotation.FieldFill
import com.baomidou.mybatisplus.annotation.FieldStrategy
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.tony.fus.model.FusModel
import com.tony.mybatis.MetaColumn
import com.tony.mybatis.MybatisPlusMetaProperty

/**
 * FusExtInstance is
 * @author tangli
 * @date 2024/01/24 10:26
 * @since 1.0.0
 */
@TableName
public class FusExtInstance : FusModel {
    @TableId
    public var instanceId: String = ""

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
     * 流程定义ID
     */
    @TableField(
        updateStrategy = FieldStrategy.NEVER
    )
    public var processId: String = ""

    /**
     * 流程模型定义JSON内容
     */
    override var modelContent: String = "{}"
    override val modelKey: String
        get() = "FUS_PROCESS_INSTANCE_MODEL:$instanceId"
}
