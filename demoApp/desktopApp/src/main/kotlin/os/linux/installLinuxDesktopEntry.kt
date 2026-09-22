package os.linux

import com.github.jershell.shadcn.util.Log
import java.io.File
import os.APP_ID
import os.isLinux
import os.resourceBytes

/** hicolor sizes to install/remove (the full family the DEs request). */
private val hicolorSizes = listOf(16, 24, 32, 40, 48, 64, 96, 128, 256, 512)

/**
 * Install-and-remove-on-close desktop entry for Linux ("icon without installation"):
 * while the app runs it writes a .desktop entry plus the icon (all hicolor sizes +
 * scalable svg) into the user hicolor theme, so the taskbar/menu show the app icon;
 * both are removed on exit.
 *
 * The `Exec` line is rebuilt from the current process command line, so it works for
 * gradle runs and for the unpackaged dist; the packaged deb/rpm install their own
 * icons via compose nativeDistributions and do not need this.
 */
internal fun installLinuxDesktopEntry() {
    if (!isLinux()) return
    runCatching {
        val home = System.getProperty("user.home")
        val appsDir = File(home, ".local/share/applications")
        appsDir.mkdirs()

        hicolorSizes.forEach { size ->
            val dir = File(home, ".local/share/icons/hicolor/${size}x${size}/apps")
            dir.mkdirs()
            resourceBytes("/icons/icon-$size.png")?.let { bytes ->
                File(dir, "${APP_ID}.png").writeBytes(bytes)
                Log.debug("[icons] installed $dir/${APP_ID}.png")
            } ?: Log.debug("[icons] MISSING resource /icons/icon-$size.png")
        }
        val scalableDir = File(home, ".local/share/icons/hicolor/scalable/apps")
        scalableDir.mkdirs()
        resourceBytes("/icons/icon-scalable.svg")?.let { bytes ->
            File(scalableDir, "${APP_ID}.svg").writeBytes(bytes)
            Log.debug("[icons] installed {}/{}.svg", scalableDir, APP_ID)
        }

        val process = ProcessHandle.current().info()
        val command = process.command().orElse("java")
        val args = process.arguments().orElse(emptyArray())
        val exec = (listOf(command) + args).joinToString(" ") { arg ->
            if (arg.contains(' ') || arg.contains('"')) "\"" + arg.replace("\"", "\\\"") + "\"" else arg
        }

        val desktop = listOf(
            "[Desktop Entry]",
            "Type=Application",
            "Version=1.0",
            "Name=shadcn-multiplatform",
            "Comment=shadcn/ui components for Compose Multiplatform",
            "Exec=$exec",
            "Icon=${APP_ID}",
            "StartupWMClass=MainKt",
            "Terminal=false",
            "Categories=Development;",
            "",
        ).joinToString("\n")
        File(appsDir, "${APP_ID}.desktop").writeText(desktop)
        Log.debug("[icons] installed ${appsDir.absolutePath}/${APP_ID}.desktop")

        // refresh caches so the DE picks the new entry/icon without a re-login
        runCatching {
            ProcessBuilder("update-desktop-database", appsDir.absolutePath).start().waitFor()
        }
        runCatching {
            val hicolor = File(home, ".local/share/icons/hicolor")
            ProcessBuilder("gtk-update-icon-cache", "-f", "-t", hicolor.absolutePath).start().waitFor()
        }
    }
}

internal fun uninstallLinuxDesktopEntry() {
    if (!isLinux()) return
    runCatching {
        val home = System.getProperty("user.home")
        File(home, ".local/share/applications/${APP_ID}.desktop").delete()
        hicolorSizes.forEach { size ->
            File(home, ".local/share/icons/hicolor/${size}x${size}/apps/${APP_ID}.png").delete()
        }
        File(home, ".local/share/icons/hicolor/scalable/apps/${APP_ID}.svg").delete()
        Log.debug("[icons] temporary desktop entry removed")
    }
}
