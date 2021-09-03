package com.tony.webcore.log

import javax.servlet.http.HttpServletResponse

const val SUCCESS = "SUCCESS"

const val FAILED = "FAILED"

const val BIZ_FAILED = "BIZ_FAILED"

const val VALIDATE_FAILED = "VALIDATE_FAILED"

const val UNAUTHORIZED = "UNAUTHORIZED"

const val NULL = "[null]"

internal val HTTP_SUCCESS_CODE = arrayOf(
    HttpServletResponse.SC_OK,
    HttpServletResponse.SC_CREATED,
    HttpServletResponse.SC_NOT_MODIFIED,
    HttpServletResponse.SC_MOVED_PERMANENTLY,
    HttpServletResponse.SC_MOVED_TEMPORARILY
)
