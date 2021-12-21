package com.tony.core.test

import com.tony.utils.getFromRootAsString
import com.tony.utils.println


fun main() {
    val valueTokenFromRoot = """{"code":20000,"message":"","data":{"test":true},"test":true}"""
        .getFromRootAsString("test")
    valueTokenFromRoot.println()
}
