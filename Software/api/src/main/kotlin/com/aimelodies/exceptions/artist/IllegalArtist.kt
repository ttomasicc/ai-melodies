package com.aimelodies.exceptions.artist

enum class IllegalArtist(val message: String) {
    EXISTING_EMAIL_ADDRESS("The artist with the given email already exists"),
    EXISTING_USERNAME("The artist with the given username already exists")
}