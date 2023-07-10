package com.tony.dto.req

import javax.validation.Valid

data class ListReq<E>(
    @get:Valid val items: List<E>,
)
