package tony.demo.sys.dao

import org.apache.ibatis.annotations.Param
import tony.demo.sys.dto.resp.DeptResp
import tony.demo.sys.po.Dept
import tony.mybatis.dao.BaseDao

/**
 * 部门 dao
 * @author tangli
 * @date 2024/07/05 11:09
 * @since 1.0.0
 */
interface DeptDao : BaseDao<Dept> {
    /**
     * 插入用户部门
     * @param [employeeId] 用户id
     * @param [deptIds] 部门id列表
     * @param [tenantId] 租户id
     * @return [Int]
     * @author tangli
     * @date 2024/07/05 11:09
     * @since 1.0.0
     */
    fun insertEmployeeDepts(
        @Param("employeeId")
        employeeId: String,
        @Param("deptIds")
        deptIds: Collection<String>,
        @Param("tenantId")
        tenantId: String,
    ): Int

    /**
     * 删除用户部门
     * @param [employeeId] 用户id
     * @param [tenantId] 租户id
     * @return [Int]
     * @author tangli
     * @date 2024/07/05 11:10
     * @since 1.0.0
     */
    fun deleteEmployeeDepts(
        @Param("employeeId")
        employeeId: String,
        @Param("tenantId")
        tenantId: String,
    ): Int

    /**
     * 删除用户部门
     * @param [deptId] 用户id
     * @param [tenantId] 租户id
     * @return [Int]
     * @author tangli
     * @date 2024/07/05 11:10
     * @since 1.0.0
     */
    fun deleteEmployeesDept(
        @Param("deptId")
        deptId: String,
        @Param("tenantId")
        tenantId: String,
    ): Int

    /**
     * 查询用户部门
     * @param [employeeId] 用户id
     * @return [List]<[DeptResp]>
     * @author tangli
     * @date 2024/07/09 10:35
     * @since 1.0.0
     */
    fun selectEmployeeDepts(
        @Param("employeeId")
        employeeId: String,
        @Param("tenantId")
        tenantId: String,
    ): List<DeptResp>
}
