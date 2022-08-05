package com.tony.db.service

import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.tony.db.dao.RoleDao
import com.tony.db.dao.UserDao
import com.tony.db.po.Role
import com.tony.db.po.User
import com.tony.dto.req.UserCreateReq
import com.tony.dto.req.UserLoginReq
import com.tony.dto.req.UserUpdateReq
import com.tony.dto.resp.UserInfoResp
import com.tony.exception.BizException
import com.tony.utils.throwIf
import com.tony.utils.throwIfNull
import com.tony.utils.toMd5UppercaseString
import com.tony.utils.uuid
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 *
 * @author tangli
 * @since 2020-11-03 14:35
 */
@Service
class UserService(
    private val userDao: UserDao,
    private val roleDao: RoleDao,
    private val moduleService: ModuleService
) {

    fun login(req: UserLoginReq) =
        userDao.selectOne(
            where<User>()
                .eq(User::userName, req.userName)
                .eq(User::pwd, "${req.pwd}${req.userName}".toMd5UppercaseString())
        ) ?: throw BizException("用户名或密码错误")

    fun info(userId: String, appId: String) =
        userDao.selectById(userId)?.run {
            UserInfoResp(
                realName,
                mobile,
                moduleService.listRouteAndComponentModules(userId, appId)
            )
        } ?: throw BizException("没有此用户")

    fun list(query: String?, page: Long = 1, size: Long = 10) =
        userDao.selectPage(
            Page(page, size),
            where<User>().like(
                !query.isNullOrBlank(),
                User::userName,
                query
            ).or(!query.isNullOrBlank()) {
                it.like(User::realName, query)
            }
        ).toPageResult().map {
            it.toDto()
        }

    fun listRolesByUserId(userId: String?, appId: String) =
        roleDao.selectByUserId(userId, appId).map { it.toDto() }

    @Transactional
    fun add(req: UserCreateReq): Boolean {
        throwIf(req.pwd != req.confirmPwd, "两次密码不相等")

        throwIf(
            userDao.selectOne(
                where<User>()
                    .eq(User::userName, req.userName)
                    .or().eq(User::mobile, req.mobile)
            ) != null,
            "用户名或手机号已重复"
        )

        return userDao.insert(
            User().apply {
                this.userId = uuid()
                userName = req.userName
                realName = req.realName
                mobile = req.mobile
                pwd = "${req.pwd}"
            }
        ) > 0
    }

    @Transactional
    fun update(req: UserUpdateReq): Boolean {
        val userId = checkNotNull(req.userId)
        userDao.selectById(userId).throwIfNull("没有此用户")

        throwIf(
            userDao.selectOne(
                where<User>()
                    .eq(User::userName, req.userName)
                    .or().eq(User::mobile, req.mobile)
            )?.userId != userId,
            "用户名或手机号已重复"
        )

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
            userName = "gateway"
            realName = "超级管理员"
            mobile = "13984842424"
            pwd = "lxkj123!@#gateway".toMd5UppercaseString()
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

        // TODO
        // roleDao.assignRole(RoleAssignReq(listOf(superAdmin), listOf(superAdmin)))
        // val moduleGroups = moduleService.listModuleGroups(appId)
        // roleDao.assignModule(ModuleAssignReq(moduleGroups, listOf(superAdmin)))
    }
}
