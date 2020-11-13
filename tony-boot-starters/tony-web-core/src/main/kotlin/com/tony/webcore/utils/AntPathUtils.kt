package com.tony.webcore.utils

import com.tony.core.utils.defaultIfBlank
import org.springframework.util.AntPathMatcher

internal val antPathMatcher = AntPathMatcher()

fun AntPathMatcher.matchAny(patterns: List<String>?, path: String?) =
    patterns?.any { match(it, path.defaultIfBlank()) } ?: false
