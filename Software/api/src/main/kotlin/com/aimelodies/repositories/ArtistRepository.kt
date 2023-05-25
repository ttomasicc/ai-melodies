package com.aimelodies.repositories

import com.aimelodies.models.domain.Artist
import org.springframework.data.jpa.repository.JpaRepository

interface ArtistRepository : JpaRepository<Artist, Long> {

    fun findByUnameIgnoreCase(username: String): Artist?

    fun existsByUnameIgnoreCase(username: String): Boolean

    fun existsByEmailIgnoreCase(email: String): Boolean
}