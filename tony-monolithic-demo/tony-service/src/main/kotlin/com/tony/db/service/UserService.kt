package com.tony.db.service

import com.tony.JPageQuery
import com.tony.PageResult
import com.tony.db.dao.RoleDao
import com.tony.db.dao.UserDao
import com.tony.db.po.Role
import com.tony.db.po.User
import com.tony.digest.md5
import com.tony.dto.req.UserCreateReq
import com.tony.dto.req.UserLoginReq
import com.tony.dto.req.UserUpdateReq
import com.tony.dto.resp.RoleResp
import com.tony.dto.resp.UserInfoResp
import com.tony.dto.resp.UserResp
import com.tony.exception.BizException
import com.tony.utils.copyTo
import com.tony.utils.throwIf
import com.tony.utils.throwIfNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 *
 * @author Tang Li
 * @date 2020-11-03 14:35
 */
@Service
class UserService(
    private val userDao: UserDao,
    private val roleDao: RoleDao,
    private val moduleService: ModuleService,
) {

    fun login(req: UserLoginReq) =
        userDao
            .ktQuery()
            .eq(User::userName, req.userName)
            .eq(User::pwd, "${req.pwd}${req.userName}".md5().uppercase())
            .oneNotNull("用户名或密码错误")

    fun info(userId: String, appId: String) =
        userDao.selectById(userId)?.run {
            UserInfoResp(
                realName,
                mobile,
                moduleService.listRouteAndComponentModules(userId, appId)
            )
        } ?: throw BizException("没有此用户")

    fun list(
        req: JPageQuery<String>,
    ): PageResult<UserResp> = userDao
        .ktQuery()
        .like(
            !req.query.isNullOrBlank(),
            User::userName,
            req.query
        )
        .or(!req.query.isNullOrBlank()) {
            it.like(User::realName, req.query)
        }
        .pageResult<PageResult<User>>(req)
        .map { it.copyTo<UserResp>() }

    fun listRolesByUserId(userId: String?, appId: String) =
        roleDao
            .selectByUserId(userId, appId)
            .map { it.copyTo<RoleResp>() }

    @Transactional
    fun add(req: UserCreateReq): Boolean {
        throwIf(req.pwd != req.confirmPwd, "两次密码不相等")
        userDao
            .ktQuery()
            .eq(User::userName, req.userName)
            .or()
            .eq(User::mobile, req.mobile)
            .throwIfExists("用户名或手机号已重复")

        return userDao.insert(
            User().apply {
                userName = req.userName
                realName = req.realName
                mobile = req.mobile
                pwd = "${req.pwd}${req.userName}".md5().uppercase()
            }
        ) > 0
    }

    @Transactional
    fun update(req: UserUpdateReq): Boolean {
        val userId = checkNotNull(req.userId)
        userDao.selectById(userId).throwIfNull("没有此用户")
        userDao
            .ktQuery()
            .eq(User::userName, req.userName)
            .or()
            .eq(User::mobile, req.mobile)
            .ne(User::userId, userId)
            .throwIfExists("用户名或手机号已重复")

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
        val superAdmin = 1L
        val user = User().apply {
            this.userId = superAdmin
            userName = "gateway"
            realName = "超级管理员"
            mobile = "13984842424"
            pwd = "lxkj123!@#gateway".md5().uppercase()
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

        // TODO
        // roleDao.assignRole(RoleAssignReq(listOf(superAdmin), listOf(superAdmin)))
        // val moduleGroups = moduleService.listModuleGroups(appId)
        // roleDao.assignModule(ModuleAssignReq(moduleGroups, listOf(superAdmin)))
    }
}
