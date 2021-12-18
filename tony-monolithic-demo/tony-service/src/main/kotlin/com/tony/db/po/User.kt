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
 * @author tangli
 * @since 2020-11-15
 */
@TableName("t_sys_user")
class User {

    /**
     * 用户id
     */
    @TableId("user_id")
    var userId: String? = null

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
    var createTime: LocalDateTime? = null

    /**
     * 用户状态：1:启用，0:禁用。 可根据需求扩展
     */
    @TableField("states")
    var states: Int? = null

    /**
     * 备注
     */
    @TableField("remark")
    var remark: String? = null
}
