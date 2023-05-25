package com.aimelodies.configurations

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration
import java.time.Duration

/**
 * Manual Redis autoconfiguration needed due to Spring 3.x version
 *
 * Find more [here](https://elvisciotti.medium.com/migrate-spring-boot-data-redis-session-to-v3-0-3be482a3cfef)
 */
@Configuration
class RedisHttpSessionConfiguration(
    @Value("\${spring.session.timeout}") val sessionTimeout: Duration,
) : RedisHttpSessionConfiguration() {

    override fun getMaxInactiveInterval(): Duration = sessionTimeout
}