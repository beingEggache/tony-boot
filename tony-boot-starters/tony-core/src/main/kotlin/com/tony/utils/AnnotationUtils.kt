package com.tony.utils

import java.lang.reflect.AnnotatedElement
import java.lang.reflect.Method
import org.springframework.core.annotation.AnnotationUtils

/**
 * AnnotationUtils is
 * @author tangli
 * @since 2023/07/06 16:00
 */
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
