/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine

/**
 * JSON BPM 流程数据传输类
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public object FlowDataTransfer {
    /**
     * 传递参数存取
     */
    @JvmStatic
    private val flowData = ThreadLocal<MutableMap<String, Any>>()

    /**
     * 设置传递参数
     *
     * @param requestData 传递参数 MAP 对象
     */
    @JvmStatic
    public fun put(requestData: MutableMap<String, Any>) {
        flowData.set(requestData)
    }

    /**
     * 设置传递参数
     *
     * @param key   关键字
     * @param value 参数值
     */
    @JvmStatic
    public fun put(
        key: String,
        value: Any,
    ) {
        val dataMap = all
        if (null != dataMap && !dataMap.isEmpty()) {
            dataMap[key] = value
        } else {
            // 创建设置值
            put(
                object : HashMap<String, Any>(16) {
                    init {
                        put(key, value)
                    }
                }
            )
        }
    }

    /**
     * 动态分配节点处理人或角色
     *
     * @param dataMap 处理人或角色信息
     */
    @JvmStatic
    public fun dynamicAssignee(dataMap: Map<String?, Any?>) {
        put(FlowConstants.PROCESS_DYNAMIC_ASSIGNEE, dataMap)
    }

    /**
     * 指定选择条件节点 KEY
     *
     * @param conditionNodeKey 条件节点 KEY
     */
    @JvmStatic
    public fun specifyConditionNodeKey(conditionNodeKey: String) {
        put(FlowConstants.PROCESS_SPECIFY_CONDITION_NODE_KEY, conditionNodeKey)
    }

    /**
     * 获取传递参数
     *
     * @param key 传递参数
     * @return 传递参数 MAP 对象
     */
    @JvmStatic
    public fun <T> get(key: String): T? {
        val dataMap: Map<String, Any>? = all
        if (null != dataMap) {
            return dataMap[key] as T?
        }
        return null
    }

    @JvmStatic
    public val all: MutableMap<String, Any>?
        /**
         * 获取传递参数
         *
         * @return 传递参数 MAP 对象
         */
        get() = flowData.get()

    /**
     * 移除传递参数
     */
    @JvmStatic
    public fun remove() {
        flowData.remove()
    }
}
