package com.tony.utils

import java.lang.reflect.AnnotatedElement
import org.springframework.core.annotation.AnnotationUtils

/**
 * AnnotationUtils is
 * @author tangli
 * @since 2023/07/06 16:00
 */
public fun <A : Annotation> AnnotatedElement.hasAnnotation(annotationType: Class<A>): Boolean =
    AnnotationUtils.getAnnotation(this, annotationType) != null

public fun <A : Annotation> AnnotatedElement.annotation(annotationType: Class<A>): A? =
    AnnotationUtils.getAnnotation(this, annotationType)
