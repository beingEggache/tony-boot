package com.tony.dto.req

import jakarta.validation.Valid

data class ListReq<E>(
    @get:Valid val rows: List<E>,
)
