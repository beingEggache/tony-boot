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
public fun <A : Annotation> Field.selfOrGetterOrSetterHasAnnotation(annotationType: Class<A>): Boolean =
    annotationFromSelfOrGetterOrSetter(annotationType) != null

public fun <A : Annotation> Field.annotationFromSelfOrGetterOrSetter(annotationType: Class<A>): A? =
    annotation(annotationType)
        ?: getter()?.annotation(annotationType)
        ?: setter()?.annotation(annotationType)

public fun <A : Annotation> Method.hasAnnotation(annotationType: Class<A>): Boolean =
    AnnotationUtils.findAnnotation(this, annotationType) != null

public fun <A : Annotation> Class<*>.hasAnnotation(annotationType: Class<A>): Boolean =
    AnnotationUtils.findAnnotation(this, annotationType) != null

public fun <A : Annotation> AnnotatedElement.hasAnnotation(annotationType: Class<A>): Boolean =
    AnnotationUtils.findAnnotation(this, annotationType) != null

public fun <A : Annotation> Method.annotation(annotationType: Class<A>): A? =
    AnnotationUtils.findAnnotation(this, annotationType)

public fun <A : Annotation> Class<*>.annotation(annotationType: Class<A>): A? =
    AnnotationUtils.findAnnotation(this, annotationType)

public fun <A : Annotation> AnnotatedElement.annotation(annotationType: Class<A>): A? =
    AnnotationUtils.findAnnotation(this, annotationType)
