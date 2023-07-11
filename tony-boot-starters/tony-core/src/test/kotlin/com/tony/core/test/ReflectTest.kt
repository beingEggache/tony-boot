package com.tony.core.test

import com.tony.utils.annotation
import com.tony.utils.println
import org.springframework.beans.BeanUtils

fun main() {
    val obj = ReflectObj(1)

    val propertyDescriptor = BeanUtils.getPropertyDescriptor(obj::class.java, "data")

    val getter = propertyDescriptor?.readMethod
    val setter = propertyDescriptor?.writeMethod

    println(getter)
    val getterAnnotation = getter?.annotation(ReflectAnno::class.java)
    println(getterAnnotation)
    println(setter)
    propertyDescriptor?.setValue("data", 2)  // not work
    propertyDescriptor?.getValue("data").println()
    println(obj)
}

data class ReflectObj(override val data: Any) : ReflectLike

interface ReflectLike {

    @get:ReflectAnno
    val data: Any
}


@Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
)
@MustBeDocumented
annotation class ReflectAnno
