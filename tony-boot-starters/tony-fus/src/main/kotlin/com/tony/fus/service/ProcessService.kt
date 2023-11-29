package com.tony.fus.service

import com.tony.fus.db.po.FusProcess

/**
 * 流程定义 Service
 * @author Tang Li
 * @date 2023/10/09 14:20
 * @since 1.0.0
 */
public interface ProcessService {
    /**
     * 根据主键ID获取流程定义对象
     * @param [processId] 流程id
     * @return [FusProcess] 流程定义对象
     * @author Tang Li
     * @date 2023/10/09 14:23
     * @since 1.0.0
     */
    public fun getById(processId: String): FusProcess

    /**
     * 部署流程
     * @param [modelContent] 流程定义json字符串
     * @param [userId] 操作人id
     * @param [repeat] 是否重复部署 true 存在版本+1新增一条记录 false 存在流程直接返回
     * @return [String] 流程id
     * @author Tang Li
     * @date 2023/10/09 14:52
     * @since 1.0.0
     */
    public fun deploy(
        modelContent: String,
        userId: String,
        repeat: Boolean,
    ): String

    /**
     * 重新部署流程
     * @param [processId] 流程id
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
     * @param [processId] 流程id
     * @author Tang Li
     * @date 2023/10/10 09:19
     * @since 1.0.0
     */
    public fun cascadeRemove(processId: String)
}
