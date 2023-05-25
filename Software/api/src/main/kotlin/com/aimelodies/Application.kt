package com.aimelodies

import com.aimelodies.tasks.GenreSchedulingService
import org.springframework.beans.factory.getBean
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.util.TimeZone

@SpringBootApplication
class Application

fun main(args: Array<String>) {
    val app = runApplication<Application>(*args)

    // Upserts new genres upon application startup
    val genreSchedulingService = app.getBean<GenreSchedulingService>()
    genreSchedulingService.upsertGenres()

    TimeZone.setDefault(TimeZone.getTimeZone("Europe/Zagreb"))
    println("Application successfully started!")
}