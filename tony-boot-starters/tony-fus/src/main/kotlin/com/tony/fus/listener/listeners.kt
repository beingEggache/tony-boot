package com.tony.fus.listener

import com.tony.fus.db.po.FusInstance
import com.tony.fus.db.po.FusTask
import com.tony.fus.model.enums.EventType
import java.util.function.Supplier

/**
 * FUS Listener
 * @author Tang Li
 * @date 2023/11/13 13:44
 * @since 1.0.0
 */
public fun interface FusListener<T> {
    /**
     * 通知
     * @param [eventType] 事件类型
     * @param [supplier] 数据
     * @author Tang Li
     * @date 2023/11/13 13:44
     * @since 1.0.0
     */
    public fun notify(
        eventType: EventType,
        supplier: Supplier<T>,
    )
}

/**
 * 任务监听器
 * @author Tang Li
 * @date 2023/11/13 13:46
 * @since 1.0.0
 */
public interface TaskListener : FusListener<FusTask>

/**
 * 实例监听器
 * @author Tang Li
 * @date 2023/11/13 13:46
 * @since 1.0.0
 */
public interface InstanceListener : FusListener<FusInstance>
