package com.tony.dto.req

import com.tony.PageQueryLike

class UserListQueryReq(
    var query: String?,
    override val page: Long = 1,
    override val size: Long = 10,
    override val ascs: MutableList<String?> = mutableListOf(),
    override val descs: MutableList<String?> = mutableListOf(),
) : PageQueryLike
