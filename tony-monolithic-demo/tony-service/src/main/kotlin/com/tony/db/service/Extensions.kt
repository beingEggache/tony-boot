package com.tony.db.service

import com.tony.db.po.Role
import com.tony.db.po.User
import com.tony.dto.resp.RoleResp
import com.tony.dto.resp.UserResp

/**
 *
 * @author tangli
 * @since 2020-12-10 13:39
 */
fun User.toDto() = UserResp(
    userId,
    userName,
    realName,
    mobile,
    createTime,
    remark,
    states
)

fun Role.toDto() = RoleResp(roleId, roleName, remark)
