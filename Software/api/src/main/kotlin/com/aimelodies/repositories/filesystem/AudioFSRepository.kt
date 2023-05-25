package com.aimelodies.repositories.filesystem

import com.aimelodies.models.domain.Melody
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.util.UUID
import kotlin.io.path.name

@Repository
class AudioFSRepository(
    @Value("\${spring.web.resources.static-locations}")
    directory: String
) : FSRepository(directory) {

    override fun <T : Any> save(file: MultipartFile, existingFileName: String?, type: Class<T>): String {
        existingFileName?.run { delete(this, type) }
        return saveAudio(file, getPath(type))
    }

    override fun <T : Any> getPath(type: Class<T>): Path =
        when (type) {
            Melody::class.java -> MELODIES_PATH
            else -> throw IllegalArgumentException("Unsupported type")
        }

    private fun saveAudio(audio: MultipartFile, path: Path): String {
        val extension = getExtension(audio)
        val outDir = path.resolve("${UUID.randomUUID()}.$extension")

        Files.write(outDir, audio.bytes)

        return outDir.fileName.name
    }
}