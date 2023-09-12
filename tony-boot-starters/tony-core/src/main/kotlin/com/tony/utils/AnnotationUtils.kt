@file:JvmName("AnnotationUtils")

package com.tony.utils

import java.lang.reflect.AnnotatedElement
import java.lang.reflect.Field
import java.lang.reflect.Method
import org.springframework.core.annotation.AnnotationUtils

/**
 * AnnotationUtils is
 * @author Tang Li
 * @date 2023/07/06 16:00
 */

/**
 * Find a single Annotation of annotationType from the field and getter and setter.
 *
 * field -> getter -> setter.
 * @param A 注解泛型
 * @param [annotationType] 注解类型
 * @receiver [Field]
 * @author Tang Li
 * @date 2023/7/12 10:29
 */
public fun <A : Annotation> Field.selfOrGetterOrSetterHasAnnotation(annotationType: Class<A>): Boolean =
    annotationFromSelfOrGetterOrSetter(annotationType) != null

/**
 * [Field] 从 self -> getter -> setter 获取注解
 * @receiver [Field] 字段
 * @param A 注解泛型
 * @param [annotationType] 注解类型
 * @return [A]?
 * @author Tang Li
 * @date 2023/09/12 10:12
 * @since 1.0.0
 */
public fun <A : Annotation> Field.annotationFromSelfOrGetterOrSetter(annotationType: Class<A>): A? =
    annotation(annotationType)
        ?: getter()?.annotation(annotationType)
        ?: setter()?.annotation(annotationType)

/**
 * [Method] 是否有 [annotationType] 注解
 * @receiver [Method] 方法
 * @param A 注解泛型
 * @param [annotationType] 注解类型
 * @return [Boolean]
 * @author Tang Li
 * @date 2023/09/12 10:13
 * @since 1.0.0
 */
public fun <A : Annotation> Method.hasAnnotation(annotationType: Class<A>): Boolean =
    AnnotationUtils.findAnnotation(this, annotationType) != null

/**
 * [Class] 是否有 [annotationType] 注解
 * @receiver [Class] 类
 * @param A 注解泛型
 * @param [annotationType] 注解类型
 * @return [Boolean]
 * @author Tang Li
 * @date 2023/09/12 10:17
 * @since 1.0.0
 */
public fun <A : Annotation> Class<*>.hasAnnotation(annotationType: Class<A>): Boolean =
    AnnotationUtils.findAnnotation(this, annotationType) != null

/**
 * [AnnotatedElement] 是否有 [annotationType] 注解
 * @receiver [AnnotatedElement]
 * @param A 注解泛型
 * @param [annotationType] 注解类型
 * @return [Boolean]
 * @author Tang Li
 * @date 2023/09/12 10:17
 * @since 1.0.0
 */
public fun <A : Annotation> AnnotatedElement.hasAnnotation(annotationType: Class<A>): Boolean =
    AnnotationUtils.findAnnotation(this, annotationType) != null

/**
 * [Method] 获取注解
 * @receiver [Method] 方法
 * @param A 注解泛型
 * @param [annotationType] 注解类型
 * @return [A]?
 * @author Tang Li
 * @date 2023/09/12 10:18
 * @since 1.0.0
 */
public fun <A : Annotation> Method.annotation(annotationType: Class<A>): A? =
    AnnotationUtils.findAnnotation(this, annotationType)

/**
 * [Class] 获取注解
 * @receiver [Class] 方法
 * @param A 注解泛型
 * @param [annotationType] 注解类型
 * @return [A]?
 * @author Tang Li
 * @date 2023/09/12 10:32
 * @since 1.0.0
 */
public fun <A : Annotation> Class<*>.annotation(annotationType: Class<A>): A? =
    AnnotationUtils.findAnnotation(this, annotationType)

/**
 * [AnnotatedElement] 获取注解
 * @receiver [AnnotatedElement]
 * @param A 注解泛型
 * @param [annotationType] 注解类型
 * @return [A]?
 * @author Tang Li
 * @date 2023/09/12 10:33
 * @since 1.0.0
 */
public fun <A : Annotation> AnnotatedElement.annotation(annotationType: Class<A>): A? =
    AnnotationUtils.findAnnotation(this, annotationType)
