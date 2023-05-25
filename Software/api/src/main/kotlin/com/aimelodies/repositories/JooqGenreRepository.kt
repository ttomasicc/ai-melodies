package com.aimelodies.repositories

import com.aimelodies.models.domain.Genre
import com.aimelodies.models.generated.tables.Melody.MELODY
import com.aimelodies.models.generated.tables.Genre.GENRE
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository

@Repository
class JooqGenreRepository(
    private val dsl: DSLContext
) : GenreRepository {

    override fun findAll(): List<Genre> =
        dsl.selectFrom(GENRE)
            .orderBy(GENRE.NAME.asc())
            .fetchInto(Genre::class.java)

    override fun findByIdOrNull(id: Long): Genre? =
        dsl.selectFrom(GENRE)
            .where(GENRE.ID.eq(id))
            .fetchOneInto(Genre::class.java)

    override fun findByNameIgnoreCase(name: String): Genre? =
        dsl.selectFrom(GENRE)
            .where(DSL.lower(GENRE.NAME).eq(DSL.lower(name)))
            .fetchOneInto(Genre::class.java)

    override fun existsByNameIgnoreCase(name: String): Boolean =
        dsl.fetchExists(
            dsl.selectFrom(GENRE)
                .where(DSL.lower(GENRE.NAME).eq(DSL.lower(name)))
        )

    override fun existsById(id: Long): Boolean =
        dsl.fetchExists(
            dsl.selectFrom(GENRE)
                .where(GENRE.ID.eq(id))
        )

    override fun update(genre: Genre): Genre? =
        dsl.update(GENRE)
            .set(GENRE.NAME, DSL.lower(genre.name))
            .where(GENRE.ID.eq(genre.id))
            .returning()
            .fetchOneInto(Genre::class.java)

    override fun upsert(name: String) {
        dsl.insertInto(GENRE, GENRE.NAME)
            .values(DSL.lower(name))
            .onDuplicateKeyIgnore()
            .execute()
    }

    override fun delete(id: Long): Boolean =
        dsl.deleteFrom(GENRE)
            .where(GENRE.ID.eq(id))
            .execute() > 0

    override fun deleteUnused() {
        dsl.deleteFrom(GENRE)
            .whereNotExists(
                dsl.selectOne().from(MELODY)
                    .where(MELODY.GENRE_ID.eq(GENRE.ID))
            )
            .execute()
    }
}