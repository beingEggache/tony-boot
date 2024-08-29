package com.aizuda.bpm.engine.core.enums

/**
 * 审批人与提交人为同一人时
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public enum class NodeApproveSelf(
    private val value: Int,
) {
    /**
     * 由发起人对自己审批
     */
    INITIATOR_THEMSELVES(0),

    /**
     * 自动跳过
     */
    AUTO_SKIP(1),

    /**
     * 转交给直接上级审批
     */
    TRANSFER_DIRECT_SUPERIOR(2),

    /**
     * 转交给部门负责人审批
     */
    TRANSFER_DEPARTMENT_HEAD(3),
    ;

    public fun ne(value: Int): Boolean =
        !eq(value)

    public fun eq(value: Int): Boolean =
        this.value == value
}
