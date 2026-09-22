import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.github.jershell.shadcn.App
import com.github.jershell.shadcn.commonModule
import com.github.jershell.shadcn.platformModule
import java.awt.Dimension
import kotlinx.io.files.FileSystem
import kotlinx.io.files.SystemFileSystem
import org.koin.core.context.startKoin
import org.koin.dsl.module
import os.isLinux
import os.linux.installLinuxDesktopEntry
import os.linux.uninstallLinuxDesktopEntry
import os.windowIconPainter

fun main() = application {
    startKoin {
        modules(
            commonModule,
            platformModule(),
            module {
                single<FileSystem> {
                    SystemFileSystem
                }
            },
        )
    }

    if (isLinux()) {
        installLinuxDesktopEntry()
        Runtime.getRuntime().addShutdownHook(Thread { uninstallLinuxDesktopEntry() })
    }

    Window(
        title = "shadcn-multiplatform",
        state = rememberWindowState(width = 800.dp, height = 600.dp),
        onCloseRequest = ::exitApplication,
        icon = windowIconPainter(),
    ) {
        window.minimumSize = Dimension(350, 600)
        App()
    }
}
