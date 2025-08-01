package tony.demo.sys.service

import org.springframework.stereotype.Service
import tony.core.ApiProperty
import tony.core.utils.md5
import tony.core.utils.notEquals
import tony.core.utils.throwIfFalse
import tony.core.utils.throwIfTrue
import tony.demo.sys.dao.EmployeeDao
import tony.demo.sys.dao.ModuleDao
import tony.demo.sys.dao.RoleDao
import tony.demo.sys.dto.enums.ModuleType
import tony.demo.sys.dto.req.ChangePwdReq
import tony.demo.sys.dto.req.LoginReq
import tony.demo.sys.dto.resp.InfoResp
import tony.demo.sys.po.Employee

/**
 * 首页Service
 * @author tangli
 * @date 2024/07/02 10:40
 */
@Service
class IndexService(
    private val employeeDao: EmployeeDao,
    private val roleDao: RoleDao,
    private val moduleDao: ModuleDao,
) {
    /**
     * 登录
     * @param [req] 请求
     * @return [String] id
     * @author tangli
     * @date 2024/07/04 14:37
     */
    fun login(req: LoginReq): String {
        val salt =
            employeeDao
                .ktQuery()
                .select(Employee::salt)
                .eq(Employee::account, req.userName)
                .oneObjNotNull<String>("用户名或密码错误")

        val employee =
            employeeDao
                .ktQuery()
                .eq(Employee::account, req.userName)
                .eq(Employee::pwd, "${req.pwd}$salt".md5().uppercase())
                .oneNotNull("用户名或密码错误")
        employee.enabled.throwIfFalse("用户已禁用")
        return employee.employeeId
    }

    /**
     * 用户信息
     * @param [employeeId] 用户id
     * @param [appId] 应用id
     * @return [InfoResp]
     * @author tangli
     * @date 2024/07/04 14:38
     */
    fun info(
        employeeId: String,
        appId: String,
        tenantId: String,
    ): InfoResp {
        val employee = employeeDao.selectByIdNotNull(employeeId, "没有此用户")
        employee.enabled.throwIfFalse("用户已被停用", ApiProperty.unauthorizedCode)
        val infoResp =
            employee.let {
                InfoResp(
                    it.employeeId,
                    it.account,
                    it.realName,
                    it.employeeMobile
                )
            }
        val employeeHasAdminRole = roleDao.selectEmployeeHasBuildInRole(employeeId, "ADMIN")
        val modules =
            if (employeeHasAdminRole) {
                moduleDao.selectByAppId(appId)
            } else {
                employeeDao.selectEmployeeModulesByEmployeeIdAndAppId(
                    employeeId,
                    appId,
                    tenantId,
                    listOf(ModuleType.ROUTE, ModuleType.NODE, ModuleType.COMPONENT)
                )
            }

        val route =
            modules
                .filter { it.moduleType == ModuleType.ROUTE || it.moduleType == ModuleType.NODE }
                .map { it.moduleValue }
        val components =
            modules
                .filter { it.moduleType == ModuleType.COMPONENT }
                .map { it.moduleValue }
        infoResp.routePaths = route
        infoResp.componentCodes = components
        return infoResp
    }

    /**
     * 修改密码
     * @param [req] 请求
     * @author tangli
     * @date 2024/07/10 13:47
     */
    fun changePwd(req: ChangePwdReq) {
        val newPwd = req.newPwd
        newPwd.notEquals(req.confirmPwd).throwIfTrue("两次密码不一致")
        val po =
            employeeDao
                .ktQuery()
                .eq(Employee::employeeId, req.employeeId)
                .oneNotNull()
        employeeDao
            .ktQuery()
            .eq(Employee::employeeId, req.employeeId)
            .eq(Employee::pwd, "${req.pwd}${po.salt}".md5().uppercase())
            .oneNotNull("密码错误")
        val updatedPwd = "$newPwd${po.salt}".md5().uppercase()
        po.pwd = updatedPwd
        employeeDao.updateById(po)
    }
}
