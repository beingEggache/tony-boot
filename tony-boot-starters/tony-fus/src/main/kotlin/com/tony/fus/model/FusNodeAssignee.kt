package com.tony.fus.model

/**
 * FusNodeAssignee is
 * @author tangli
 * @date 2023/10/24 17:09
 * @since 1.0.0
 */
public data class FusNodeAssignee
    @JvmOverloads
    constructor(
        public val id: String,
        public val name: String,
        public val weight: Int = 0,
    )
