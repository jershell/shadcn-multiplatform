@file:OptIn(ExperimentalSerializationApi::class)

package com.github.jershell.shadcn

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.github.jershell.shadcn.services.AppService
import com.github.jershell.shadcn.containers.ShadcnUI
import com.github.jershell.shadcn.theme.LocalShadcnFonts
import com.github.jershell.shadcn.theme.Mode
import com.github.jershell.shadcn.ui.containers.root.RootLayout
import com.github.jershell.shadcn.ui.containers.root.RootLayoutViewModel
import com.github.jershell.shadcn.ui.navigation.Component
import com.github.jershell.shadcn.ui.navigation.Icons
import com.github.jershell.shadcn.ui.navigation.Overview
import com.github.jershell.shadcn.ui.navigation.Route
import com.github.jershell.shadcn.ui.screens.ComponentScreen
import com.github.jershell.shadcn.ui.screens.IconsScreen
import com.github.jershell.shadcn.ui.screens.OverviewScreen
import com.github.jershell.shadcn.ui.theme.LocalAppPresetState
import com.github.jershell.shadcn.ui.theme.PresetCodec
import com.github.jershell.shadcn.ui.theme.resolveAppFont
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

private val routeJson = Json {
    ignoreUnknownKeys = true
    isLenient = true
}

private fun Route.toSessionString(): String = routeJson.encodeToString(Route.serializer(), this)

private fun String.toRouteOrNull(): Route? = try {
    routeJson.decodeFromString(Route.serializer(), this)
} catch (e: Exception) {
    null
}

@Preview
@Composable
fun App(
    onModeChanged: @Composable (mode: Mode) -> Unit = {}
) {
    val navConfig = remember {
        SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclassesOfSealed(Route.serializer())
                }
            }
        }
    }

    val appService = koinInject<AppService>()
    val initialRoute = remember {
        appService.session.value.lastScreen?.toRouteOrNull() ?: Overview
    }
    val backStack = rememberNavBackStack(navConfig, initialRoute)
    val viewModel = koinViewModel<RootLayoutViewModel>()
    val isDark by viewModel.isDark.collectAsState()
    val mode = if (isDark) Mode.Dark else Mode.Light
    val appPresetState = remember {
        mutableStateOf(appService.session.value.lastPreset?.let { PresetCodec.decode(it) })
    }

    LaunchedEffect(appPresetState.value) {
        appService.setLastPreset(appPresetState.value?.let { PresetCodec.encode(it) })
    }

    val shadcnFonts = resolveAppFont(appPresetState.value?.font)

    CompositionLocalProvider(
        LocalAppPresetState provides appPresetState,
        LocalShadcnFonts provides shadcnFonts,
    ) {
        ShadcnUI(mode = mode, onModeChanged = onModeChanged, preset = appPresetState.value) {
            RootLayout(backStack) {
                LaunchedEffect(backStack.toList()) {
                    val current = backStack.lastOrNull() as? Route ?: Overview
                    appService.setLastScreen(current.toSessionString())
                }

                val entryProvider: (key: NavKey) -> NavEntry<NavKey> = entryProvider {
                    entry<Overview> { OverviewScreen() }
                    entry<Icons> { IconsScreen() }
                    entry<Component> { key -> ComponentScreen(key.id) }
//                entry<RouteD>(metadata = DialogSceneStrategy.dialog()){ ScreenD() }
                }

                NavDisplay(
                    backStack = backStack,
                    onBack = { backStack.removeLastOrNull() },
                    entryProvider = entryProvider,
                    // (add)
                    transitionSpec = {
                        fadeIn(animationSpec = tween(300)) togetherWith fadeOut(
                            animationSpec = tween(
                                300
                            )
                        )
                    },
                    // (pop)
                    popTransitionSpec = {
                        fadeIn(animationSpec = tween(300)) togetherWith fadeOut(
                            animationSpec = tween(
                                300
                            )
                        )
                    }
                )
            }
        }
    }
}
