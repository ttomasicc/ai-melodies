package com.aimelodies.repositories.filesystem

import mu.KLogging
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path

sealed class FSRepository(dir: String) {

    protected val ARTISTS_PATH: Path = Path.of(dir.split(":")[1], "artists")
    protected val ALBUMS_PATH: Path = Path.of(dir.split(":")[1], "albums")
    protected val MELODIES_PATH: Path = Path.of(dir.split(":")[1], "melodies")

    companion object : KLogging()

    init {
        if (Files.notExists(ARTISTS_PATH)) {
            Files.createDirectories(ARTISTS_PATH)
            Files.createDirectories(ALBUMS_PATH)
            Files.createDirectories(MELODIES_PATH)
            logger.info { "Successfully created data directories" }
        }
    }

    abstract fun <T : Any> save(file: MultipartFile, existingFileName: String? = null, type: Class<T>): String

    @Throws(IllegalArgumentException::class)
    protected abstract fun <T : Any> getPath(type: Class<T>): Path

    fun <T : Any> delete(fileName: String, type: Class<T>): Boolean =
        Files.deleteIfExists(getPath(type).resolve(fileName))

    protected fun getExtension(file: MultipartFile): String =
        StringUtils.getFilenameExtension(file.originalFilename) ?: ""
}