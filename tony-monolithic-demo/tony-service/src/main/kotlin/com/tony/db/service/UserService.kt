package com.tony.db.service

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.tony.core.exception.BizException
import com.tony.core.utils.toMd5UppercaseString
import com.tony.core.utils.uuid
import com.tony.db.dao.RoleDao
import com.tony.db.dao.UserDao
import com.tony.db.po.Role
import com.tony.db.po.User
import com.tony.dto.req.ModuleAssignReq
import com.tony.dto.req.RoleAssignReq
import com.tony.dto.req.UserCreateReq
import com.tony.dto.req.UserLoginReq
import com.tony.dto.req.UserUpdateReq
import com.tony.dto.resp.UserInfoResp
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Objects

/**
 *
 * @author tangli
 * @since 2020-11-03 14:35
 */
@Service
class UserService(
    private val userDao: UserDao,
    private val roleDao: RoleDao,
    private val roleService: RoleService,
    private val moduleService: ModuleService
) : ServiceImpl<UserDao, User>() {

    fun login(req: UserLoginReq) =
        userDao.selectOne(
            QueryWrapper<User>()
                .eq(User.USER_NAME, req.userName)
                .eq(User.PWD, "${req.pwd}${req.userName}".toMd5UppercaseString())
        ) ?: throw BizException("用户名或密码错误")

    fun info(userId: String, appId: String) = (userDao.selectById(userId)
            ?: throw BizException("没有此用户")).run {
            UserInfoResp(
                realName,
                mobile,
                moduleService.listRouteAndComponentModules(userId, appId)
            )
        }

    fun list(query: String?, page: Long = 1, size: Long = 10) =
        userDao.selectPage(
            Page(page, size),
            QueryWrapper<User>().like(
                !query.isNullOrBlank(),
                User.USER_NAME, query
            ).or(!query.isNullOrBlank()) {
                it.like(User.REAL_NAME, query)
            }
        ).toPageResult().map {
            it.toDto()
        }

    fun listRolesByUserId(userId: String?, appId: String) =
        roleDao.selectByUserId(userId, appId).map { it.toDto() }

    @Transactional
    fun add(req: UserCreateReq): Boolean {
        if (!Objects.equals(req.pwd, req.confirmPwd)) {
            throw BizException("两次密码不相等")
        }

        val exists = userDao.selectOne(
            QueryWrapper<User>()
                .eq(User.USER_NAME, req.userName)
                .or().eq(User.MOBILE, req.mobile)
        )
        if (exists != null) {
            throw BizException("用户名或手机号已重复")
        }

        val userId = uuid()
        val insertUser = userDao.insert(
            User().apply {
                this.userId = userId
                userName = req.userName
                realName = req.realName
                mobile = req.mobile
                pwd = "${req.pwd}${req.userName}".toMd5UppercaseString()
            }
        )

        return insertUser > 0
    }

    @Transactional
    fun update(req: UserUpdateReq): Boolean {

        val userId = req.userId ?: throw BizException("userId null")

        if (userDao.selectById(userId) == null) {
            throw BizException("没有此用户")
        }

        val exists = userDao.selectOne(
            QueryWrapper<User>()
                .eq(User.USER_NAME, req.userName)
                .or().eq(User.MOBILE, req.mobile)
        )
        if (exists != null && !Objects.equals(exists.userId, userId)) {
            throw BizException("用户名或手机号已重复")
        }

        userDao.delUserProjectByUserId(userId)

        return userDao.updateById(
            User().apply {
                this.userId = userId
                userName = req.userName
                realName = req.realName
                mobile = req.mobile
            }
        ) > 0
    }

    @Transactional
    fun initSuperAdmin(appId: String) {

        val superAdmin = "SUPER_ADMIN"
        val user = User().apply {
            this.userId = superAdmin
            userName = "admin"
            realName = "超级管理员"
            mobile = "13984842424"
            pwd = "lxkj123!@#admin".toMd5UppercaseString()
        }
        userDao.deleteById(superAdmin)
        userDao.insert(user)
        roleDao.deleteById(superAdmin)
        roleDao.insert(
            Role().apply {
                roleId = superAdmin
                this.appId = appId
                roleName = "超级管理员"
            }
        )

        userDao.delUserProjectByUserId(superAdmin)

        roleService.assignRole(RoleAssignReq(listOf(superAdmin), listOf(superAdmin)))
        val moduleGroups = moduleService.listModuleGroups(appId)
        roleService.assignModule(ModuleAssignReq(moduleGroups, listOf(superAdmin)))
    }
}
