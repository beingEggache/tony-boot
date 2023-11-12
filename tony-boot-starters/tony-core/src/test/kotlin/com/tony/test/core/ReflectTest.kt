package com.tony.test.core

import com.tony.utils.annotation
import com.tony.utils.println
import org.springframework.beans.BeanUtils

fun main() {
//    testKotlinAnno()
//    testJavaAnno()
    val field = JavaTestReflect::class.java.getDeclaredField("data")
    field.type.println()
    field.declaringClass.println()
}

private fun testJavaAnno(){
    val obj = JavaTestReflect()

    val propertyDescriptor = BeanUtils.getPropertyDescriptor(obj::class.java, "data")

    val field = obj::class.java.getDeclaredField("data")
    val getter = propertyDescriptor?.readMethod
    val setter = propertyDescriptor?.writeMethod

    val fieldAnnotation = field.annotation(ReflectAnno::class.java)
    val getterAnnotation = getter?.annotation(ReflectAnno::class.java)
    val setterAnnotation = setter?.annotation(ReflectAnno::class.java)


    println("field:$field")
    println("getter:$getter")
    println("setter:$setter")

    println("fieldAnnotation:$fieldAnnotation")
    println("setterAnnotation:$setterAnnotation")
    println("getterAnnotation:$getterAnnotation")

    propertyDescriptor?.setValue("data", 2)  // not work
    propertyDescriptor?.getValue("data").println()
    println(obj)
}

private fun testKotlinAnno() {
    val obj = ReflectObj(1)

    val propertyDescriptor = BeanUtils.getPropertyDescriptor(obj::class.java, "data")

    val field = obj::class.java.getDeclaredField("data")
    val getter = propertyDescriptor?.readMethod
    val setter = propertyDescriptor?.writeMethod

    val fieldAnnotation = field.annotation(ReflectAnno::class.java)
    val getterAnnotation = getter?.annotation(ReflectAnno::class.java)
    val setterAnnotation = setter?.annotation(ReflectAnno::class.java)


    println("field:$field")
    println("getter:$getter")
    println("setter:$setter")

    println("fieldAnnotation:$fieldAnnotation")
    println("setterAnnotation:$setterAnnotation")
    println("getterAnnotation:$getterAnnotation")

    propertyDescriptor?.setValue("data", 2)  // not work
    propertyDescriptor?.getValue("data").println()
    println(obj)
}

data class ReflectObj(override var data: Any) : ReflectLike

interface ReflectLike {
    @get:ReflectAnno
    var data: Any
}

@Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@MustBeDocumented
annotation class ReflectAnno
