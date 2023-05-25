package com.aimelodies.unit.repositories

import com.aimelodies.models.domain.Melody
import com.aimelodies.models.views.MelodyView
import com.aimelodies.repositories.filesystem.AudioFSRepository
import org.assertj.core.api.Assertions.assertThat
import org.hibernate.validator.internal.util.Contracts.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.mock.web.MockMultipartFile
import java.nio.file.Files
import java.nio.file.Path

class AudioFSRepositoryUnitTest {

    val testDir = "file:/tmp/spring-test/"
    val audioFSRepository = AudioFSRepository(testDir)
    val MELODIES_PATH = Path.of(testDir.split(":")[1], "melodies")

    val testAudioDatoteka = MockMultipartFile(
        "87213-1289uasd-9182qaslkm-12asd.mp3",
        "umjetna inteligencija u e-molu.mp3",
        "audio/mpeg",
        byteArrayOf(73, 68, 51, 4, 0, 32, 4, 64)
    )

    @Test
    fun `audio repository should be successfully instantiated`() {
        assertNotNull(audioFSRepository)
    }

    @Test
    fun `saving unsupported type should throw IllegalArgumentException`() {
        assertThrows<IllegalArgumentException> {
            audioFSRepository.save(
                testAudioDatoteka,
                null,
                MelodyView::class.java
            )
        }
    }

    @Test
    fun `saving audio file should return generated file name that can also be deleted`() {
        val generatedName = audioFSRepository.save(
            testAudioDatoteka,
            "test.wav",
            Melody::class.java
        )
        assertNotNull(generatedName)
        assertThat(
            Files.exists(MELODIES_PATH.resolve(generatedName))
        ).isTrue
        assertThat(
            audioFSRepository.delete(generatedName, Melody::class.java)
        ).isTrue
    }
}