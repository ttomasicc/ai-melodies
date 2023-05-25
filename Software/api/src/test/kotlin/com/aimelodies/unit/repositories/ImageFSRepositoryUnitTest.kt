package com.aimelodies.unit.repositories

import com.aimelodies.models.domain.Album
import com.aimelodies.models.domain.Artist
import com.aimelodies.models.views.MelodyView
import com.aimelodies.repositories.filesystem.ImageFSRepository
import org.assertj.core.api.Assertions.assertThat
import org.hibernate.validator.internal.util.Contracts.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.mock.web.MockMultipartFile
import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class ImageFSRepositoryUnitTest {

    val testDir = "file:/tmp/spring-test/"
    val imageFSRepository = ImageFSRepository(testDir)
    val ARTISTS_PATH = Path.of(testDir.split(":")[1], "artists")
    val ALBUMS_PATH = Path.of(testDir.split(":")[1], "albums")

    val testImageFile = MockMultipartFile(
        "98hfs-39dskl-asd81s-12fsa.jpeg",
        "luna.jpeg",
        "image/jpeg",
        FileInputStream(Paths.get("src", "test", "resources", "static", "luna.jpeg").toFile())
    )

    @Test
    fun `image repository should be successfully instantiated`() {
        assertNotNull(imageFSRepository)
    }

    @Test
    fun `saving unsupported type should throw IllegalArgumentException`() {
        assertThrows<IllegalArgumentException> {
            imageFSRepository.save(
                testImageFile,
                null,
                MelodyView::class.java
            )
        }
    }

    @Test
    fun `saving artist image should return generated file name that can also be deleted`() {
        val generatedName = imageFSRepository.save(
            testImageFile,
            "test.jpg",
            Artist::class.java
        )
        assertNotNull(generatedName)
        assertThat(
            Files.exists(ARTISTS_PATH.resolve(generatedName))
        ).isTrue
        assertThat(
            imageFSRepository.delete(generatedName, Artist::class.java)
        ).isTrue
    }

    @Test
    fun `saving album image should return generated file name that can also be deleted`() {
        val generatedName = imageFSRepository.save(
            testImageFile,
            "test2.jpg",
            Album::class.java
        )
        assertNotNull(generatedName)
        assertThat(
            Files.exists(ALBUMS_PATH.resolve(generatedName))
        ).isTrue
        assertThat(
            imageFSRepository.delete(generatedName, Album::class.java)
        ).isTrue
    }
}