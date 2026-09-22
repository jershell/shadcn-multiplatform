package com.github.jershell.shadcn.components.tabs

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.composeunstyled.Tab
import com.composeunstyled.TabList
import com.composeunstyled.TabListScope
import com.composeunstyled.UnstyledTabGroup
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.models.DataItem
import com.github.jershell.shadcn.theme.DimProps
import com.github.jershell.shadcn.theme.DimTokens
import com.github.jershell.shadcn.theme.TwDimensions
import com.github.jershell.shadcn.theme.TypographyStyles

private data class TabLayout(
    val position: Offset,
    val size: IntSize,
)

/**
 * A tab group styled after shadcn/ui.
 *
 * Tabs are defined by [DataItem] values. Selection is tracked by the item's [DataItem.key].
 * Because [panelContent] defaults to an empty lambda, the component can also be used as a
 * radio-like list of options without panels.
 *
 * The selected tab background is rendered as a separate animated indicator behind the tabs.
 * It smoothly animates both position and size using [Animatable], similar to the original
 * HeroUI implementation.
 *
 * @param items The tab options.
 * @param selectedKey The key of the currently selected tab.
 * @param onSelectedChange Called when the selected tab changes.
 * @param orientation Orientation of the tab list.
 * @param enabled Whether the tab group is interactive.
 * @param tabContent Custom renderer for a tab trigger. Receives the item and its selection state.
 * @param panelContent Renderer for a tab panel. Receives the item whose key is currently selected.
 */
@Composable
fun <T> Tabs(
    items: List<DataItem<T>>,
    selectedKey: Int,
    onSelectedChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    orientation: Orientation = Orientation.Horizontal,
    enabled: Boolean = true,
    tabContent: @Composable (item: DataItem<T>, selected: Boolean) -> Unit = { item, selected ->
        DefaultTabTitle(item = item, selected = selected, enabled = enabled && item.enabled)
    },
    panelContent: @Composable (item: DataItem<T>) -> Unit = {},
) {
    val colors = resolveTabsColors()
    val listRadius = Theme[DimProps][DimTokens.radiusLg]
    val listShape = RoundedCornerShape(listRadius)
    val tabRadius = Theme[DimProps][DimTokens.radiusMd]
    val tabShape = RoundedCornerShape(tabRadius)
    val tabPaddingHorizontal = TwDimensions.paddingPxToken3
    val tabPaddingVertical = TwDimensions.paddingPxToken1
    val tabLayouts = remember { mutableStateMapOf<Int, TabLayout>() }
    val highlightPosition = remember { Animatable(Offset.Zero, Offset.VectorConverter) }
    val highlightSize = remember { Animatable(IntSize.Zero, IntSize.VectorConverter) }
    val selectedLayout = tabLayouts[selectedKey]
    val positionSpring = remember { spring<Offset>(stiffness = 500f, dampingRatio = 0.75f) }
    val sizeSpring = remember { spring<IntSize>(stiffness = 500f, dampingRatio = 0.75f) }
    var isInitial by remember { mutableStateOf(true) }

    LaunchedEffect(selectedKey, selectedLayout) {
        val target = selectedLayout ?: return@LaunchedEffect
        if (isInitial) {
            highlightPosition.snapTo(target.position)
            highlightSize.snapTo(target.size)
            isInitial = false
        } else {
            coroutineScope {
                launch { highlightPosition.animateTo(target.position, positionSpring) }
                launch { highlightSize.animateTo(target.size, sizeSpring) }
            }
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken2),
    ) {
        UnstyledTabGroup(
            selectedTab = selectedKey,
            onSelectedTabChange = { onSelectedChange(it) },
            tabs = items.map { it.key },
        ) {
            TabList(
                modifier = Modifier
                    .wrapContentSize()
                    .clip(listShape)
                    .background(colors.listBackground, listShape)
                    .padding(TwDimensions.paddingPxToken1),
                orientation = orientation,
            ) {
                val tabListScope: TabListScope<Int> = this
                val gap = TwDimensions.gapGapToken1

                Box {
                    if (selectedLayout != null) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .offset {
                                    IntOffset(
                                        highlightPosition.value.x.toInt(),
                                        highlightPosition.value.y.toInt(),
                                    )
                                }
                                .size(
                                    width = with(LocalDensity.current) { highlightSize.value.width.toDp() },
                                    height = with(LocalDensity.current) { highlightSize.value.height.toDp() },
                                )
                                .clip(tabShape)
                                .background(colors.activeBackground),
                        )
                    }

                    when (orientation) {
                        Orientation.Horizontal -> Row(
                            horizontalArrangement = Arrangement.spacedBy(gap),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            items.forEach { item ->
                                TabTrigger(
                                    item = item,
                                    selectedKey = selectedKey,
                                    enabled = enabled,
                                    tabShape = tabShape,
                                    tabPaddingHorizontal = tabPaddingHorizontal,
                                    tabPaddingVertical = tabPaddingVertical,
                                    tabContent = tabContent,
                                    tabListScope = tabListScope,
                                    tabLayouts = tabLayouts,
                                    orientation = orientation,
                                )
                            }
                        }

                        Orientation.Vertical -> Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(gap),
                            horizontalAlignment = Alignment.Start,
                        ) {
                            items.forEach { item ->
                                TabTrigger(
                                    item = item,
                                    selectedKey = selectedKey,
                                    enabled = enabled,
                                    tabShape = tabShape,
                                    tabPaddingHorizontal = tabPaddingHorizontal,
                                    tabPaddingVertical = tabPaddingVertical,
                                    tabContent = tabContent,
                                    tabListScope = tabListScope,
                                    tabLayouts = tabLayouts,
                                    orientation = orientation,
                                )
                            }
                        }
                    }
                }
            }
        }

        val selectedItem = items.firstOrNull { it.key == selectedKey }
        if (selectedItem != null) {
            panelContent(selectedItem)
        }
    }
}

@Composable
private fun <T> TabTrigger(
    item: DataItem<T>,
    selectedKey: Int,
    enabled: Boolean,
    tabShape: RoundedCornerShape,
    tabPaddingHorizontal: Dp,
    tabPaddingVertical: Dp,
    tabContent: @Composable (item: DataItem<T>, selected: Boolean) -> Unit,
    tabListScope: TabListScope<Int>,
    tabLayouts: SnapshotStateMap<Int, TabLayout>,
    orientation: Orientation,
) {
    val selected = item.key == selectedKey
    val itemEnabled = enabled && item.enabled

    with(tabListScope) {
        Tab(
            key = item.key,
            enabled = itemEnabled,
            indication = null,
            modifier = Modifier
                .alpha(if (itemEnabled) 1f else 0.5f)
                .then(if (orientation == Orientation.Vertical) Modifier.fillMaxWidth() else Modifier)
                .clip(tabShape)
                .onPlaced { coordinates ->
                    tabLayouts[item.key] = TabLayout(
                        position = coordinates.positionInParent(),
                        size = coordinates.size,
                    )
                },
        ) {
            Box(
                modifier = Modifier.padding(
                    horizontal = tabPaddingHorizontal,
                    vertical = tabPaddingVertical,
                ),
                contentAlignment = Alignment.Center,
            ) {
                tabContent(item, selected)
            }
        }
    }
}

@Composable
private fun DefaultTabTitle(
    item: DataItem<*>,
    selected: Boolean,
    enabled: Boolean,
) {
    val colors = resolveTabsColors()
    val color = when {
        !enabled -> colors.disabledContent
        selected -> colors.activeContent
        else -> colors.inactiveContent
    }

    BasicText(
        text = item.title,
        style = TypographyStyles.textSmMedium.copy(color = color),
    )
}
