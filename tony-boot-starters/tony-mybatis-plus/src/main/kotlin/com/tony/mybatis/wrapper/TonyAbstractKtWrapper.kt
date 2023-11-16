package com.tony.mybatis.wrapper

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils
import com.baomidou.mybatisplus.core.toolkit.StringPool
import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache
import java.util.stream.Collectors
import kotlin.reflect.KProperty1

/**
 * TonyAbstractKtWrapper is
 * @author tangli
 * @date 2023/11/16 15:54
 * @since 1.0.0
 */
public abstract class TonyAbstractKtWrapper<T, Children : TonyAbstractKtWrapper<T, Children>> :
    AbstractWrapper<T, KProperty1<T, *>, Children>() {
    /**
     * 列 Map
     */
    protected lateinit var columnMap: Map<String, ColumnCache>

    /**
     * 重载方法，默认 onlyColumn = true
     */
    override fun columnToString(kProperty: KProperty1<T, *>): String? =
        columnToString(kProperty, true)

    /**
     * 核心实现方法，供重载使用
     */
    private fun columnToString(
        kProperty: KProperty1<T, *>,
        onlyColumn: Boolean,
    ): String? =
        columnMap[
            LambdaUtils.formatKey(
                kProperty.name
            )
        ]?.let { if (onlyColumn) it.column else it.columnSelect }

    /**
     * 批量处理传入的属性，正常情况下的输出就像：
     *
     * "user_id" AS "userId" , "user_name" AS "userName"
     */
    public fun columnsToString(
        onlyColumn: Boolean,
        vararg columns: KProperty1<T, *>,
    ): String =
        columns.mapNotNull { columnToString(it, onlyColumn) }.joinToString(separator = StringPool.COMMA)

    /**
     * 批量处理传入的属性，正常情况下的输出就像：
     *
     * "user_id" AS "userId" , "user_name" AS "userName"
     */
    public fun columnsToString(
        onlyColumn: Boolean,
        columns: MutableList<KProperty1<T, *>>,
    ): String =
        columns.stream().map { columnToString(it, onlyColumn) }.collect(Collectors.joining(StringPool.COMMA))

    override fun initNeed() {
        super.initNeed()
        if (!::columnMap.isInitialized) {
            columnMap = LambdaUtils.getColumnMap(this.entityClass)
        }
    }
}
