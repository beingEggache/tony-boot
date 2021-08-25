package com.tony.core.annotation

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@MustBeDocumented
@Import(TonyBootConfiguration::class)
annotation class EnableTonyBoot

@ComponentScan(basePackages = ["com.tony.**"])
class TonyBootConfiguration
