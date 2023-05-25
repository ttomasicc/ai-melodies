package com.aimelodies.configurations

import com.aimelodies.models.enumerations.converters.ResourceTypeConverter
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfiguration : WebMvcConfigurer {

    private val allowedOrigins = arrayOf(
        "http://localhost:4200",
        "http://host.docker.internal:4200"
    )

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins(*allowedOrigins)
            .allowCredentials(true)
            .allowedMethods("GET", "POST", "PUT", "DELETE");
    }

    override fun addFormatters(registry: FormatterRegistry) {
        registry.addConverter(ResourceTypeConverter())
    }
}