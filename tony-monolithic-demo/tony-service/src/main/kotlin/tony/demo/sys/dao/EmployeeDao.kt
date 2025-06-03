package tony.demo.sys.dao

import com.baomidou.mybatisplus.core.metadata.IPage
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO
import org.apache.ibatis.annotations.Param
import tony.demo.sys.dto.enums.ModuleType
import tony.demo.sys.dto.req.EmployeeQuery
import tony.demo.sys.dto.resp.EmployeeResp
import tony.demo.sys.dto.resp.ModuleResp
import tony.demo.sys.po.Employee
import tony.mybatis.dao.BaseDao

/**
 * 员工dao
 * @author tangli
 * @date 2024/07/08 16:05
 * @since 1.0.0
 */
interface EmployeeDao : BaseDao<Employee> {
    /**
     * 通过查询选择员工
     * @param [query] 参数
     * @param [page] 分页
     * @return [PageDTO]<[EmployeeResp]>
     * @author tangli
     * @date 2024/07/08 16:05
     * @since 1.0.0
     */
    fun selectEmployeeByQuery(
        @Param("query")
        query: EmployeeQuery,
        page: IPage<EmployeeQuery>,
    ): Page<EmployeeResp>

    /**
     * 根据员工id和应用程序id选择员工模块
     * @param [employeeId] 员工id
     * @param [appId] 应用程序id
     * @param [tenantId] 租户id
     * @param [moduleTypes] 模块类型
     * @return [List]<[ModuleResp]>
     * @author tangli
     * @date 2024/07/09 14:03
     * @since 1.0.0
     */
    fun selectEmployeeModulesByEmployeeIdAndAppId(
        @Param("employeeId")
        employeeId: String,
        @Param("appId")
        appId: String,
        @Param("tenantId")
        tenantId: String,
        @Param("moduleTypes")
        moduleTypes: Collection<ModuleType>,
    ): List<ModuleResp>
}
