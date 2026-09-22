package os

internal const val APP_ID = "shadcn-multiplatform"

internal fun resourceBytes(name: String): ByteArray? {
    // ClassLoader.getResourceAsStream expects NO leading slash (unlike Class.getResource)
    val path = name.removePrefix("/")
    return Thread.currentThread().contextClassLoader?.getResourceAsStream(path)?.use { it.readBytes() }
}

internal fun isLinux(): Boolean = System.getProperty("os.name").lowercase().contains("linux")
