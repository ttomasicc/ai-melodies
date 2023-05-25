package com.aimelodies.controllers.assemblers

enum class Links {
    LOGIN,
    LOGOUT,
    TOKEN,
    ARTIST,
    PROFILE,
    ALBUM,
    ALBUMS,
    MELODIES;

    override fun toString() = name.lowercase()
}