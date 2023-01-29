package com.tony.dto.req

import javax.validation.Valid

public data class ListReq<E>(@get:Valid val items: List<E>)
