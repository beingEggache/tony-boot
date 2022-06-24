package com.tony.core.test

import com.github.yitter.contract.IdGeneratorOptions
import com.github.yitter.idgen.YitIdHelper
import com.tony.utils.localIp
import com.tony.utils.println
import com.tony.utils.secureRandom
import reactor.core.publisher.Flux
import reactor.kotlin.core.publisher.toFlux


fun main() {
//    val valueTokenFromRoot = """{"code":20000,"message":"","data":{"test":true},"test":true}"""
//        .getFromRootAsString("test")
//    valueTokenFromRoot.println()

    val arr = listOf<Int>()

    val next = Flux.fromIterable(arr)
        .filter { it != null }
        .blockFirst()
        .println()

    localIp().println()
// 保存参数（必须的操作，否则以上设置都不能生效）：

//    repeat(1024) {
//        Thread.sleep(secureRandom.nextInt(200).toLong())
//        YitIdHelper.nextId().println()
//    }
}
