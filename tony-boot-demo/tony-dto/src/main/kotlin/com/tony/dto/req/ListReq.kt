package com.tony.dto.req

import javax.validation.Valid


/**
 *
 * @author tangli
 * @since 2020-11-18 10:22
 */
data class ListReq<E>(@get:Valid val items: List<E>)
