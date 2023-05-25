package com.aimelodies.tasks

import com.aimelodies.configurations.settings.SpotifySettings
import com.aimelodies.models.responses.SpotifyAPIGenresResponse
import com.aimelodies.models.responses.SpotifyAPITokenResponse
import com.aimelodies.repositories.GenreRepository
import java.util.Base64
import mu.KLogging
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.reactive.function.client.body
import org.springframework.web.reactive.function.client.bodyToMono
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class GenreSchedulingService(
    private val webClient: WebClient,
    private val spotifySettings: SpotifySettings,
    private val genreRepository: GenreRepository
) {

    companion object : KLogging()

    // Every day at midnight (Europe/Zagreb TimeZone)
    @Scheduled(cron = "0 0 0 * * *")
    // Safety: if communication between multiple application instances breaks for a couple of minutes
    @SchedulerLock(name = "genres-update", lockAtLeastFor = "5m")
    @Transactional
    fun upsertGenres() {
        logger.info { "Upserting new genres" }

        fetchGenres().subscribe { apiResponse ->
            apiResponse.genres.forEach {
                genreRepository.upsert(it)
                logger.info { "Upserting genre: $it" }
            }
            logger.info { "Upserting new genres from Spotify API finished" }
        }
    }

    fun fetchGenres(): Mono<SpotifyAPIGenresResponse> =
        fetchSpotifyAccessToken().flatMap {
            logger.info { "Spotify token: ${it.token}" }
            webClient
                .get()
                .uri("${spotifySettings.baseUrl}/recommendations/available-genre-seeds")
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer ${it.token}")
                .retrieve()
                .bodyToMono()
        }

    fun fetchSpotifyAccessToken(): Mono<SpotifyAPITokenResponse> =
        webClient
            .post()
            .uri("https://accounts.spotify.com/api/token")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .header(
                HttpHeaders.AUTHORIZATION,
                "Basic ${
                Base64.getEncoder().encodeToString(
                    "${spotifySettings.clientId}:${spotifySettings.clientSecret}".toByteArray(Charsets.UTF_8)
                )
                }"
            )
            .body(Mono.just("grant_type=client_credentials"))
            .retrieve()
            .bodyToMono()
}