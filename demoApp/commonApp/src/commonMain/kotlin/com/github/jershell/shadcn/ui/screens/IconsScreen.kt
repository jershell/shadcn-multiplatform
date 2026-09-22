package com.github.jershell.shadcn.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composeunstyled.Text
import com.composeunstyled.UnstyledButton
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.components.typography.H4
import com.github.jershell.shadcn.components.typography.Muted
import com.github.jershell.shadcn.components.typography.Small
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.DimProps
import com.github.jershell.shadcn.theme.DimTokens
import com.github.jershell.shadcn.theme.TypographyStyles
import com.github.jershell.shadcn.ui.icons.IconCatalog
import com.github.jershell.shadcn.ui.icons.IconEntry
import com.github.jershell.shadcn.ui.icons.IconSetInfo
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.icons_icons
import com.github.jershell.shadcn.demoapp.generated.resources.icons_loading_icons
import com.github.jershell.shadcn.demoapp.generated.resources.icons_loading_x
import com.github.jershell.shadcn.demoapp.generated.resources.icons_search_icons
import com.github.jershell.shadcn.demoapp.generated.resources.icons_x_icons_in_x
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.stringResource

@Composable
fun IconsScreen() {
    val s_icons_loading_x = stringResource(Res.string.icons_loading_x)
    val s_icons_x_icons_in_x = stringResource(Res.string.icons_x_icons_in_x)
    val iconSets = remember { IconCatalog.sets }
    var selectedSetId by remember { mutableStateOf(iconSets.first().id) }
    var query by remember { mutableStateOf("") }

    val selectedSet = remember(selectedSetId, iconSets) {
        iconSets.firstOrNull { it.id == selectedSetId } ?: iconSets.first()
    }
    var icons by remember { mutableStateOf<List<IconEntry>?>(null) }
    val iconsCache = remember { mutableMapOf<String, List<IconEntry>>() }

    LaunchedEffect(selectedSetId) {
        icons = iconsCache[selectedSetId] ?: withContext(Dispatchers.Default) {
            IconCatalog.iconsFor(selectedSetId).also { loaded ->
                iconsCache[selectedSetId] = loaded
            }
        }
    }

    val loadedIcons = icons.orEmpty()
    val filteredIcons = remember(loadedIcons, query) {
        val normalizedQuery = query.trim().lowercase()
        if (normalizedQuery.isEmpty()) {
            loadedIcons
        } else {
            loadedIcons.filter { icon ->
                icon.copyName.lowercase().contains(normalizedQuery) ||
                    icon.slugName.lowercase().contains(normalizedQuery)
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        H4(stringResource(Res.string.icons_icons))

        IconSetSelector(
            sets = iconSets,
            selectedId = selectedSet.id,
            onSelected = { selectedSetId = it },
        )

        IconSearchField(
            value = query,
            onValueChange = { query = it },
            placeholder = stringResource(Res.string.icons_search_icons),
        )

        Muted(
            text = when {
                icons == null -> stringResource(Res.string.icons_loading_x, selectedSet.label)
                else -> stringResource(Res.string.icons_x_icons_in_x, filteredIcons.size, selectedSet.label)
            },
        )

        if (icons == null) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Muted(stringResource(Res.string.icons_loading_icons))
            }
        } else {
            IconViewer(
                icons = filteredIcons,
                copyPrefix = selectedSet.copyPrefix,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun IconSetSelector(
    sets: List<IconSetInfo>,
    selectedId: String,
    onSelected: (String) -> Unit,
) {
    val radius = Theme[DimProps][DimTokens.radiusMd]
    val borderWidth = Theme[DimProps][DimTokens.borderWidth]
    val borderColor = Theme[ColorProps][ColorTokens.border]
    val accent = Theme[ColorProps][ColorTokens.accent]
    val accentForeground = Theme[ColorProps][ColorTokens.accentForeground]
    val muted = Theme[ColorProps][ColorTokens.muted]
    val foreground = Theme[ColorProps][ColorTokens.foreground]
    val shape = RoundedCornerShape(radius)

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        sets.forEach { set ->
            val selected = set.id == selectedId
            UnstyledButton(
                onClick = { onSelected(set.id) },
                modifier = Modifier
                    .border(borderWidth, borderColor, shape)
                    .background(
                        color = if (selected) accent else muted,
                        shape = shape,
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                indication = null,
            ) {
                Small(
                    text = set.label,
                    color = if (selected) accentForeground else foreground,
                )
            }
        }
    }
}

@Composable
private fun IconSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
) {
    val radius = Theme[DimProps][DimTokens.radiusMd]
    val borderWidth = Theme[DimProps][DimTokens.borderWidth]
    val borderColor = Theme[ColorProps][ColorTokens.border]
    val background = Theme[ColorProps][ColorTokens.background]
    val foreground = Theme[ColorProps][ColorTokens.foreground]
    val mutedForeground = Theme[ColorProps][ColorTokens.mutedForeground]
    val shape = RoundedCornerShape(radius)

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .border(borderWidth, borderColor, shape)
            .background(background, shape)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        textStyle = TypographyStyles.textSmRegular.copy(color = foreground),
        cursorBrush = SolidColor(foreground),
        singleLine = true,
        decorationBox = { innerTextField ->
            Box(contentAlignment = Alignment.CenterStart) {
                if (value.isEmpty()) {
                    Muted(text = placeholder, color = mutedForeground)
                }
                innerTextField()
            }
        },
    )
}

@Composable
fun IconViewer(
    icons: List<IconEntry>,
    copyPrefix: String,
    modifier: Modifier = Modifier,
) {
    val gridState = rememberLazyGridState()

    LazyVerticalGrid(
        columns = GridCells.FixedSize(size = 112.dp),
        state = gridState,
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        items(
            items = icons,
            key = { it.copyName },
        ) { icon ->
            IconView(
                icon = icon,
                copyPrefix = copyPrefix,
            )
        }
    }
}

@Composable
fun IconView(
    icon: IconEntry,
    copyPrefix: String,
) {
    val clipboardManager = LocalClipboardManager.current
    val radius = Theme[DimProps][DimTokens.radiusMd]
    val borderWidth = Theme[DimProps][DimTokens.borderWidth]
    val borderColor = Theme[ColorProps][ColorTokens.border]
    val background = Theme[ColorProps][ColorTokens.background]
    val foreground = Theme[ColorProps][ColorTokens.foreground]
    val mutedForeground = Theme[ColorProps][ColorTokens.mutedForeground]
    val shape = RoundedCornerShape(radius)
    val fullName = remember(icon.copyName, copyPrefix) {
        "$copyPrefix.${icon.copyName}"
    }
    val vector = remember(icon.copyName) { icon.vector() }

    UnstyledButton(
        onClick = { clipboardManager.setText(AnnotatedString(fullName)) },
        modifier = Modifier
            .fillMaxWidth()
            .height(112.dp)
            .border(borderWidth, borderColor, shape)
            .background(background, shape)
            .padding(start = 6.dp, end = 6.dp, top = 8.dp, bottom = 4.dp),
        indication = null,
    ) {
        Column(
//            modifier = Modifier.size()
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Image(
                painter = rememberVectorPainter(vector),
                contentDescription = icon.slugName,
                modifier = Modifier.size(48.dp),
                colorFilter = ColorFilter.tint(foreground)
            )

            Box(
                modifier = Modifier.fillMaxSize().weight(1f, fill = true),
                contentAlignment = Alignment.Center,
                content = {
                    Text(
                        modifier = Modifier,
                        text = icon.copyName,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = TypographyStyles.textSmLight.copy(
                            color = mutedForeground,
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp
                        )
                    )
                })

        }
    }
}
