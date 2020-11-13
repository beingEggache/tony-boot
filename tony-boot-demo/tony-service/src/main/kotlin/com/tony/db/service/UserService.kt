package com.tony.db.service

import com.tony.core.exception.BizException
import com.tony.core.utils.toMd5UppercaseString
import com.tony.core.utils.uuid
import com.tony.db.dao.UserDao
import com.tony.db.po.User
import com.tony.pojo.req.UserCreateReq
import com.tony.pojo.req.UserLoginReq
import com.tony.pojo.resp.UserInfoResp
import java.util.Objects
import javax.validation.Valid
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated

/**
 *
 * @author tangli
 * @since 2020-11-03 14:35
 */
@Validated
@Service
class UserService(
    private val userDao: UserDao,
    private val moduleService: ModuleService
) {

    fun login(req: UserLoginReq) =
        userDao.getByUserNameAndPwd(req.userName, "${req.pwd}${req.userName}".toMd5UppercaseString())
            ?: throw BizException("用户名或密码错误")

    fun info(userId: String, appId: String): UserInfoResp {
        val user = userDao.selectByPk(userId)
            ?: throw BizException("没有此用户")
        return UserInfoResp(
            user.realName, user.mobile,
            moduleService.listRouteAndComponentModules(user.userId ?: "", appId))
    }

    @Transactional
    fun create(@Valid req: UserCreateReq): Int {
        if (!Objects.equals(req.pwd, req.confirmPwd)) {
            throw BizException("两次密码不相等")
        }

        if (userDao.getByNameOrPhone(req.userName, req.mobile) != null) {
            throw BizException("用户名或手机号已重复")
        }

        return userDao.insertDynamic(User().apply {
            userId = uuid()
            userName = req.userName
            realName = req.realName
            mobile = req.mobile
            pwd = "${req.pwd}${req.userName}".toMd5UppercaseString()
        })
    }
}

