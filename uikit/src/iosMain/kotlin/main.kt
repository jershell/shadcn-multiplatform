import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ComposeUIViewController
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.containers.ShadcnUI
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.Mode
import platform.UIKit.UIApplication
import platform.UIKit.UIStatusBarStyleDarkContent
import platform.UIKit.UIStatusBarStyleLightContent
import platform.UIKit.UIViewController
import platform.UIKit.setStatusBarStyle

fun MainViewController(): UIViewController = ComposeUIViewController {
    ShadcnUI(onModeChanged = { ThemeChanged(it) }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Theme[ColorProps][ColorTokens.background])
                .padding(24.dp)
        ) {
            H4("UIKit Preview")
        }
    }
}

@Composable
private fun ThemeChanged(mode: Mode) {
    LaunchedEffect(mode) {
        UIApplication.sharedApplication.setStatusBarStyle(
            if (mode == Mode.Dark) UIStatusBarStyleDarkContent else UIStatusBarStyleLightContent
        )
    }
}
