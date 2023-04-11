package com.tony.aspect.test

import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.DeclareMixin
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.stereotype.Component

@SpringBootApplication
@EnableAspectJAutoProxy
class TestAspectApp

@Component
@Aspect
object TestAnnoAspect {

    @JvmStatic
    @DeclareMixin("com.tony.aspect.test.TestTarget", interfaces = [])
    fun go(): ITestMixinAspect = TestMixinAspect()
}


interface ITestMixinAspect {
    fun go() {
        println("123")
    }
}

class TestMixinAspect : ITestMixinAspect

@Component
class TestTarget {

    fun gogo() {
        println("I'm leaving")
    }
}
