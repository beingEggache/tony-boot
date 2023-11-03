package com.tony.flow.service

import com.tony.flow.db.po.FlowProcess
import com.tony.flow.model.FlowOperator

/**
 * 流程定义 Service
 * @author Tang Li
 * @date 2023/10/09 14:20
 * @since 1.0.0
 */
public interface ProcessService {
    /**
     * 根据主键ID获取流程定义对象
     * @param [processId] 流程定义id
     * @return [FlowProcess] 流程定义对象
     * @author Tang Li
     * @date 2023/10/09 14:23
     * @since 1.0.0
     */
    public fun getById(processId: String): FlowProcess

    /**
     * 根据流程名称或版本号查找流程定义对象
     * @param [processName] 流程定义名称
     * @param [processVersion] 版本
     * @return [FlowProcess]
     * @author Tang Li
     * @date 2023/10/09 14:25
     * @since 1.0.0
     */
    public fun getByVersion(
        processName: String,
        processVersion: Int?,
    ): FlowProcess

    /**
     * 根据流程名称查找流程定义对象
     * @param [processName] 流程定义名称
     * @return [FlowProcess]
     * @author Tang Li
     * @date 2023/10/09 14:26
     * @since 1.0.0
     */
    public fun getByName(processName: String): FlowProcess =
        getByVersion(processName, null)

    /**
     * 部署流程
     * @param [modelContent] 流程定义json字符串
     * @param [flowCreator] 流程创建者
     * @param [repeat] 是否重复部署 true 存在版本+1新增一条记录 false 存在流程直接返回
     * @author Tang Li
     * @date 2023/10/09 14:52
     * @since 1.0.0
     */
    public fun deploy(
        modelContent: String,
        flowCreator: FlowOperator,
        repeat: Boolean,
    ): String

    /**
     * 重新部署流程
     * @param [processId] 流程定义id
     * @param [modelContent] 流程定义json字符串
     * @return [Boolean]
     * @author Tang Li
     * @date 2023/10/09 15:53
     * @since 1.0.0
     */
    public fun redeploy(
        processId: String,
        modelContent: String,
    ): Boolean

    /**
     * 级联移除
     *
     * 谨慎使用！！！不可恢复，
     * 级联删除指定流程定义的所有数据
     * @param [processId] 流程定义ID
     * @author Tang Li
     * @date 2023/10/10 09:19
     * @since 1.0.0
     */
    public fun cascadeRemove(processId: String)
}
