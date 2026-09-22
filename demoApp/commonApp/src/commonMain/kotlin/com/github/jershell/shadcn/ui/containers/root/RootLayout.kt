package com.github.jershell.shadcn.ui.containers.root

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.composeunstyled.theme.Theme
import com.github.jershell.features.root.ToolBar
import com.github.jershell.shadcn.components.sidebar.Sidebar
import com.github.jershell.shadcn.components.sidebar.SidebarCollapsible
import com.github.jershell.shadcn.components.sidebar.SidebarProvider
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.ui.navigation.Component
import com.github.jershell.shadcn.ui.navigation.ComponentsRegistry
import com.github.jershell.shadcn.ui.navigation.Icons
import com.github.jershell.shadcn.ui.navigation.Overview
import com.github.jershell.shadcn.util.Log
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.root_layout_components
import com.github.jershell.shadcn.demoapp.generated.resources.root_layout_icons
import com.github.jershell.shadcn.demoapp.generated.resources.root_layout_overview
import com.github.jershell.shadcn.demoapp.generated.resources.root_layout_shadcn_for_compose
import com.github.jershell.shadcn.demoapp.generated.resources.root_layout_text_in_footer
import org.jetbrains.compose.resources.stringResource

@Composable
fun RootLayout(navStack: NavBackStack<NavKey>, content: @Composable () -> Unit) {
    val s_root_layout_components = stringResource(Res.string.root_layout_components)
    val s_root_layout_icons = stringResource(Res.string.root_layout_icons)
    val s_root_layout_overview = stringResource(Res.string.root_layout_overview)
    val registryEntries = ComponentsRegistry.all()
    val currentScreen = navStack.lastOrNull()

    LaunchedEffect(navStack.toList()) {
        navStack.forEachIndexed { idx, item ->
            Log.debug("Navstack: [$idx] ${item}")
        }
    }

    SidebarProvider {
        Row(Modifier.fillMaxSize().background(Theme[ColorProps][ColorTokens.sidebar]).safeDrawingPadding()) {
            // LeftSideBar
            Sidebar(
                modifier = Modifier.fillMaxHeight(),
                collapsible = SidebarCollapsible.Icon,
            ) {
                Header {
                    if (isExpanded) {
                        H4(stringResource(Res.string.root_layout_shadcn_for_compose))
                    }
                }
                Content {
                    Group {
                        Menu {
                            Item {
                                Button(
                                    label = s_root_layout_overview,
                                    onClick = {
                                        navStack.clear()
                                        navStack.add(Overview)
                                    },
                                    isSelected = currentScreen == Overview
                                )
                            }
                            Item {
                                Button(
                                    label = s_root_layout_icons,
                                    onClick = {
                                        navStack.clear()
                                        navStack.add(Icons)
                                    },
                                    isSelected = currentScreen == Icons
                                )
                            }
                        }
                    }
                    Separator()
                    Group {
                        Label(s_root_layout_components)
                        Menu {
                            registryEntries.forEach {
                                Item(key = it.key.id) {
                                    Button(
                                        label = it.value.name,
                                        isSelected = currentScreen is Component && it.key == currentScreen.id,
                                        onClick = {
                                            navStack.clear()
                                            navStack.add(Component(it.key))
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
                Footer {
                    if (isExpanded) {
                        H4(stringResource(Res.string.root_layout_text_in_footer))
                    }
                }
            }

            // WorkSpace
            Column(Modifier.fillMaxSize().weight(1f, fill = true)) {
                ToolBar()
                Box(Modifier.fillMaxSize().padding(16.dp)) {
                    content()
                }
            }
        }
    }
}

