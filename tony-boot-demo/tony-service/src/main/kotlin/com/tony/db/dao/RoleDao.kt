package com.tony.db.dao

import com.tony.db.po.Role

interface RoleDao {
    fun deleteByPk(roleId: String?): Int
    fun insert(record: Role?): Int
    fun insertDynamic(record: Role?): Int
    fun selectByPk(roleId: String?): Role?
    fun updateByPkDynamic(record: Role?): Int
    fun updateByPk(record: Role?): Int
    fun selectAll(): List<Role?>?
}
