package com.aimelodies.models.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedDate
import java.util.Date

@Entity
@Table(name = "album")
class Album(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "image", length = 50)
    var image: String? = null,

    @Column(name = "title", length = 100)
    var title: String = "My new album",

    @CreatedDate
    @Column(name = "date_created")
    val dateCreated: Date = Date(),

    @Column(name = "description")
    var description: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id")
    val artist: Artist,

    @ManyToMany
    @JoinTable(
        name = "album_melody",
        joinColumns = [JoinColumn(name = "album_id")],
        inverseJoinColumns = [JoinColumn(name = "melody_id")]
    )
    val melodies: MutableSet<Melody> = mutableSetOf(),
)