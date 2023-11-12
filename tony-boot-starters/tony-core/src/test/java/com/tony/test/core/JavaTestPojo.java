package com.tony.test.core;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

/**
 * javaTestPojo is
 *
 * @author Tang Li
 * @date 2023/07/14 11:02
 */
public class JavaTestPojo {

    private Integer age = 18;

    @JsonSetter(nulls = Nulls.SKIP)
    private String name = "tony";

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
