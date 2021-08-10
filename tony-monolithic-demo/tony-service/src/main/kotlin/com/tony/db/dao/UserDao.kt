package com.tony.db.dao

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.tony.db.po.User
import org.springframework.stereotype.Repository

@Repository
interface UserDao : BaseMapper<User> {

    fun selectUserProjectIdList(userId: String?): List<String>

    fun delUserProjectByUserId(userId: String)
}
