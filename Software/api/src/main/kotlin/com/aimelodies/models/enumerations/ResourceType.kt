package com.aimelodies.models.enumerations

enum class ResourceType {
    ALBUM,
    MELODY;

    object Names {
        const val ALBUM = "ALBUM"
        const val MELODY = "MELODY"
    }
}