package com.tony.buildscript

import org.gradle.api.Project

fun getProfile(): String = System.getProperty("profile", "dev")

fun Project.getImageNameFromProperty(): String = System.getProperty("project_name", name)
