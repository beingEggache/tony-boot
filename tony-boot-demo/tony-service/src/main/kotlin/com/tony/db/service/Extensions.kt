package com.tony.db.service

import com.baomidou.mybatisplus.core.metadata.IPage
import com.tony.dto.resp.RoleResp
import com.tony.core.PageResult
import com.tony.db.po.Role
import com.tony.db.po.User
import com.tony.dto.resp.UserResp

/**
 *
 * @author tangli
 * @since 2020-12-10 13:39
 */
fun <T> IPage<T>.toPageResult() = PageResult(
    records,
    current,
    size,
    pages,
    total,
    pages > current
)

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
