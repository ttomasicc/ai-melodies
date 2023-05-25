package com.aimelodies.models.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToMany
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedDate
import java.util.Date

@Entity
@Table(name = "melody")
class Melody(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "audio", length = 50)
    val audio: String,

    @Column(name = "name", length = 100)
    var name: String = "My new melody",

    @CreatedDate
    @Column(name = "date_created")
    val dateCreated: Date = Date(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    val author: Artist,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id")
    val genre: Genre,

    @ManyToMany(mappedBy = "melodies")
    val albums: MutableSet<Album> = mutableSetOf()
)