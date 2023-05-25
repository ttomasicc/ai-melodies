package com.aimelodies.controllers

import com.aimelodies.services.JwtService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthFilter(
    private val jwtService: JwtService,
    private val userDetailsService: UserDetailsService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        request.getHeader("Authorization")?.let { authHeader ->
            if (authHeader.startsWith("Bearer ").not()) {
                filterChain.doFilter(request, response)
                return
            }

            val jwt = authHeader.substring(7)
            setSecurityContext(request, jwt)
        }
        filterChain.doFilter(request, response)
    }

    fun setSecurityContext(request: HttpServletRequest, jwt: String) {
        if (SecurityContextHolder.getContext().authentication == null && jwtService.isValid(jwt)) {
            val username = jwtService.getUsername(jwt)
            val userDetails = userDetailsService.loadUserByUsername(username)

            SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
                userDetails.username,
                userDetails.password,
                userDetails.authorities
            ).apply {
                details = WebAuthenticationDetailsSource().buildDetails(request)
            }
        }
    }
}