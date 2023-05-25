package com.aimelodies.repositories.filesystem

import com.aimelodies.models.domain.Album
import com.aimelodies.models.domain.Artist
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import org.springframework.web.multipart.MultipartFile
import java.awt.image.BufferedImage
import java.nio.file.Path
import java.util.UUID
import javax.imageio.ImageIO
import kotlin.io.path.name
import kotlin.math.min

@Repository
class ImageFSRepository(
    @Value("\${spring.web.resources.static-locations}")
    directory: String
) : FSRepository(directory) {

    override fun <T : Any> save(file: MultipartFile, existingFileName: String?, type: Class<T>): String {
        existingFileName?.run { delete(this, type) }
        return saveImage(file, getPath(type))
    }

    override fun <T : Any> getPath(type: Class<T>): Path =
        when (type) {
            Artist::class.java -> ARTISTS_PATH
            Album::class.java -> ALBUMS_PATH
            else -> throw IllegalArgumentException("Unsupported type")
        }

    private fun saveImage(image: MultipartFile, path: Path): String {
        val extension = getExtension(image)
        val squareImage = cropImage(ImageIO.read(image.inputStream))
        val outDir = path.resolve("${UUID.randomUUID()}.$extension")

        ImageIO.write(squareImage, extension, outDir.toFile())

        return outDir.fileName.name
    }

    /**
     * Crops the image into a 1:1 ratio while keeping the image focus in the center.
     *
     * @param image
     * @return A cropped image in 1:1 ratio.
     */
    private fun cropImage(image: BufferedImage): BufferedImage {
        val height = image.height
        val width = image.width

        return if (height == width) // No need to crop the image as it is already in the 1:1 ratio
            image
        else {
            // The maximum available image size (pixel-wise) bound by either image width or image height
            val maxLength = min(height, width)

            // Crops the bigger image side to the maxLength and "moves" the canvas to the image center
            var (xOffset, yOffset) = 0 to 0
            if (height == maxLength)
                xOffset = (width - maxLength) / 2
            else
                yOffset = (height - maxLength) / 2

            image.getSubimage(xOffset, yOffset, maxLength, maxLength)
        }
    }
}