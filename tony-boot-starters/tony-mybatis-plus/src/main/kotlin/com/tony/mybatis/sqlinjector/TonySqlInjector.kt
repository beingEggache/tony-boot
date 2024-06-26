package com.tony.mybatis.sqlinjector

import com.baomidou.mybatisplus.core.injector.AbstractMethod
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector
import com.baomidou.mybatisplus.core.metadata.TableInfo
import com.tony.mybatis.sqlinjector.method.PhysicalDelete
import com.tony.mybatis.sqlinjector.method.PhysicalDeleteById
import com.tony.mybatis.sqlinjector.method.PhysicalDeleteByIds
import org.apache.ibatis.session.Configuration

/**
 * 自定义方法注入
 * @author tangli
 * @date 2024/06/26 12:57
 * @since 1.0.0
 */
internal class TonySqlInjector : DefaultSqlInjector() {
    override fun getMethodList(
        configuration: Configuration,
        mapperClass: Class<*>,
        tableInfo: TableInfo,
    ): MutableList<AbstractMethod> =
        super
            .getMethodList(configuration, mapperClass, tableInfo)
            .apply {
                addAll(
                    mutableListOf(
                        PhysicalDelete(),
                        PhysicalDeleteById(),
                        PhysicalDeleteByIds()
                    )
                )
            }
}
