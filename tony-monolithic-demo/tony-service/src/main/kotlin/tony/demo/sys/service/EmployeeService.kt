package tony.demo.sys.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tony.core.model.PageQueryLike
import tony.core.model.PageResult
import tony.core.model.PageResultLike
import tony.core.utils.alsoIfNotEmpty
import tony.core.utils.copyTo
import tony.core.utils.md5
import tony.core.utils.uuid
import tony.demo.sys.dao.DeptDao
import tony.demo.sys.dao.EmployeeDao
import tony.demo.sys.dao.RoleDao
import tony.demo.sys.dto.query.EmployeeQuery
import tony.demo.sys.dto.req.EmployeeAddReq
import tony.demo.sys.dto.req.EmployeeAssignRoleReq
import tony.demo.sys.dto.req.EmployeeDetailReq
import tony.demo.sys.dto.req.EmployeeResetPwdReq
import tony.demo.sys.dto.req.EmployeeToggleEnabledReq
import tony.demo.sys.dto.req.EmployeeUpdateReq
import tony.demo.sys.dto.resp.EmployeeResp
import tony.demo.sys.po.Employee
import tony.mybatis.utils.toPage
import tony.mybatis.utils.toPageResult

/**
 * 员工Service
 * @author tangli
 * @date 2024/07/04 14:43
 */
@Service
class EmployeeService(
    private val employeeDao: EmployeeDao,
    private val roleDao: RoleDao,
    private val deptDao: DeptDao,
) {
    /**
     * 新增
     * @param [req] 请求
     * @author tangli
     * @date 2024/07/09 09:08
     */
    @Transactional(rollbackFor = [Throwable::class])
    fun add(req: EmployeeAddReq) {
        employeeDao
            .ktQuery()
            .eq(Employee::employeeMobile, req.employeeMobile)
            .throwIfExists("手机号重复")

        employeeDao
            .ktQuery()
            .eq(Employee::account, req.account)
            .throwIfExists("账号重复")

        val po =
            req.copyTo<Employee>().apply {
                salt = uuid()
            }
        employeeDao.insert(po)
        val employeeId = po.employeeId
        val tenantId = req.tenantId
        resetPwd(po)
        deptDao.deleteEmployeeDepts(employeeId, tenantId)
        req.deptIds.alsoIfNotEmpty {
            deptDao.insertEmployeeDepts(employeeId, it, tenantId)
        }
    }

    /**
     * 更新
     * @param [req] 请求
     * @author tangli
     * @date 2024/07/09 09:08
     */
    @Transactional(rollbackFor = [Throwable::class])
    fun update(req: EmployeeUpdateReq) {
        employeeDao.selectByIdNotNull(req.employeeId)
        val po = req.copyTo<Employee>()
        employeeDao.updateById(po)
        val employeeId = po.employeeId
        val tenantId = req.tenantId
        deptDao.deleteEmployeeDepts(employeeId, tenantId)
        req.deptIds.alsoIfNotEmpty {
            deptDao.insertEmployeeDepts(employeeId, it, tenantId)
        }
    }

    /**
     * 详情
     * @param [req] 请求
     * @return [EmployeeResp]
     * @author tangli
     * @date 2024/07/09 10:41
     */
    fun detail(req: EmployeeDetailReq): EmployeeResp {
        val id = req.employeeId
        val po = employeeDao.selectByIdNotNull(id)
        val resp = po.copyTo<EmployeeResp>()

        val tenantId = req.tenantId
        val deptIds =
            deptDao
                .selectEmployeeDepts(id, tenantId)
                .map { it.deptId }
        val roleIds =
            roleDao
                .selectEmployeeRoles(id, tenantId)
                .map { it.roleId }
        resp.deptIds = deptIds
        resp.roleIds = roleIds
        return resp
    }

    /**
     * 列表
     * @param [req] 请求
     * @return [PageResultLike]<[EmployeeResp]>
     * @author tangli
     * @date 2024/07/04 14:43
     */
    fun pageList(req: PageQueryLike<EmployeeQuery>): PageResult<EmployeeResp> =
        employeeDao.selectEmployeeByQuery(req.query, req.toPage()).toPageResult()

    /**
     * 启用切换
     * @param [req] 请求
     * @author tangli
     * @date 2024/07/05 11:15
     */
    fun toggleEnabled(req: EmployeeToggleEnabledReq) {
        val employee =
            employeeDao
                .ktQuery()
                .eq(Employee::employeeId, req.employeeId)
                .oneNotNull("员工不存在")

        employeeDao.updateById(employee.apply { this.enabled = !req.enabled })
    }

    /**
     * 分配角色
     * @param [req] 请求
     * @author tangli
     * @date 2024/07/05 11:15
     */
    @Transactional(rollbackFor = [Throwable::class])
    fun assignRoles(req: EmployeeAssignRoleReq) {
        val employeeId = req.employeeId
        employeeDao
            .ktQuery()
            .eq(Employee::employeeId, employeeId)
            .oneNotNull()

        val tenantId = req.tenantId
        roleDao.deleteEmployeeRoles(employeeId, tenantId)
        req.roleIds.alsoIfNotEmpty {
            roleDao.insertEmployeeRoles(employeeId, it, tenantId)
        }
    }

    /**
     * 重置密码
     * @param [req] 绿色
     * @author tangli
     * @date 2024/07/09 09:33
     */
    @Transactional(rollbackFor = [Throwable::class])
    fun resetPwd(req: EmployeeResetPwdReq) {
        val po =
            employeeDao
                .ktQuery()
                .select(Employee::employeeId, Employee::salt)
                .eq(Employee::employeeId, req.employeeId)
                .oneNotNull()
        resetPwd(po)
    }

    private fun resetPwd(po: Employee) {
        po.pwd = "${"123456".md5().uppercase()}${po.salt}".md5().uppercase()
        po.accountState = tony.demo.sys.dto.enums.AccountState.NEED_CHANGE_PWD
        employeeDao.updateById(po)
    }
}
