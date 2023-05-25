package com.aimelodies.models.enumerations.converters

import com.aimelodies.models.enumerations.ResourceType
import org.springframework.core.convert.converter.Converter

class ResourceTypeConverter : Converter<String, ResourceType> {

    override fun convert(source: String): ResourceType? =
        try {
            ResourceType.valueOf(source.uppercase())
        } catch (ex: IllegalArgumentException) {
            null
        }
}