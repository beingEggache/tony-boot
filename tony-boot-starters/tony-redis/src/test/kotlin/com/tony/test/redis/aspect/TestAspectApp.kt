package com.tony.test.redis.aspect

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.context.expression.MethodBasedEvaluationContext
import org.springframework.core.DefaultParameterNameDiscoverer
import org.springframework.expression.TypedValue
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

@SpringBootApplication
@EnableAspectJAutoProxy
class TestAspectApp

@Service
class TestAopService {

    @TestAnnoAop(expression = "#obj.name")
    fun testAop(obj: TestAnnoAopArg) {
        println("I'm aop.")
    }
}

class TestAnnoAopArg(
    var name: String
)

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class TestAnnoAop(
    val expression: String,
)

@Component
@Aspect
object TestAnnoAspect {

    @Around("@annotation(anno)")
    fun doAop(joinPoint: ProceedingJoinPoint, anno: TestAnnoAop): Any? {
        val arguments = joinPoint.args
        val methodSignature = joinPoint.signature as MethodSignature
//
//        val paramMap = methodSignature.parameterNames.foldIndexed(mutableMapOf<String, Any?>()) { index, acc, s ->
//            acc[s] = arguments[index]
//            acc
//        }
        val value = SpelExpressionParser()
            .parseExpression(anno.expression)
            .getValue(
                MethodBasedEvaluationContext(
                    TypedValue.NULL,
                    methodSignature.method,
                    arguments,
                    DefaultParameterNameDiscoverer()
                )
            )
        println(value)
        return joinPoint.proceed()
    }
}
