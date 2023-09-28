package com.tony.db.po

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
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
class User : Auditable {
    /**
     * 用户id
     */
    @TableId("user_id")
    var userId: Long? = null

    /**
     * 用户登录名
     */
    @TableField("user_name")
    var userName: String? = null

    /**
     * 用户真实姓名
     */
    @TableField("real_name")
    var realName: String? = null

    /**
     * 手机号
     */
    @TableField("mobile")
    var mobile: String? = null

    /**
     * 密码
     */
    @TableField("pwd")
    var pwd: String? = null

    /**
     * 创建时间
     */
    @TableField("create_time")
    override var createTime: LocalDateTime? = null

    /**
     * 创建人
     */
    @TableField("creator_id")
    override var creatorId: Long? = null

    /**
     * 创建人名称
     */
    @TableField("creator_name")
    override var creatorName: String? = null

    /**
     * 更新时间
     */
    @TableField("update_time")
    override var updateTime: LocalDateTime? = null

    /**
     * 更新人
     */
    @TableField("updator_id")
    override var updatorId: Long? = null

    /**
     * 更新人名称
     */
    @TableField("updator_name")
    override var updatorName: String? = null

    /**
     * 状态：1-启用，0-禁用
     */
    @TableField("enabled")
    var enabled: Boolean? = null

    /**
     * 备注
     */
    @TableField("remark")
    var remark: String? = null

    /**
     * 删除标记：1-已删除，0-未删除
     */
    @TableField("deleted")
    var deleted: Boolean? = null

    /**
     * 租户id
     */
    @TableField("tenant_id")
    var tenantId: Long? = null
}
