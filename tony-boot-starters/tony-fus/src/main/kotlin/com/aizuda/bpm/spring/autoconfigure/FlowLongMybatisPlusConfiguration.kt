/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package com.aizuda.bpm.spring.autoconfigure

import org.mybatis.spring.annotation.MapperScan
import org.springframework.context.annotation.Configuration

/**
 * FlowLong MybatisPlus 加载配置处理类
 *
 *
 *
 * 尊重知识产权，不允许非法使用，后果自负
 *
 *
 * @author hubin
 * @since 1.0
 */
@Configuration
@MapperScan("com.aizuda.bpm.engine.dao")
public class FlowLongMybatisPlusConfiguration public constructor()
