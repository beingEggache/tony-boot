package com.aizuda.bpm.engine.core.enums

/**
 * JSON BPM 节点表达式 条件类型
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author insist
 * @since 1.0
 */
public enum class ConditionType {
    /**
     * 自定义条件字段
     */
    CUSTOM,

    /**
     * 表单条件字段
     */
    FORM,

    ;

    public fun ne(type: String): Boolean =
        !eq(type)

    public fun eq(type: String): Boolean =
        this.name == type
}
