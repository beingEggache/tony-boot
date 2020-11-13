package com.tony.db.dao

import com.tony.db.po.User
import org.apache.ibatis.annotations.Param
import org.springframework.stereotype.Repository

@Repository
interface UserDao {
    fun deleteByPk(userId: String?): Int
    fun insert(record: User?): Int
    fun insertDynamic(record: User?): Int
    fun selectByPk(userId: String?): User?
    fun updateByPkDynamic(record: User?): Int
    fun updateByPk(record: User?): Int
    fun selectAll(): List<User?>?

    fun getByNameOrPhone(@Param("userName") userName: String?, @Param("mobile") mobile: String?): User?

    fun getByUserNameAndPwd(@Param("userName") userName: String?, @Param("pwd") pwd: String?): User?
}
