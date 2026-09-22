package com.github.jershell.shadcn.components.sidebar

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composeunstyled.FocusVisibilityProvider
import com.composeunstyled.UnstyledHorizontalSeparator
import com.composeunstyled.rememberScrollbarState
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.components.scroll.VerticalScrollbar
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.DimProps
import com.github.jershell.shadcn.theme.DimTokens

@Composable
fun Sidebar(
    modifier: Modifier = Modifier,
    side: SidebarSide = SidebarSide.Left,
    variant: SidebarVariant = SidebarVariant.Sidebar,
    collapsible: SidebarCollapsible = SidebarCollapsible.None,
    expanded: Boolean? = null,
    defaultExpanded: Boolean = true,
    onExpandedChange: (Boolean) -> Unit = {},
    showRail: Boolean = true,
    content: @DisallowComposableCalls SidebarScope.() -> Unit,
) {
    val sidebarNodes = rememberSidebarNodes(content)

    val providedController = LocalSidebarController.current
    val (currentExpanded, onChange) = if (providedController != null) {
        // The SidebarProvider owns the state: local expanded/defaultExpanded/onExpandedChange are ignored
        val route = remember(providedController) {
            { value: Boolean -> providedController.setExpanded(value) }
        }
        providedController.isExpanded to route
    } else {
        rememberSidebarExpandedState(
            expanded = expanded,
            defaultExpanded = defaultExpanded,
            onExpandedChange = onExpandedChange,
        )
    }
    val controller = remember(currentExpanded, side, onChange) {
        SidebarController(
            isExpanded = currentExpanded,
            side = side,
            onExpandedChange = onChange,
        )
    }
    val sidebarState = remember(controller, collapsible, variant) {
        SidebarState(
            controller = controller,
            collapsible = collapsible,
            variant = variant,
        )
    }

    val targetWidth = when (collapsible) {
        SidebarCollapsible.Icon -> if (currentExpanded) SidebarDefaults.Width else SidebarDefaults.WidthIcon
        SidebarCollapsible.Offcanvas -> if (currentExpanded) SidebarDefaults.Width else 0.dp
        SidebarCollapsible.None -> SidebarDefaults.Width
    }
    val animatedWidth by animateDpAsState(targetWidth)

    val sidebarColor = Theme[ColorProps][ColorTokens.sidebar]
    val sidebarBorder = Theme[ColorProps][ColorTokens.sidebarBorder]
    val radiusMd = Theme[DimProps][DimTokens.radiusMd]
    val borderWidth = Theme[DimProps][DimTokens.borderWidth]

    FocusVisibilityProvider {
        CompositionLocalProvider(
            LocalSidebarState provides sidebarState,
            LocalSidebarController provides controller,
        ) {
            Box(
                modifier = modifier
                    .width(animatedWidth)
                    .fillMaxHeight()
                    .then(
                        modifier.sidebarContainerModifier(
                            variant = variant,
                            side = side,
                            sidebarColor = sidebarColor,
                            sidebarBorder = sidebarBorder,
                            radiusMd = radiusMd,
                            borderWidth = borderWidth,
                        ),
                    ),
            ) {
                if (!sidebarState.isOffcanvasHidden) {
                    Column(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
                        RenderSidebarNodes(
                            nodes = sidebarNodes,
                            columnScope = this,
                        )
                    }
                }
                if (showRail && collapsible != SidebarCollapsible.None) {
                    SidebarRail(
                        controller = controller,
                        modifier = Modifier
                            .align(if (side == SidebarSide.Left) Alignment.CenterEnd else Alignment.CenterStart)
                            .offset(
                                x = if (side == SidebarSide.Left) {
                                    SidebarDefaults.RailWidth / 2
                                } else {
                                    -SidebarDefaults.RailWidth / 2
                                },
                            ),
                    )
                }
            }
        }
    }
}

private fun Modifier.sidebarContainerModifier(
    variant: SidebarVariant,
    side: SidebarSide,
    sidebarColor: Color,
    sidebarBorder: Color,
    radiusMd: Dp,
    borderWidth: Dp,
): Modifier = when (variant) {
    SidebarVariant.Floating -> this
        .padding(SidebarDefaults.ContentPadding)
        .shadow(2.dp, RoundedCornerShape(radiusMd))
        .clip(RoundedCornerShape(radiusMd))
        .border(borderWidth, sidebarBorder, RoundedCornerShape(radiusMd))
        .background(sidebarColor)

    SidebarVariant.Inset -> this
        .padding(SidebarDefaults.ContentPadding)
        .clip(RoundedCornerShape(radiusMd))
        .background(sidebarColor)

    SidebarVariant.Sidebar -> this
        .background(sidebarColor)
        .sidebarEdgeBorder(side, sidebarBorder, borderWidth)
}

private fun Modifier.sidebarEdgeBorder(side: SidebarSide, color: Color, width: Dp): Modifier = drawBehind {
    val x = if (side == SidebarSide.Left) size.width - width.toPx() else 0f
    drawLine(
        color = color,
        start = Offset(x, 0f),
        end = Offset(x, size.height),
        strokeWidth = width.toPx(),
    )
}

@Composable
private fun SidebarSection(
    modifier: Modifier = Modifier,
    block: @Composable SidebarSectionScope.() -> Unit,
) {
    val sidebarState = requireSidebarState()
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(SidebarDefaults.ContentPadding),
        verticalArrangement = Arrangement.spacedBy(SidebarDefaults.SectionGap),
        content = {
            SidebarSectionScopeImpl(this, sidebarState).block()
        },
    )
}

@Composable
internal fun SidebarHeader(
    modifier: Modifier = Modifier,
    block: @Composable SidebarSectionScope.() -> Unit,
) = SidebarSection(modifier, block)

@Composable
internal fun SidebarFooter(
    modifier: Modifier = Modifier,
    block: @Composable SidebarSectionScope.() -> Unit,
) = SidebarSection(modifier, block)

@Composable
internal fun ColumnScope.SidebarContent(
    modifier: Modifier = Modifier,
    block: @Composable () -> Unit,
) {
    val sidebarState = requireSidebarState()
    if (sidebarState.isIconCollapsed) {
        Column(
            modifier = modifier
                .weight(1f, fill = true)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(SidebarDefaults.SectionGap),
            content = { block() },
        )
    } else {
        // expanded: content scrolls on its own with the shadcn scrollbar
        val scrollState = rememberScrollState()
        val scrollbarState = rememberScrollbarState(scrollState)
        Row(
            modifier = modifier
                .weight(1f, fill = true)
                .fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier
                    .weight(1f, fill = true)
                    .fillMaxHeight()
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(SidebarDefaults.SectionGap),
                content = { block() },
            )
            VerticalScrollbar(scrollbarState = scrollbarState)
        }
    }
}

@Composable
internal fun SidebarGroup(
    modifier: Modifier = Modifier,
    block: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(SidebarDefaults.ContentPadding),
        verticalArrangement = Arrangement.spacedBy(SidebarDefaults.MenuGap),
        content = { block() },
    )
}

@Composable
internal fun SidebarGroupLabel(
    text: String,
    modifier: Modifier = Modifier,
) {
    val sidebarState = requireSidebarState()
    if (sidebarState.isIconCollapsed) return

    val radiusMd = Theme[DimProps][DimTokens.radiusMd]

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(SidebarDefaults.GroupLabelHeight)
            .clip(RoundedCornerShape(radiusMd))
            .padding(horizontal = SidebarDefaults.ContentPadding),
        contentAlignment = Alignment.CenterStart,
    ) {
        SidebarText(text = text, style = SidebarTextStyle.GroupLabel)
    }
}

@Composable
internal fun SidebarGroupAction(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String = "+",
) {
    val sidebarState = requireSidebarState()
    if (sidebarState.isIconCollapsed) return

    val radiusMd = Theme[DimProps][DimTokens.radiusMd]

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterEnd,
    ) {
        SidebarActionButton(
            onClick = onClick,
            enabled = enabled,
            modifier = Modifier
                .height(20.dp)
                .width(20.dp),
            shape = RoundedCornerShape(radiusMd),
            content = {
                SidebarText(text = label, style = SidebarTextStyle.Action)
            },
        )
    }
}

@Composable
internal fun SidebarComposeItem(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        content = { content() },
    )
}

@Composable
internal fun SidebarSeparator(modifier: Modifier = Modifier) {
    val sidebarBorder = Theme[ColorProps][ColorTokens.sidebarBorder]
    val borderWidth = Theme[DimProps][DimTokens.borderWidth]

    UnstyledHorizontalSeparator(
        color = sidebarBorder,
        thickness = borderWidth,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = SidebarDefaults.ContentPadding),
    )
}

@Composable
fun SidebarInset(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val background = Theme[ColorProps][ColorTokens.background]
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(background),
        content = { content() },
    )
}
