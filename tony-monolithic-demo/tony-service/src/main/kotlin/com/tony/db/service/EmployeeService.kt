package com.tony.db.service

import com.tony.PageQueryLike
import com.tony.PageResult
import com.tony.PageResultLike
import com.tony.db.dao.DeptDao
import com.tony.db.dao.EmployeeDao
import com.tony.db.dao.RoleDao
import com.tony.db.po.Employee
import com.tony.dto.enums.AccountState
import com.tony.dto.req.EmployeeAddReq
import com.tony.dto.req.EmployeeAssignRoleReq
import com.tony.dto.req.EmployeeDetailReq
import com.tony.dto.req.EmployeeQuery
import com.tony.dto.req.EmployeeResetPwdReq
import com.tony.dto.req.EmployeeToggleEnabledReq
import com.tony.dto.req.EmployeeUpdateReq
import com.tony.dto.resp.EmployeeResp
import com.tony.utils.copyTo
import com.tony.utils.md5
import com.tony.utils.toPage
import com.tony.utils.toPageResult
import com.tony.utils.uuid
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 员工Service
 * @author tangli
 * @date 2024/07/04 14:43
 * @since 1.0.0
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
     * @since 1.0.0
     */
    @Transactional(rollbackFor = [Throwable::class])
    fun add(req: EmployeeAddReq) {
        employeeDao
            .ktQuery()
            .eq(Employee::employeeMobile, req.employeeMobile)
            .throwIfExists("手机号重复")

        val po = req.copyTo<Employee>()
        employeeDao.insert(po)
        val employeeId = po.employeeId
        val tenantId = req.tenantId
        po.salt = uuid()
        resetPwd(po)
        deptDao.deleteEmployeeDepts(employeeId, tenantId)
        req.deptIds.takeIf { it.isNotEmpty() }?.let {
            deptDao.insertEmployeeDepts(employeeId, it, tenantId)
        }
    }

    /**
     * 更新
     * @param [req] 请求
     * @author tangli
     * @date 2024/07/09 09:08
     * @since 1.0.0
     */
    @Transactional(rollbackFor = [Throwable::class])
    fun update(req: EmployeeUpdateReq) {
        employeeDao.selectByIdNotNull(req.employeeId)
        val po = req.copyTo<Employee>()
        employeeDao.updateById(po)
        val employeeId = po.employeeId
        val tenantId = req.tenantId
        deptDao.deleteEmployeeDepts(employeeId, tenantId)
        req.deptIds.takeIf { it.isNotEmpty() }?.let {
            deptDao.insertEmployeeDepts(employeeId, it, tenantId)
        }
    }

    /**
     * 详情
     * @param [req] 请求
     * @return [EmployeeResp]
     * @author tangli
     * @date 2024/07/09 10:41
     * @since 1.0.0
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
     * @since 1.0.0
     */
    fun list(req: PageQueryLike<EmployeeQuery>): PageResult<EmployeeResp> =
        employeeDao.selectEmployeeByQuery(req.query, req.toPage()).toPageResult()

    /**
     * 启用切换
     * @param [req] 请求
     * @author tangli
     * @date 2024/07/05 11:15
     * @since 1.0.0
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
     * @since 1.0.0
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
        req.roleIds.takeIf { it.isNotEmpty() }?.let {
            roleDao.insertEmployeeRoles(employeeId, it, tenantId)
        }
    }

    /**
     * 重置密码
     * @param [req] 绿色
     * @author tangli
     * @date 2024/07/09 09:33
     * @since 1.0.0
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
        po.accountState = AccountState.NEED_CHANGE_PWD
        employeeDao.updateById(po)
    }
}
