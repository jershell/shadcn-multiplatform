package com.github.jershell.shadcn

import com.github.jershell.shadcn.services.FileService
import kotlinx.io.buffered
import kotlinx.io.files.FileSystem
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readString
import kotlinx.io.writeString

class JvmFileService(): FileService {
    private val fileSystem: FileSystem = SystemFileSystem

    // Use the current project directory
    private val appDir = "."

    // Helper method to create a Path object from a string
    private fun getPath(filename: String): Path {
        return Path("$appDir/$filename")
    }

    /**
     * Reads the whole text file.
     * Returns the string or null if the file does not exist.
     */
    override fun readTextFile(filename: String): String? {
        val path = getPath(filename)

        // Check that the file exists before reading
        if (!fileSystem.exists(path)) {
            return null
        }

        // Open the data source (Source), buffer it and read into a string
        return fileSystem.source(path).buffered().use { source ->
            source.readString()
        }
    }

    /**
     * Writes (or overwrites) text content to the file.
     */
    override fun writeTextFile(filename: String, content: String) {
        val path = getPath(filename)

        // Open the data sink (Sink), buffer it and write the string
        fileSystem.sink(path).buffered().use { sink ->
            sink.writeString(content)
        }
    }

    /**
     * Checks whether the file exists at the given path.
     */
    override fun fileExists(filename: String): Boolean {
        val path = getPath(filename)
        return fileSystem.exists(path)
    }
}
