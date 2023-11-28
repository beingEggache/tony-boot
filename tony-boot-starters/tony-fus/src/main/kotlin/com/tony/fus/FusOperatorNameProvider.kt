package com.tony.fus

/**
 * FusOperatorNameProvider is
 * @author tangli
 * @date 2023/11/28 14:24
 * @since 1.0.0
 */
public interface FusOperatorNameProvider {
    public fun operatorName(userId: String): String

    public val adminId: String
        get() = "ADMIN"

    public val adminName: String
        get() = "管理员"
}

internal class DefaultOperatorNameProvider : FusOperatorNameProvider {
    override fun operatorName(userId: String): String =
        ""
}
