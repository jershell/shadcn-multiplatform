package com.github.jershell.shadcn.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Moon
import com.composables.icons.lucide.Shuffle
import com.composables.icons.lucide.Sun
import com.composeunstyled.rememberScrollbarState
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.components.button.Button
import com.github.jershell.shadcn.components.button.ButtonIcon
import com.github.jershell.shadcn.components.button.ButtonSize
import com.github.jershell.shadcn.components.button.ButtonText
import com.github.jershell.shadcn.components.button.ButtonVariant
import com.github.jershell.shadcn.components.dialog.Dialog
import com.github.jershell.shadcn.components.dialog.DialogDescription
import com.github.jershell.shadcn.components.dialog.DialogFooter
import com.github.jershell.shadcn.components.dialog.DialogHeader
import com.github.jershell.shadcn.components.dialog.DialogTitle
import com.github.jershell.shadcn.components.input.Input
import com.github.jershell.shadcn.components.scroll.HorizontalScrollbar
import com.github.jershell.shadcn.components.scroll.VerticalScrollbar
import com.github.jershell.shadcn.components.select.Select
import com.github.jershell.shadcn.components.slider.Slider
import com.github.jershell.shadcn.components.textarea.Textarea
import com.github.jershell.shadcn.components.toast.Toast
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.Small
import com.github.jershell.shadcn.models.DataItem
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.ShadcnPreset
import com.github.jershell.shadcn.theme.themeScales
import com.github.jershell.shadcn.theme.themeStyles
import com.github.jershell.shadcn.ui.containers.root.RootLayoutViewModel
import com.github.jershell.shadcn.ui.screens.dashboard.DashboardScreen
import com.github.jershell.shadcn.ui.theme.LocalAppPresetState
import com.github.jershell.shadcn.ui.theme.PresetCodec
import com.github.jershell.shadcn.ui.theme.generatePresetCode
import com.github.jershell.shadcn.ui.theme.rememberAppFontFamilies
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.overview_apply
import com.github.jershell.shadcn.demoapp.generated.resources.overview_base_color
import com.github.jershell.shadcn.demoapp.generated.resources.overview_cancel
import com.github.jershell.shadcn.demoapp.generated.resources.overview_chart_color
import com.github.jershell.shadcn.demoapp.generated.resources.overview_close
import com.github.jershell.shadcn.demoapp.generated.resources.overview_default
import com.github.jershell.shadcn.demoapp.generated.resources.overview_font
import com.github.jershell.shadcn.demoapp.generated.resources.overview_get_code
import com.github.jershell.shadcn.demoapp.generated.resources.overview_get_preset
import com.github.jershell.shadcn.demoapp.generated.resources.overview_icon_library
import com.github.jershell.shadcn.demoapp.generated.resources.overview_inter
import com.github.jershell.shadcn.demoapp.generated.resources.overview_invalid_preset_code
import com.github.jershell.shadcn.demoapp.generated.resources.overview_kotlin_snippet_paste_it_into_another_app_and_pas
import com.github.jershell.shadcn.demoapp.generated.resources.overview_lucide
import com.github.jershell.shadcn.demoapp.generated.resources.overview_menu_style
import com.github.jershell.shadcn.demoapp.generated.resources.overview_mira_neutral_blue
import com.github.jershell.shadcn.demoapp.generated.resources.overview_nova_zinc_amber
import com.github.jershell.shadcn.demoapp.generated.resources.overview_open_preset
import com.github.jershell.shadcn.demoapp.generated.resources.overview_paste_preset_code_here
import com.github.jershell.shadcn.demoapp.generated.resources.overview_pick_an_example_preset_or_paste_a_preset_code
import com.github.jershell.shadcn.demoapp.generated.resources.overview_preset
import com.github.jershell.shadcn.demoapp.generated.resources.overview_radius
import com.github.jershell.shadcn.demoapp.generated.resources.overview_radius_2
import com.github.jershell.shadcn.demoapp.generated.resources.overview_reset
import com.github.jershell.shadcn.demoapp.generated.resources.overview_sera_stone_rose
import com.github.jershell.shadcn.demoapp.generated.resources.overview_share_string_of_the_current_theme_open_it_via_op
import com.github.jershell.shadcn.demoapp.generated.resources.overview_shuffle
import com.github.jershell.shadcn.demoapp.generated.resources.overview_theme
import com.github.jershell.shadcn.demoapp.generated.resources.overview_toggle_theme
import com.github.jershell.shadcn.demoapp.generated.resources.overview_vega_slate_emerald
import kotlin.random.Random
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OverviewScreen() {
    val presetState = LocalAppPresetState.current
    val preset by presetState
    val rootViewModel = koinViewModel<RootLayoutViewModel>()
    val isDark by rootViewModel.isDark.collectAsState()

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme[ColorProps][ColorTokens.background]),
    ) {
        CustomizerPanel(
            preset = preset,
            onPresetChange = { presetState.value = it },
            onToggleMode = { rootViewModel.setIsDark(!isDark) },
            modifier = Modifier
                .width(360.dp)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
        ) {
            // dashboard preview: vertical + horizontal scrolling with scrollbars
            // (like the reference create preview); content keeps an intrinsic width
            // so the horizontal scrollbar appears on narrow windows
            val verticalState = rememberScrollState()
            val horizontalState = rememberScrollState()
            BoxWithConstraints {
                // guard: inside a horizontal scroll container maxWidth is infinite —
                // never propagate Dp.Infinity into the content width
                val contentWidth = if (maxWidth != Dp.Infinity) {
                    maxOf(maxWidth, 1100.dp)
                } else {
                    1100.dp
                }
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(modifier = Modifier.weight(1f, fill = true)) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .verticalScroll(verticalState)
                                .horizontalScroll(horizontalState),
                        ) {
                        Box(modifier = Modifier.width(contentWidth).padding(16.dp)) {
                            DashboardScreen(preset)
                        }
                        }
                        VerticalScrollbar(scrollbarState = rememberScrollbarState(verticalState))
                    }
                    HorizontalScrollbar(scrollbarState = rememberScrollbarState(horizontalState))
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CustomizerPanel(
    preset: ShadcnPreset?,
    onPresetChange: (ShadcnPreset?) -> Unit,
    onToggleMode: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val s_overview_default = stringResource(Res.string.overview_default)
    val rootViewModel = koinViewModel<RootLayoutViewModel>()
    val isDark by rootViewModel.isDark.collectAsState()
    var openPresetDialog by remember { mutableStateOf(false) }
    var getPresetDialog by remember { mutableStateOf(false) }
    var getCodeDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            H4(stringResource(Res.string.overview_theme))
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = onToggleMode,
                variant = ButtonVariant.Outline,
                size = ButtonSize.Icon,
                content = { ButtonIcon(imageVector = if (isDark) Lucide.Moon else Lucide.Sun, contentDescription = stringResource(Res.string.overview_toggle_theme)) },
            )
            val s_overview_inter = stringResource(Res.string.overview_inter)
            Button(onClick = { onPresetChange(shuffledPreset(s_overview_inter)) }, variant = ButtonVariant.Outline) {
                ButtonIcon(imageVector = Lucide.Shuffle, contentDescription = stringResource(Res.string.overview_shuffle))
                ButtonText(stringResource(Res.string.overview_shuffle))
            }
        }

        PanelSection(stringResource(Res.string.overview_menu_style)) {
            val styleItems = remember {
                listOf(s_overview_default) + themeStyles.map { it.name.replaceFirstChar { c -> c.uppercaseChar() } }
            }
            val selectedStyle = preset?.style?.replaceFirstChar { c -> c.uppercaseChar() } ?: "Default"
            PickerSelect(
                label = stringResource(Res.string.overview_menu_style),
                items = styleItems,
                selected = selectedStyle,
                onSelect = { value ->
                    onPresetChange(
                        (preset ?: ShadcnPreset()).copy(
                            style = value.lowercase().takeIf { it != "default" },
                        ),
                    )
                },
            )
        }

        PanelSection(stringResource(Res.string.overview_base_color)) {
            ScaleSwatches(
                names = listOf("neutral") + themeScales.map { it.name },
                selected = preset?.baseColor ?: "neutral",
                onSelect = { name -> onPresetChange((preset ?: ShadcnPreset()).copy(baseColor = name)) },
            )
        }

        PanelSection(stringResource(Res.string.overview_theme)) {
            ScaleSwatches(
                names = themeScales.map { it.name },
                selected = preset?.theme ?: (preset?.baseColor ?: "neutral"),
                onSelect = { name -> onPresetChange((preset ?: ShadcnPreset()).copy(theme = name)) },
            )
        }

        PanelSection(stringResource(Res.string.overview_chart_color)) {
            ScaleSwatches(
                names = themeScales.map { it.name },
                selected = preset?.chartColor ?: (preset?.theme ?: (preset?.baseColor ?: "neutral")),
                onSelect = { name -> onPresetChange((preset ?: ShadcnPreset()).copy(chartColor = name)) },
            )
        }

        PanelSection(stringResource(Res.string.overview_font)) {
            val fontFamilyNames = rememberAppFontFamilies().keys.toList()
            PickerSelect(
                label = stringResource(Res.string.overview_font),
                items = fontFamilyNames,
                selected = preset?.font ?: "System",
                onSelect = { name ->
                    onPresetChange((preset ?: ShadcnPreset()).copy(font = name))
                },
            )
        }

        PanelSection(stringResource(Res.string.overview_icon_library)) {
            PickerSelect(
                label = stringResource(Res.string.overview_icon_library),
                items = listOf(stringResource(Res.string.overview_lucide), "Tabler"),
                selected = (preset?.iconLibrary ?: "lucide").replaceFirstChar { c -> c.uppercaseChar() },
                onSelect = { value ->
                    onPresetChange((preset ?: ShadcnPreset()).copy(iconLibrary = value.lowercase()))
                },
            )
        }

        PanelSection(stringResource(Res.string.overview_radius)) {
            RadiusSlider(preset = preset, onPresetChange = onPresetChange)
        }

        PanelSection(stringResource(Res.string.overview_preset)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { openPresetDialog = true }, variant = ButtonVariant.Outline) {
                    ButtonText(stringResource(Res.string.overview_open_preset))
                }
                Button(onClick = { getPresetDialog = true }, variant = ButtonVariant.Outline) {
                    ButtonText(stringResource(Res.string.overview_get_preset))
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { getCodeDialog = true }, variant = ButtonVariant.Outline) {
                    ButtonText(stringResource(Res.string.overview_get_code))
                }
                Button(onClick = { onPresetChange(null) }, variant = ButtonVariant.Ghost) {
                    ButtonText(stringResource(Res.string.overview_reset))
                }
            }
        }
    }

    if (openPresetDialog) {
        OpenPresetDialog(
            onApply = { value ->
                onPresetChange(value)
                openPresetDialog = false
            },
            onDismiss = { openPresetDialog = false },
        )
    }

    if (getPresetDialog) {
        GetPresetDialog(
            preset = preset,
            onDismiss = { getPresetDialog = false },
        )
    }

    if (getCodeDialog) {
        PresetCodeDialog(
            preset = preset,
            onDismiss = { getCodeDialog = false },
        )
    }
}

@Composable
private fun PanelSection(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Small(title)
        content()
    }
}

@Composable
private fun PickerSelect(
    label: String,
    items: List<String>,
    selected: String,
    onSelect: (String) -> Unit,
) {
    val dataItems = items.mapIndexed { i, name -> DataItem(i, name, name) }
    val selectedKey = dataItems.firstOrNull { it.title == selected }?.key
    Select(
        items = dataItems,
        selected = setOfNotNull(selectedKey),
        onSelectedChange = { keys ->
            dataItems.firstOrNull { it.key in keys }?.data?.let(onSelect)
        },
        placeholder = label,
        modifier = Modifier.fillMaxWidth(),
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ScaleSwatches(
    names: List<String>,
    selected: String,
    onSelect: (String) -> Unit,
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        names.forEach { name ->
            ScaleSwatch(
                name = name,
                selected = name == selected,
                onClick = { onSelect(name) },
            )
        }
    }
}

@Composable
private fun ScaleSwatch(
    name: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val scale = themeScales.firstOrNull { it.name == name }
    val swatchColor = scale?.get(5) ?: Theme[ColorProps][ColorTokens.primary]
    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(swatchColor)
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = if (selected) {
                    Theme[ColorProps][ColorTokens.foreground]
                } else {
                    Theme[ColorProps][ColorTokens.border]
                },
                shape = CircleShape,
            )
            .clickable(onClick = onClick),
    )
}

@Composable
private fun RadiusSlider(
    preset: ShadcnPreset?,
    onPresetChange: (ShadcnPreset?) -> Unit,
) {
    val currentRadius = preset?.radius ?: 0.625
    var radiusValue by remember(currentRadius) { mutableStateOf(currentRadius.toFloat()) }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Slider(
            value = radiusValue,
            onValueChange = { radiusValue = it },
            onValueChangeFinished = {
                onPresetChange((preset ?: ShadcnPreset()).copy(radius = radiusValue.toDouble()))
            },
            valueRange = 0f..1.0f,
        )
        Small(stringResource(Res.string.overview_radius_2) + radiusValue.toString().take(5) + " rem")
    }
}

private fun shuffledPreset(interFont: String): ShadcnPreset = ShadcnPreset(
    style = themeStyles[Random.nextInt(themeStyles.size)].name,
    baseColor = listOf("neutral", "gray", "zinc", "stone", "slate")[Random.nextInt(5)],
    theme = themeScales[Random.nextInt(themeScales.size)].name,
    chartColor = null,
    font = listOf(interFont, "Noto Sans", "Roboto", "System")[Random.nextInt(4)],
    iconLibrary = "lucide",
    radius = listOf(0.0, 0.25, 0.5, 0.625, 0.75, 1.0)[Random.nextInt(6)],
)

@Composable
private fun presetLibrary(): List<Pair<String, ShadcnPreset>> = listOf(
    stringResource(Res.string.overview_default) to ShadcnPreset(),
    stringResource(Res.string.overview_nova_zinc_amber) to ShadcnPreset(
        style = "nova", baseColor = "zinc", theme = "amber", radius = 0.625,
    ),
    stringResource(Res.string.overview_mira_neutral_blue) to ShadcnPreset(
        style = "mira", baseColor = "neutral", theme = "blue", radius = 0.5,
    ),
    stringResource(Res.string.overview_vega_slate_emerald) to ShadcnPreset(
        style = "vega", baseColor = "slate", theme = "emerald", radius = 0.75,
    ),
    stringResource(Res.string.overview_sera_stone_rose) to ShadcnPreset(
        style = "sera", baseColor = "stone", theme = "rose", radius = 0.5,
    ),
)

@Composable
private fun OpenPresetDialog(
    onApply: (ShadcnPreset?) -> Unit,
    onDismiss: () -> Unit,
) {
    val s_overview_invalid_preset_code = stringResource(Res.string.overview_invalid_preset_code)
    val importState = rememberTextFieldState()
    Dialog(
        open = true,
        onOpenChange = { if (!it) onDismiss() },
    ) {
        DialogHeader {
            DialogTitle(stringResource(Res.string.overview_open_preset))
            DialogDescription(stringResource(Res.string.overview_pick_an_example_preset_or_paste_a_preset_code))
        }
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            presetLibrary().forEach { (name, preset) ->
                Button(
                    onClick = { onApply(preset) },
                    variant = ButtonVariant.Outline,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    ButtonText(name)
                }
            }
        }
        Input(
            state = importState,
            placeholder = stringResource(Res.string.overview_paste_preset_code_here),
            modifier = Modifier.fillMaxWidth(),
        )
        DialogFooter {
            Button(onClick = onDismiss, variant = ButtonVariant.Outline) {
                ButtonText(stringResource(Res.string.overview_cancel))
            }
            Button(onClick = {
                val decoded = PresetCodec.decode(importState.text.toString())
                if (decoded != null) {
                    onApply(decoded)
                } else {
                    Toast(s_overview_invalid_preset_code)
                }
            }) {
                ButtonText(stringResource(Res.string.overview_apply))
            }
        }
    }
}

/**
 * `Get Preset` dialog: the share string of the current preset (zip+base64).
 */
@Composable
private fun GetPresetDialog(
    preset: ShadcnPreset?,
    onDismiss: () -> Unit,
) {
    val code = remember(preset) { preset?.let { PresetCodec.encode(it) } ?: "" }
    val codeState = rememberTextFieldState(code)
    Dialog(
        open = true,
        onOpenChange = { if (!it) onDismiss() },
    ) {
        DialogHeader {
            DialogTitle(stringResource(Res.string.overview_get_preset))
            DialogDescription(stringResource(Res.string.overview_share_string_of_the_current_theme_open_it_via_op))
        }
        Input(
            state = codeState,
            modifier = Modifier.fillMaxWidth(),
        )
        DialogFooter {
            Button(onClick = onDismiss, variant = ButtonVariant.Outline) {
                ButtonText(stringResource(Res.string.overview_close))
            }
        }
    }
}

/**
 * `Get Code` dialog: ready-to-paste Kotlin snippet with the fully resolved
 * light/dark palettes, so another app depending on the uikit can render the
 * identical theme.
 */
@Composable
private fun PresetCodeDialog(
    preset: ShadcnPreset?,
    onDismiss: () -> Unit,
) {
    val code = remember(preset) { generatePresetCode(preset) }
    val codeState = rememberTextFieldState(code)
    Dialog(
        open = true,
        onOpenChange = { if (!it) onDismiss() },
    ) {
        DialogHeader {
            DialogTitle(stringResource(Res.string.overview_get_code))
            DialogDescription(stringResource(Res.string.overview_kotlin_snippet_paste_it_into_another_app_and_pas))
        }
        Textarea(
            state = codeState,
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
        )
        DialogFooter {
            Button(onClick = onDismiss, variant = ButtonVariant.Outline) {
                ButtonText(stringResource(Res.string.overview_close))
            }
        }
    }
}
