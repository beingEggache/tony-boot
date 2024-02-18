package com.tony.fus.model

import com.tony.fus.Fus

/**
 * 流程模型接口.
 * @author tangli
 * @date 2024/01/24 10:13
 * @since 1.0.0
 */
public interface FusModel {
    /**
     * 模型内容
     */
    public var modelContent: String

    /**
     * 模型键
     */
    public val modelKey: String

    /**
     * 模型对象
     * @return [FusProcessModel]
     * @author Tang Li
     * @date 2024/01/24 10:15
     * @since 1.0.0
     */
    public fun model(): FusProcessModel =
        Fus.processModelParser.parse(modelContent, modelKey, false)
}
