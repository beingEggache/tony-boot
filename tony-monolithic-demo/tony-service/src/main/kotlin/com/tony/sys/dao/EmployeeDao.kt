package com.tony.sys.dao

import com.baomidou.mybatisplus.core.metadata.IPage
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO
import com.tony.mybatis.dao.BaseDao
import com.tony.sys.dto.enums.ModuleType
import com.tony.sys.dto.req.EmployeeQuery
import com.tony.sys.dto.resp.EmployeeResp
import com.tony.sys.dto.resp.ModuleResp
import com.tony.sys.po.Employee
import org.apache.ibatis.annotations.Param

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
