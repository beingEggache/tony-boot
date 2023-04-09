package com.tony.db.dao

import com.tony.db.po.User
import com.tony.mybatis.dao.BaseDao
import org.springframework.stereotype.Repository

@Repository
interface UserDao : BaseDao<User>
