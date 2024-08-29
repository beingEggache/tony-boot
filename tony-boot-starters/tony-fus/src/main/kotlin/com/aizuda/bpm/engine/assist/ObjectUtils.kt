/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.engine.assist

/**
 * Java 对象判断处理帮助类
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
public object ObjectUtils {
    /**
     * 判断字符串是否为空
     *
     * @param obj 待判断对象
     * @return 是否为空标识
     */
    public fun isEmpty(obj: Any?): Boolean {
        if (null == obj) {
            return true
        }
        if (obj is String) {
            return obj.isEmpty()
        } else if (obj is Collection<*>) {
            return obj.isEmpty()
        } else if (obj is Map<*, *>) {
            return obj.isEmpty()
        }
        return false
    }

    /**
     * 判断字符串是否为非空
     *
     * @param obj 待判断对象
     * @return 是否为非空标识
     */
    @JvmStatic
    public fun isNotEmpty(obj: Any?): Boolean =
        !isEmpty(obj)

    /**
     * 判断 Map 是否未 Collections$SingletonMap 对象
     *
     * @return true 是 false 否
     */
    public fun isSingletonMap(mapObj: Map<*, *>): Boolean =
        mapObj.javaClass.name == "java.util.Collections\$SingletonMap"

    /**
     * 使用反射机制创建类的实例
     *
     * @param clazz 带创建实例的类
     * @return 创建对象
     */
    @Throws(ReflectiveOperationException::class)
    public fun newInstance(clazz: Class<*>): Any {
        val constructor = clazz.getDeclaredConstructor()
        return constructor.newInstance()
    }

    public fun getArgs(args: MutableMap<String?, Any?>?): MutableMap<String?, Any?> {
        var args = args
        if (args == null) {
            args = HashMap()
        } else if (isSingletonMap(args)) {
            // 兼容 Collections.SingletonMap(k, v)
            args = HashMap(args)
        }
        return args
    }
}
