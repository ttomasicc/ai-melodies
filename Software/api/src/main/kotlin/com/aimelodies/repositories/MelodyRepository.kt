package com.aimelodies.repositories

import com.aimelodies.models.domain.Melody
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface MelodyRepository : JpaRepository<Melody, Long> {

    fun findAllByNameContainsIgnoreCase(searchQuery: String, pageable: Pageable): Page<Melody>
}