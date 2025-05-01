package tony.demo.sys.po

import com.baomidou.mybatisplus.annotation.FieldFill
import com.baomidou.mybatisplus.annotation.FieldStrategy
import com.baomidou.mybatisplus.annotation.OrderBy
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableLogic
import com.baomidou.mybatisplus.annotation.TableName
import java.time.LocalDateTime
import tony.mybatis.MetaColumn
import tony.mybatis.MybatisPlusMetaProperty

/**
 * 字典类型
 * @TableName sys_dict_type
 */
@TableName("sys_dict_type")
class DictType {
    /**
     *
     */
    @TableId
    var dictTypeId: String = ""

    /**
     * 上级id
     */
    @TableField(
        updateStrategy = FieldStrategy.NOT_EMPTY
    )
    var parentDictTypeId: String = ""

    /**
     * 字典类型编码
     */
    @TableField(
        updateStrategy = FieldStrategy.NEVER
    )
    var dictTypeCode: String = ""

    /**
     * 字典类型编码序列
     */
    var dictTypeCodeSeq: String = ""

    /**
     * 应用id
     */
    @TableField(
        updateStrategy = FieldStrategy.NOT_EMPTY
    )
    var appId: String = ""

    /**
     * 字典类型名
     */
    @TableField(
        updateStrategy = FieldStrategy.NOT_EMPTY
    )
    var dictTypeName: String = ""

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
