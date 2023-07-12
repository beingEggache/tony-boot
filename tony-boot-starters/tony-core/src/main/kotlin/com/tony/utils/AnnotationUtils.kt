@file:JvmName("AnnotationUtils")

package com.tony.utils

import java.lang.reflect.AnnotatedElement
import java.lang.reflect.Field
import java.lang.reflect.Method
import org.springframework.core.annotation.AnnotationUtils

/**
 * AnnotationUtils is
 * @author tangli
 * @since 2023/07/06 16:00
 */

/**
 * Find a single Annotation of annotationType from the field and getter and setter.
 *
 * field -> getter -> setter.
 * @author tangli
 * @since 2023/7/12 10:29
 */
public fun <A : Annotation> Field.selfOrGetterOrSetterHasAnnotation(annotationType: Class<A>): Boolean {
    val fieldHasAnno = AnnotationUtils.findAnnotation(this, annotationType) != null
    if (fieldHasAnno) {
        return true
    }
    val getter = getter()
    if (getter?.hasAnnotation(annotationType) == true) {
        return true
    }
    return setter()?.hasAnnotation(annotationType) == true
}

public fun <A : Annotation> Method.hasAnnotation(annotationType: Class<A>): Boolean =
    AnnotationUtils.findAnnotation(this, annotationType) != null

public fun <A : Annotation> Class<*>.hasAnnotation(annotationType: Class<A>): Boolean =
    AnnotationUtils.findAnnotation(this, annotationType) != null

public fun <A : Annotation> AnnotatedElement.hasAnnotation(annotationType: Class<A>): Boolean =
    AnnotationUtils.findAnnotation(this, annotationType) != null

public fun <A : Annotation> Field.annotationFromSelfOrGetterOrSetter(annotationType: Class<A>): A? {
    val fieldHasAnno = AnnotationUtils.findAnnotation(this, annotationType)
    if (fieldHasAnno != null) {
        return fieldHasAnno
    }

    val getter = getter()
    val getterAnnotation = getter?.annotation(annotationType)
    if (getterAnnotation != null) {
        return getterAnnotation
    }
    return setter()?.annotation(annotationType)
}

public fun <A : Annotation> Method.annotation(annotationType: Class<A>): A? =
    AnnotationUtils.findAnnotation(this, annotationType)

public fun <A : Annotation> Class<*>.annotation(annotationType: Class<A>): A? =
    AnnotationUtils.findAnnotation(this, annotationType)

public fun <A : Annotation> AnnotatedElement.annotation(annotationType: Class<A>): A? =
    AnnotationUtils.findAnnotation(this, annotationType)
