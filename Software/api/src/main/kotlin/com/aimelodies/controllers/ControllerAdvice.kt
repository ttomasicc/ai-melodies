package com.aimelodies.controllers

import mu.KLogging
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.sql.SQLException
import java.time.Instant
import java.time.ZoneOffset

/**
 * Controller Advice - catches all unexpected exceptions
 */
@RestControllerAdvice
class ControllerAdvice {

    companion object : KLogging()

    /**
     * Exception Handler for all (SQL) database exceptions
     *
     * @param sqlException general unexpected SQL Exception that should be caught now
     * @return [ResponseEntity] informing the user that something went wrong on the server side
     */
    @ExceptionHandler
    fun handleDatabaseExceptions(sqlException: SQLException): ResponseEntity<String> {
        logger.warn { "Controller Advice caught unexpected SQL Exception: $sqlException" }

        return ResponseEntity(
            """{
                "timestamp": "${Instant.now().atOffset(ZoneOffset.UTC)}",
                "status": 500,
                "error": "Internal Server Error",
                "message": "Unexpected error occurred, please try again later or contact the support line."
                }
            """.trimIndent(),
            HttpHeaders().apply {
                contentType = MediaType.APPLICATION_JSON
            },
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }
}