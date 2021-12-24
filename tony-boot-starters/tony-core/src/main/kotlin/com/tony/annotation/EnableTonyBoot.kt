package com.tony.annotation

import com.tony.PROJECT_GROUP
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@MustBeDocumented
@Import(TonyBootConfiguration::class)
annotation class EnableTonyBoot

@ComponentScan(basePackages = ["$PROJECT_GROUP.**"])
internal class TonyBootConfiguration
