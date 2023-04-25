package com.tony.mybatis.db.dao

import com.tony.mybatis.dao.BaseDao
import com.tony.mybatis.db.po.User
import org.springframework.stereotype.Repository

@Repository
interface UserDao : BaseDao<User>
