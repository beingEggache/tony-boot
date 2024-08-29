/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine.scheduling

import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

/**
 * 任务执行锁接口
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public class LocalLock public constructor() : JobLock {
    override fun tryLock(): Boolean =
        localLock.tryLock()

    override fun unlock() {
        localLock.unlock()
    }

    public companion object {
        /**
         * 防重入锁
         */
        private var pLocalLock: Lock? = null

        public val localLock: Lock
            get() {
                if (null == pLocalLock) {
                    synchronized(LocalLock::class.java) {
                        pLocalLock = ReentrantLock()
                    }
                }
                return pLocalLock!!
            }
    }
}
