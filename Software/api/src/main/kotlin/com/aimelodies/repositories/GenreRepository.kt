package com.aimelodies.repositories

import com.aimelodies.models.domain.Genre
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
interface GenreRepository {

    @Cacheable(value = ["genres"], unless = "#result.isEmpty()")
    fun findAll(): List<Genre>

    @Cacheable(value = ["genre"], key = "#id", unless = "#result == null")
    fun findByIdOrNull(id: Long): Genre?

    fun findByNameIgnoreCase(name: String): Genre?

    fun existsByNameIgnoreCase(name: String): Boolean

    fun existsById(id: Long): Boolean

    @Transactional
    @Caching(
        put = [CachePut(value = ["genre"], key = "#genre.id", unless = "#result == null")],
        evict = [CacheEvict(value = ["genres"], allEntries = true, condition = "#result != null")]
    )
    fun update(genre: Genre): Genre?

    @Transactional
    @CacheEvict(value = ["genres"], allEntries = true)
    fun upsert(name: String)

    @Transactional
    @Caching(
        evict = [
            CacheEvict(value = ["genres"], allEntries = true),
            CacheEvict(value = ["genre"], key = "#id")
        ]
    )
    fun delete(id: Long): Boolean

    @Transactional
    @CacheEvict(value = ["genres", "genre"], allEntries = true)
    fun deleteUnused()
}