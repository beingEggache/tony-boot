/*
 * MIT License
 *
 * Copyright (c) 2023-present, tangli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

@file:JvmName("Annos")

package com.tony.utils

import org.springframework.core.annotation.AnnotationUtils
import java.lang.reflect.AnnotatedElement
import java.lang.reflect.Field
import java.lang.reflect.Method

/**
 * 注解工具类
 * @author tangli
 * @date 2023/07/06 19:00
 */

/**
 * Find a single Annotation of annotationType from the field and getter and setter.
 *
 * field -> getter -> setter.
 * @param A 注解泛型
 * @param [annotationType] 注解类型
 * @receiver [Field]
 * @author tangli
 * @date 2023/07/12 19:29
 */
public fun <A : Annotation> Field.selfOrGetterOrSetterHasAnnotation(annotationType: Class<A>): Boolean =
    annotationFromSelfOrGetterOrSetter(annotationType) != null

/**
 * [Field] 从 self -> getter -> setter 获取注解
 * @receiver [Field] 字段
 * @param A 注解泛型
 * @param [annotationType] 注解类型
 * @return [A]?
 * @author tangli
 * @date 2023/09/12 19:12
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
 * @author tangli
 * @date 2023/09/12 19:13
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
 * @author tangli
 * @date 2023/09/12 19:17
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
 * @author tangli
 * @date 2023/09/12 19:17
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
 * @author tangli
 * @date 2023/09/12 19:18
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
 * @author tangli
 * @date 2023/09/12 19:32
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
 * @author tangli
 * @date 2023/09/12 19:33
 * @since 1.0.0
 */
public fun <A : Annotation> AnnotatedElement.annotation(annotationType: Class<A>): A? =
    AnnotationUtils.findAnnotation(this, annotationType)
