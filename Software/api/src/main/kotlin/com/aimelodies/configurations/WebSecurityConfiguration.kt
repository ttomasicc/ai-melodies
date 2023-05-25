package com.aimelodies.configurations

import com.aimelodies.controllers.JwtAuthFilter
import com.aimelodies.models.enumerations.Role
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.access.hierarchicalroles.RoleHierarchy
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class WebSecurityConfiguration(
    private val jwtAuthFilter: JwtAuthFilter,
    private val authenticationProvider: AuthenticationProvider
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            cors { }
            csrf { disable() }

            sessionManagement { sessionCreationPolicy = SessionCreationPolicy.STATELESS }

            authorizeRequests {
                // Auth
                authorize(HttpMethod.GET, "/api/v1/auth/token", permitAll)
                authorize(HttpMethod.POST, "/api/v1/auth/login", permitAll)
                authorize(HttpMethod.POST, "/api/v1/auth/register", permitAll)
                authorize(HttpMethod.DELETE, "/api/v1/auth/logout", permitAll)
                authorize(HttpMethod.GET, "/api/v1/auth/current", hasRole((Role.ARTIST.name)))

                // Artists
                authorize(HttpMethod.GET, "/api/v1/artists/*", permitAll)
                authorize(HttpMethod.PUT, "/api/v1/artists/*", hasRole(Role.ARTIST.name))

                // Albums
                authorize(HttpMethod.GET, "/api/v1/artists/*/albums", hasRole(Role.ARTIST.name))
                authorize(HttpMethod.GET, "/api/v1/artists/*/albums/*", hasRole(Role.ARTIST.name))
                authorize(HttpMethod.POST, "/api/v1/artists/*/albums", hasRole(Role.ARTIST.name))
                authorize(HttpMethod.PUT, "/api/v1/artists/*/albums/*", hasRole(Role.ARTIST.name))
                authorize(HttpMethod.DELETE, "/api/v1/artists/*/albums/*", hasRole(Role.ARTIST.name))

                // Melodies
                authorize(HttpMethod.GET, "/api/v1/artists/*/albums/*/melodies", hasRole(Role.ARTIST.name))
                authorize(HttpMethod.GET, "/api/v1/artists/*/albums/*/melodies/*", hasRole(Role.ARTIST.name))
                authorize(HttpMethod.POST, "/api/v1/artists/*/albums/*/melodies", hasRole(Role.ARTIST.name))
                authorize(HttpMethod.PUT, "/api/v1/artists/*/albums/*/melodies/*", hasRole(Role.ARTIST.name))
                authorize(HttpMethod.DELETE, "/api/v1/artists/*/albums/*/melodies/*", hasRole(Role.ARTIST.name))

                // Genres
                authorize(HttpMethod.GET, "/api/v1/genres", hasRole(Role.ARTIST.name))
                authorize(HttpMethod.POST, "/api/v1/genres", hasRole(Role.ADMINISTRATOR.name))
                authorize(HttpMethod.DELETE, "/api/v1/genres", hasRole(Role.ADMINISTRATOR.name))
                authorize(HttpMethod.PUT, "/api/v1/genres/*", hasRole(Role.ADMINISTRATOR.name))
                authorize(HttpMethod.DELETE, "/api/v1/genres/*", hasRole(Role.ADMINISTRATOR.name))

                // Info
                authorize(HttpMethod.GET, "/api/v1/info/search", hasRole(Role.ARTIST.name))
                authorize(HttpMethod.GET, "/api/v1/info/new", permitAll)

                authorize("/error", permitAll)
                authorize("/static/**", permitAll)

                authorize(anyRequest, authenticated)
            }

            addFilterBefore<UsernamePasswordAuthenticationFilter>(jwtAuthFilter)
        }

        http.authenticationProvider(authenticationProvider)

        return http.build()
    }

    @Bean
    fun customWebSecurityExpressionHandler(): DefaultWebSecurityExpressionHandler =
        DefaultWebSecurityExpressionHandler().apply {
            setRoleHierarchy(roleHierarchy())
        }

    @Bean
    fun roleHierarchy(): RoleHierarchy =
        RoleHierarchyImpl().apply {
            setHierarchy("ROLE_${Role.ADMINISTRATOR.name} > ROLE_${Role.ARTIST.name}")
        }
}