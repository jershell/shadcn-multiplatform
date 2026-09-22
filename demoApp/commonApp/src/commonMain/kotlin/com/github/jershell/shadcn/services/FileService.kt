package com.github.jershell.shadcn.services

interface FileService {

    /**
     * Reads the whole text file.
     * Returns the string or null if the file does not exist.
     */
    fun readTextFile(filename: String): String?

    /**
     * Writes (or overwrites) text content to the file.
     */
    fun writeTextFile(filename: String, content: String)

    /**
     * Checks whether the file exists at the given path.
     */
    fun fileExists(filename: String): Boolean
}