package com.tony.fus.db.po

import com.baomidou.mybatisplus.annotation.TableName
import com.tony.fus.db.enums.InstanceState
import java.time.LocalDateTime

/**
 * 历史流程实例
 * @author Tang Li
 * @date 2023/09/29 16:13
 * @since 1.0.0
 */
@TableName
public class FusHistoryInstance : FusInstance() {
    /**
     * 流程实例状态: 1.审批中, 2.审批通过, 3.审批拒绝, 4.超时结束, 5.强制终止, 6.撤销审批
     */
    public var instanceState: InstanceState = InstanceState.ACTIVE

    /**
     * 结束时间
     */
    public var endTime: LocalDateTime? = null
}
