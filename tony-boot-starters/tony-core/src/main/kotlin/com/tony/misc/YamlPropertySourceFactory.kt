package com.tony.misc

import com.tony.exception.ApiException
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean
import org.springframework.core.env.PropertiesPropertySource
import org.springframework.core.env.PropertySource
import org.springframework.core.io.support.EncodedResource
import org.springframework.core.io.support.PropertySourceFactory

public class YamlPropertySourceFactory : PropertySourceFactory {
    override fun createPropertySource(name: String?, resource: EncodedResource): PropertySource<*> {
        val ymlProperties = YamlPropertiesFactoryBean().apply {
            setResources(resource.resource)
            afterPropertiesSet()
        }.getObject() ?: throw ApiException("yml properties null")
        val propertyName = (name ?: resource.resource.filename)
            ?: throw ApiException("Property source name must contain at least one character")
        return PropertiesPropertySource(propertyName, ymlProperties)
    }
}
