package com.tony.db.po

import java.time.LocalDateTime

/**
 * 可审计
 * @author Tang Li
 * @date 2023/09/28 15:27
 * @since 1.0.0
 */
interface Auditable :
    CreateAuditable,
    UpdateAuditable

/**
 * 创建 可审计
 * @author Tang Li
 * @date 2023/09/28 15:27
 * @since 1.0.0
 */
interface CreateAuditable {
    /**
     * 创建时间
     */
    var createTime: LocalDateTime?

    /**
     * 创建人
     */
    var creatorId: Long?

    /**
     * 创建人名称
     */
    var creatorName: String?
}

/**
 * 更新 可审计
 * @author Tang Li
 * @date 2023/09/28 15:27
 * @since 1.0.0
 */
interface UpdateAuditable {
    /**
     * 更新时间
     */
    var updateTime: LocalDateTime?

    /**
     * 更新人
     */
    var updatorId: Long?

    /**
     * 更新人名称
     */
    var updatorName: String?
}
