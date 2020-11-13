package com.tony.db.po

import java.time.LocalDateTime

/**
 * 用户
 *
 */
class User {
    /**
     * 用户id
     *
     */
    var userId: String? = null

    /**
     * 用户登录名
     *
     */
    var userName: String? = null

    /**
     * 用户真实姓名
     *
     */
    var realName: String? = null

    /**
     * 手机号
     *
     */
    var mobile: String? = null

    /**
     * 密码
     *
     */
    var pwd: String? = null

    /**
     * 创建时间
     *
     */
    var createTime: LocalDateTime? = null

    /**
     * 用户状态：1:启用，0:禁用。 可根据需求扩展
     *
     */
    var states: Int? = null

    /**
     * 备注
     *
     */
    var remark: String? = null
}
