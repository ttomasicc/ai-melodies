package com.aimelodies.repositories

import com.aimelodies.models.domain.Album
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface AlbumRepository : JpaRepository<Album, Long> {

    fun findAllByTitleContainsIgnoreCase(searchQuery: String, pageable: Pageable): Page<Album>
}