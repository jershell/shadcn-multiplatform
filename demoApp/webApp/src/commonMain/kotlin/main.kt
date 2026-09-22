import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.github.jershell.shadcn.App
import com.github.jershell.shadcn.commonModule
import com.github.jershell.shadcn.platformModule
import org.koin.core.context.startKoin

@OptIn(ExperimentalComposeUiApi::class)
fun main() = ComposeViewport {
    startKoin {
        modules(commonModule, platformModule())
    }
    App()
}
